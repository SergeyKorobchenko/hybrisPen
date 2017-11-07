package com.bridgex.core.order.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.bridgex.core.order.PentlandOrderExportService;
import com.bridgex.integration.domain.ETReturnDto;
import com.bridgex.integration.domain.ExportOrderResponse;
import com.bridgex.integration.domain.MultiBrandCartDto;
import com.bridgex.integration.domain.SapOrderDTO;
import com.bridgex.integration.service.IntegrationService;

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

    //TODO fill in all request data in integration task
    MultiBrandCartDto request = new MultiBrandCartDto();
    ResponseEntity<ExportOrderResponse> exportOrderResponseResponseEntity = orderExportService.sendRequest(request, ExportOrderResponse.class);

    if(HttpStatus.OK.equals(exportOrderResponseResponseEntity.getStatusCode())){
      ExportOrderResponse responseBody = exportOrderResponseResponseEntity.getBody();
      ETReturnDto etReturn = responseBody.getEtReturn();
      if("S".equals(etReturn.getType())){
        List<SapOrderDTO> sapOrderDTOList = responseBody.getSapOrderDTOList();
        List<OrderModel> sapOrders = new ArrayList<>();
        for(SapOrderDTO sapOrderDTO: sapOrderDTOList){
          OrderModel sapOrder = modelService.create(OrderModel.class);
          sapOrder.setCode(sapOrderDTO.getOrderCode());
          sapOrder.setSapBrand(sapOrderDTO.getSapBrand());
          OrderStatus newStatus = OrderStatus.valueOf(sapOrderDTO.getStatus());
          sapOrder.setStatus(newStatus);
          sapOrder.setTotalPrice(Double.parseDouble(sapOrderDTO.getTotalPrice()));
          sapOrder.setTotalQty(Integer.parseInt(sapOrderDTO.getTotalQty()));
          sapOrder.setRdd(sapOrderDTO.getRequestedDeliveryDate());
          sapOrder.setSapMsg(sapOrderDTO.getSapMessage());
          modelService.save(sapOrder);
          sapOrders.add(sapOrder);
        }
        orderModel.setByBrandOrderList(sapOrders);
        orderModel.setExportStatus(ExportStatus.EXPORTED);
        modelService.save(orderModel);
        return true;
      }
    }
    orderModel.setExportStatus(ExportStatus.NOTEXPORTED);
    modelService.save(orderModel);
    return false;
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
