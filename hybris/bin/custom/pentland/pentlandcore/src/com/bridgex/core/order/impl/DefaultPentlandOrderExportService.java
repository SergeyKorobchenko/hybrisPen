package com.bridgex.core.order.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.bridgex.core.customer.PentlandCustomerAccountService;
import com.bridgex.core.model.ApparelSizeVariantProductModel;
import com.bridgex.core.order.PentlandOrderExportService;
import com.bridgex.core.services.PentlandB2BUnitService;
import com.bridgex.integration.constants.ErpintegrationConstants;
import com.bridgex.integration.domain.*;
import com.bridgex.integration.service.IntegrationService;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.core.enums.ExportStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.variants.model.VariantProductModel;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/7/2017.
 */
public class DefaultPentlandOrderExportService implements PentlandOrderExportService{

  private static final Logger LOG = Logger.getLogger(DefaultPentlandOrderExportService.class);

  private IntegrationService<OrderExportDto, ExportOrderResponse> orderExportService;
  private ModelService                                            modelService;
  private PentlandB2BUnitService                                  pentlandB2BUnitService;
  private PentlandCustomerAccountService                          customerAccountService;

  @Override
  public boolean exportOrder(OrderModel orderModel) {

    OrderExportDto request = createOrderExportRequest(orderModel);
    ResponseEntity<ExportOrderResponse> exportOrderResponseEntity = orderExportService.sendRequest(request, ExportOrderResponse.class);

    ExportOrderResponse responseBody = exportOrderResponseEntity.getBody();

    if(successResponse(responseBody)){
      return processSuccessfulResponse(orderModel, responseBody);
    }

    orderModel.setExportStatus(ExportStatus.NOTEXPORTED);
    orderModel.setReexportRetries(orderModel.getReexportRetries() + 1);
    modelService.save(orderModel);
    return false;
  }

  private OrderExportDto createOrderExportRequest(OrderModel orderModel) {
    B2BUnitModel unit = orderModel.getUnit();
    AddressModel deliveryAddressModel = orderModel.getDeliveryAddress();
    OrderExportDto request = new OrderExportDto();

    fillBaseData(orderModel, unit, deliveryAddressModel, request);
    populateOrderEntries(orderModel, request);

    fillAddresses(orderModel, deliveryAddressModel, request);

    return request;
  }

  private void fillAddresses(OrderModel orderModel, AddressModel deliveryAddressModel, OrderExportDto request) {List<UserContactsDto> userContacts = new ArrayList<>();
    UserContactsDto deliveryAddressDto = new UserContactsDto();
    deliveryAddressDto.setEmail(orderModel.getUser().getUid());
    ShipToDto deliveryAddress = populateAddress(deliveryAddressModel, StringUtils.EMPTY);
    deliveryAddressDto.setShipToAddress(deliveryAddress);
    userContacts.add(deliveryAddressDto);
    if(orderModel.getMarkFor() != null){
      UserContactsDto markForAddressDto = new UserContactsDto();
      markForAddressDto.setEmail(orderModel.getUser().getUid());
      ShipToDto markForAddress = populateAddress(orderModel.getMarkFor(), ErpintegrationConstants.REQUEST.DEFAULT_ERP_FLAG_TRUE);
      markForAddressDto.setShipToAddress(markForAddress);
      userContacts.add(markForAddressDto);
    }
    request.setUserContacts(userContacts);
  }

  private ShipToDto populateAddress(AddressModel deliveryAddressModel, String markFor) {
    ShipToDto deliveryAddress = new ShipToDto();
    deliveryAddress.setId(deliveryAddressModel.getAddressID());
    deliveryAddress.setName(deliveryAddressModel.getDisplayName());
    deliveryAddress.setMarkForFlag(markFor);
    deliveryAddress.setStreet(deliveryAddressModel.getStreetname());
    deliveryAddress.setCity(deliveryAddressModel.getTown());
    deliveryAddress.setRegion(deliveryAddressModel.getRegion() != null ? deliveryAddressModel.getRegion().getIsocode() : StringUtils.EMPTY);
    deliveryAddress.setCountry(deliveryAddressModel.getCountry() != null ? deliveryAddressModel.getCountry().getIsocode() : StringUtils.EMPTY);
    deliveryAddress.setPostalCode(deliveryAddressModel.getPostalcode());
    return deliveryAddress;
  }

  private void fillBaseData(OrderModel orderModel, B2BUnitModel unit, AddressModel deliveryAddressModel, OrderExportDto request) {
    request.setLang(orderModel.getLanguage().getIsocode().toUpperCase());
    request.setDocNumber(orderModel.getCode());
    request.setPurchaseOrderNumber(orderModel.getPurchaseOrderNumber());

    request.setRdd(orderModel.getRdd());
    request.setSapCustomerID(unit.getSapID());
    request.setShippingAddress(deliveryAddressModel.getAddressID());

    request.setPaymentType(orderModel.getPaymentType().getCode());
    request.setCustomerComment(orderModel.getCustomerNotes());
    request.setEmail(orderModel.getUser().getUid());

    request.setPaymentTransactionCode(orderModel.getWorldpayOrderCode());
  }

  private void populateOrderEntries(OrderModel orderModel, OrderExportDto request) {
    //group entries by base product
    Map<ProductModel, List<AbstractOrderEntryModel>> entriesGroupedByStyle =
      orderModel.getEntries().stream().filter(entry -> entry.getProduct() instanceof ApparelSizeVariantProductModel)
                .collect(Collectors.groupingBy(entry -> ((VariantProductModel) entry.getProduct()).getBaseProduct()));

    List<MultiBrandOrderInput> styleEntries = new ArrayList<>();
    fillOrderEntries(orderModel, entriesGroupedByStyle, styleEntries);
    request.setOrderEntries(styleEntries);
  }

  private void fillOrderEntries(OrderModel orderModel, Map<ProductModel, List<AbstractOrderEntryModel>> entriesGroupedByStyle, List<MultiBrandOrderInput> styleEntries) {
    List<B2BUnitModel> usersB2BUnits = pentlandB2BUnitService.getUsersB2BUnits((B2BCustomerModel) orderModel.getUser());
    entriesGroupedByStyle.forEach((styleProduct, entries) -> {
      MultiBrandOrderInput groupedEntry = new MultiBrandOrderInput();
      groupedEntry.setMaterialNumber(styleProduct.getCode());
      String sapBrand = styleProduct.getSapBrand();
      if(StringUtils.isNotEmpty(sapBrand)) {
        B2BUnitModel unit = usersB2BUnits.stream().filter(unitModel -> sapBrand.equals(unitModel.getSapBrand())).findFirst().orElse(null);
        groupedEntry.setBrandCode(sapBrand);
        groupedEntry.setSalesOrg(unit.getSalesOrg());
        groupedEntry.setDistrChannel(unit.getDistCh());
      }
      groupedEntry.setSalesUnit(styleProduct.getUnit().getCode());

      List<SchedLinesDto> sizeEntries = new ArrayList<>();
      entries.forEach(size -> {
        if(size.getQuantity() > 0) {
          SchedLinesDto sizeEntry = new SchedLinesDto();
          ApparelSizeVariantProductModel sizeProduct = (ApparelSizeVariantProductModel)size.getProduct();
          sizeEntry.setEan(sizeProduct.getCode());
          sizeEntry.setQuantity(Long.toString(size.getQuantity()));
//          sizeEntry.setSize(sizeProduct.getSize());
          sizeEntry.setRdd(orderModel.getRdd());
          sizeEntries.add(sizeEntry);
        }
      });
      groupedEntry.setSchedLines(sizeEntries);
      styleEntries.add(groupedEntry);
    });
  }

  private boolean processSuccessfulResponse(OrderModel orderModel, ExportOrderResponse responseBody) {
    List<SapOrderDto> sapOrderDTOList = responseBody.getSapOrderDTOList();
    List<OrderModel> byBrandOrderList = new ArrayList<>();
    for(SapOrderDto sapOrderDTO: sapOrderDTOList){
      if(StringUtils.isEmpty(sapOrderDTO.getOrderCode())){
        continue;
      }
      OrderModel sapOrder = null;
      try {
        sapOrder = customerAccountService.getOrderForCode(sapOrderDTO.getOrderCode(), orderModel.getStore());
      } catch(ModelNotFoundException e){
        //this is ok, no order with this code was previously created
      } catch(AmbiguousIdentifierException e){
        //corrupted data
        LOG.error("Multiple orders found for code #" + sapOrderDTO.getOrderCode() + ", parent order #" + orderModel.getCode());
        orderModel.setReexportRetries(orderModel.getReexportRetries() + 1);
        orderModel.setExportStatus(ExportStatus.NOTEXPORTED);
        modelService.save(orderModel);
        return false;
      }
      if(sapOrder == null) {
        sapOrder = modelService.create(OrderModel.class);
      }
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

    ETReturnDto creditMessage = responseBody.getEtReturn().stream().filter(etReturnDto -> "023".equals(etReturnDto.getNumber())).findFirst().orElse(null);
    if(creditMessage != null){
      orderModel.setCreditCheckPassed(false);
    }else{
      orderModel.setCreditCheckPassed(true);
    }
    modelService.save(orderModel);
    return true;
  }

  protected boolean successResponse(final ExportOrderResponse response) {
    boolean result = true;
    final List<ETReturnDto> returnList = response.getEtReturn();
    if (CollectionUtils.isNotEmpty(returnList)) {
      for (final ETReturnDto returnDto : returnList) {
        if (ErpintegrationConstants.RESPONSE.ET_RETURN.SUCCESS_TYPE.equals(returnDto.getType())) {
          return true;
        }
        if (ErpintegrationConstants.RESPONSE.ET_RETURN.ERROR_TYPE.equals(returnDto.getType())) {
          result = false;
        }
      }
    }
    return result;
  }

  @Required
  public void setOrderExportService(IntegrationService<OrderExportDto, ExportOrderResponse> orderExportService) {
    this.orderExportService = orderExportService;
  }

  @Required
  public void setModelService(ModelService modelService) {
    this.modelService = modelService;
  }

  @Required
  public void setPentlandB2BUnitService(PentlandB2BUnitService pentlandB2BUnitService) {
    this.pentlandB2BUnitService = pentlandB2BUnitService;
  }

  @Required
  public void setCustomerAccountService(PentlandCustomerAccountService customerAccountService) {
    this.customerAccountService = customerAccountService;
  }
}
