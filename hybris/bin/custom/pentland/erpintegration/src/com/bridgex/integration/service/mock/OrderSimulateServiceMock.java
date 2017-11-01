package com.bridgex.integration.service.mock;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.bridgex.integration.domain.ETReturnDto;
import com.bridgex.integration.domain.MultiBrandCartDto;
import com.bridgex.integration.domain.MultiBrandCartResponse;
import com.bridgex.integration.service.impl.OrderSimulateServiceImpl;

/**
 * @author Created by konstantin.pavlyukov on 10/30/2017.
 */
public class OrderSimulateServiceMock extends OrderSimulateServiceImpl {

  @Override
  public ResponseEntity<MultiBrandCartResponse> sendRequest(MultiBrandCartDto requestDto, Class responseClass) {
    MultiBrandCartResponse response = new MultiBrandCartResponse();
    ETReturnDto etReturn = new ETReturnDto();
    etReturn.setType("S");
    etReturn.setNumber("007");
    etReturn.setMessage("Materials successfully returned");
    response.setEtReturn(etReturn);
    ResponseEntity<MultiBrandCartResponse> mock = new ResponseEntity<>(response, HttpStatus.OK);
    return mock;
  }

}
