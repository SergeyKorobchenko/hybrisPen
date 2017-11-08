package com.bridgex.integration.service.mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.bridgex.integration.domain.ETReturnDto;
import com.bridgex.integration.domain.ExportOrderResponse;
import com.bridgex.integration.domain.MultiBrandCartDto;
import com.bridgex.integration.domain.SapOrderDTO;
import com.bridgex.integration.service.impl.OrderExportServiceImpl;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/7/2017.
 */
public class OrderExportServiceMock extends OrderExportServiceImpl {

  @Override
  public ResponseEntity<ExportOrderResponse> sendRequest(MultiBrandCartDto requestDto, Class responseClass) {
    ExportOrderResponse response = new ExportOrderResponse();

    SapOrderDTO order1 = new SapOrderDTO();
    order1.setOrderCode(requestDto.getDocNumber() + "_1");
    order1.setOrderType(requestDto.getDocType());
    order1.setRequestedDeliveryDate(new Date());
    order1.setSapBrand("mitre");
    order1.setStatus("CREATED");
    order1.setTotalPrice("1000");
    order1.setTotalQty("2");

    SapOrderDTO order2 = new SapOrderDTO();
    order2.setOrderCode(requestDto.getDocNumber() + "_2");
    order2.setOrderType(requestDto.getDocType());
    order2.setRequestedDeliveryDate(DateUtils.addDays(new Date(), 2));
    order2.setSapBrand("canterbury");
    order2.setStatus("CREATED");
    order2.setTotalPrice("2000");
    order2.setTotalQty("1");

    List<SapOrderDTO> orders = new ArrayList<>();
    orders.add(order1);
    orders.add(order2);
    response.setSapOrderDTOList(orders);

    ETReturnDto etReturn = new ETReturnDto();
    etReturn.setType("S");
    etReturn.setNumber("007");
    etReturn.setMessage("Materials successfully returned");
    response.setEtReturn(etReturn);
    ResponseEntity<ExportOrderResponse> mock = new ResponseEntity<>(response, HttpStatus.OK);
    return mock;
  }

}
