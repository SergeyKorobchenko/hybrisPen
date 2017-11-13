package com.bridgex.integration.service.mock;

import java.util.Collections;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.bridgex.integration.domain.*;
import com.bridgex.integration.service.impl.OrderDetailsServiceImpl;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;

/**
 * @author Goncharenko Mikhail, created on 07.11.2017.
 */
public class OrderDetailsServiceImplMock extends OrderDetailsServiceImpl {

  @Override
  public String getServiceName() {
    return "int_sync_06";
  }

  @Override
  public ResponseEntity<OrderDetailsResponse> sendRequest(OrderDetailsDto requestDto, Class responseClass) {

    OrderDetailsResponse response = new OrderDetailsResponse();
    response.setCode("11234");
    response.setPurshaseOrderNumber("43523");
    response.setCreationTime(new Date());
    response.setRdd(new Date());
    response.setStatus("approved");
    response.setCurrency("GBP");
    response.setCustomerName("Akira Namomuta");
    response.setCustomerId("339982");

    response.setDeliveryAddressCity("London");
    response.setDeliveryAddressCountry("GB");
    response.setDeliveryAddressMarkForName("Akira");
    response.setDeliveryAddressMarkForId("7735");
    response.setDeliveryAddressStreet("Abbey Road, 4");
    response.setDeliveryAddressPostcode("223610");
    response.setDeliveryAddressState("London");

    response.setNetPrice("1000.00");
    response.setTaxPrice("10.00");
    response.setTotalPrice("1010.00");
    response.setTotalQuantity("10.00");

    response.setEtReturn(getEtReturnDto());
    response.setOrderEntries(Collections.singletonList(getOrderEntryDto()));

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  private ETReturnDto getEtReturnDto() {
    ETReturnDto etReturn = new ETReturnDto();
    etReturn.setType("S");
    etReturn.setNumber("007");
    etReturn.setMessage("Materials successfully returned");
    return etReturn;
  }

  private OrderEntryDto getOrderEntryDto() {
    OrderEntryDto entry = new OrderEntryDto();
    entry.setEntryNumber("0");
    entry.setProduct("300005449");
    entry.setEntryStatus("shipped");
    entry.setNetPrice("1000.00");
    entry.setUnitNetPrice("100.00");
    entry.setPrice("1010.00");
    entry.setQuantity("10.00");
    entry.setRdd(new Date());
    entry.setSizeVariants(Collections.singletonList(getSizeVariantDto()));
    return entry;
  }

  private SizeVariantDto getSizeVariantDto() {
    SizeVariantDto size = new SizeVariantDto();
    size.setEan("10908");
    size.setLineNumber("1");
    size.setShipDate(new Date());
    size.setShipQty("10.00");
    size.setShipStatus(ConsignmentStatus.PICKPACK.getCode());
    size.setTotalQuantity("10.00");
    return size;
  }
}