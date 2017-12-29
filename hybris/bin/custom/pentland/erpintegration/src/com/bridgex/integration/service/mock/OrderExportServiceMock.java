package com.bridgex.integration.service.mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.bridgex.integration.domain.*;
import com.bridgex.integration.service.impl.OrderExportServiceImpl;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/7/2017.
 */
public class OrderExportServiceMock extends OrderExportServiceImpl {

  @Override
  public ResponseEntity<ExportOrderResponse> sendRequest(OrderExportDto requestDto, Class responseClass) {
    ExportOrderResponse response = new ExportOrderResponse();

    SapOrderDto order1 = new SapOrderDto();
    order1.setOrderCode(requestDto.getDocNumber() + "_1");
    order1.setOrderType("ZZRO");
    order1.setRequestedDeliveryDate(new Date());
    order1.setSapBrand("05");
    order1.setStatus("CREATED");
    order1.setTotalPrice("1000");
    order1.setTotalQty("2");

    SapOrderDto order2 = new SapOrderDto();
    order2.setOrderCode(requestDto.getDocNumber() + "_2");
    order2.setOrderType("ZMTO");
    order2.setRequestedDeliveryDate(DateUtils.addDays(new Date(), 2));
    order2.setSapBrand("0Q");
    order2.setStatus("CREATED");
    order2.setTotalPrice("2000");
    order2.setTotalQty("1");

    List<SapOrderDto> orders = new ArrayList<>();
    orders.add(order1);
    orders.add(order2);
    response.setSapOrderDTOList(orders);

    ETReturnDto etReturn = new ETReturnDto();
    etReturn.setType("S");
    etReturn.setNumber("007");
    etReturn.setMessage("Materials successfully returned");
    response.setEtReturn(Collections.singletonList(etReturn));
    ResponseEntity<ExportOrderResponse> mock = new ResponseEntity<>(response, HttpStatus.OK);
    return mock;
  }

}
