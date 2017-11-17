package com.bridgex.core.order.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.bridgex.core.order.PentlandOrderExportService;
import com.bridgex.integration.domain.*;
import com.bridgex.integration.service.IntegrationService;

import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.core.enums.ExportStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.model.ModelService;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/7/2017.
 */
public class DefaultPentlandOrderExportService implements PentlandOrderExportService{

  private IntegrationService<MultiBrandCartDto, ExportOrderResponse> orderExportService;
  private ModelService                                               modelService;

  @Override
  public boolean exportOrder(OrderModel orderModel) {

    MultiBrandCartDto request = createOrderExportRequest(orderModel);
    ResponseEntity<ExportOrderResponse> exportOrderResponseResponseEntity = orderExportService.sendRequest(request, ExportOrderResponse.class);

    if(HttpStatus.OK.equals(exportOrderResponseResponseEntity.getStatusCode())){
      ExportOrderResponse responseBody = exportOrderResponseResponseEntity.getBody();
      ETReturnDto etReturn = responseBody.getEtReturn();
      if("S".equals(etReturn.getType())){
        return processSuccessfulResponse(orderModel, responseBody);
      }
    }
    orderModel.setExportStatus(ExportStatus.NOTEXPORTED);
    orderModel.setReexportRetries(orderModel.getReexportRetries() + 1);
    modelService.save(orderModel);
    return false;
  }

  private MultiBrandCartDto createOrderExportRequest(OrderModel orderModel) {
    OrderExportDto request = new OrderExportDto();
    request.setDocNumber(orderModel.getCode());
    return request;
  }

  private boolean processSuccessfulResponse(OrderModel orderModel, ExportOrderResponse responseBody) {List<SapOrderDto> sapOrderDTOList = responseBody.getSapOrderDTOList();
    List<OrderModel> byBrandOrderList = new ArrayList<>();
    for(SapOrderDto sapOrderDTO: sapOrderDTOList){
      OrderModel sapOrder = modelService.create(OrderModel.class);
      sapOrder.setCode(sapOrderDTO.getOrderCode());
      sapOrder.setSapBrand(sapOrderDTO.getSapBrand());
      OrderStatus newStatus = OrderStatus.valueOf(sapOrderDTO.getStatus());
      sapOrder.setStatus(newStatus);
      sapOrder.setTotalPrice(Double.parseDouble(sapOrderDTO.getTotalPrice()));
      sapOrder.setTotalQty(Integer.parseInt(sapOrderDTO.getTotalQty()));
      sapOrder.setRdd(sapOrderDTO.getRequestedDeliveryDate());
      sapOrder.setSapMsg(sapOrderDTO.getSapMessage());
      sapOrder.setSite(orderModel.getSite());
      sapOrder.setStore(orderModel.getStore());
      sapOrder.setSourceOrder(orderModel);
      sapOrder.setCurrency(orderModel.getCurrency());
      sapOrder.setDate(orderModel.getDate());
      sapOrder.setPurchaseOrderNumber(sapOrderDTO.getPoNumber());
      sapOrder.setUser(orderModel.getUser());
      sapOrder.setUnit(orderModel.getUnit());
      sapOrder.setSalesApplication(SalesApplication.SAP);
      modelService.save(sapOrder);
      byBrandOrderList.add(sapOrder);
    }
    orderModel.setExportStatus(ExportStatus.EXPORTED);
    orderModel.setReexportRetries(0);
    orderModel.setByBrandOrderList(byBrandOrderList);
    modelService.save(orderModel);
    return true;
  }

  @Required
  public void setOrderExportService(IntegrationService<MultiBrandCartDto, ExportOrderResponse> orderExportService) {
    this.orderExportService = orderExportService;
  }

  @Required
  public void setModelService(ModelService modelService) {
    this.modelService = modelService;
  }
}
