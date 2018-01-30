package com.bridgex.integration.service.mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.bridgex.integration.domain.*;
import com.bridgex.integration.service.impl.OrderDetailsServiceImpl;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;

/**
 * @author Goncharenko Mikhail, created on 07.11.2017.
 */
public class OrderDetailsServiceMock extends OrderDetailsServiceImpl {

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

    response.setDeliveryAddressId("123580");
    response.setDeliveryAddressName("Test Ship-To Name");
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
    response.setEtReturn(Collections.emptyList());

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  private List<ETReturnDto> getEtReturnDto() {
    ETReturnDto etReturn = new ETReturnDto();
    etReturn.setType("S");
    etReturn.setNumber("007");
    etReturn.setMessage("Materials successfully returned");
    return Collections.singletonList(etReturn);
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
    entry.setShippedDate("20180101");
    entry.setShippedQuantity("10.00");
    entry.setSizeVariants(getSizeVariantsDto());
    return entry;
  }

  private List<SizeVariantDto> getSizeVariantsDto() {
    SizeVariantDto size = new SizeVariantDto();
    size.setEan("300147490");
    size.setLineNumber("1");
    size.setShipDate("20180101");
    size.setShipQty("10.00");
    size.setShipStatus("SHIPPED");
    size.setTotalQuantity("10.00");

    SizeVariantDto size2 = new SizeVariantDto();
    size2.setEan("300020465");
    size2.setLineNumber("2");
    size2.setShipDate("20180101");
    size2.setShipQty("5.00");
    size2.setShipStatus("SHIPPED");
    size2.setTotalQuantity("5.00");

    List<SizeVariantDto> list = new ArrayList<>();
    list.add(size);
    list.add(size2);

    return list;
  }

}