package com.bridgex.integration.service.impl;

import java.util.Collections;

import org.springframework.http.ResponseEntity;

import com.bridgex.integration.constants.ErpintegrationConstants;
import com.bridgex.integration.domain.ETReturnDto;
import com.bridgex.integration.domain.OrderDetailsDto;
import com.bridgex.integration.domain.OrderDetailsResponse;

/**
 * @author Goncharenko Mikhail, created on 01.11.2017.
 */
public class OrderDetailsServiceImpl extends AbstractIntegrationService<OrderDetailsDto, OrderDetailsResponse> {

  @Override
  public String getServiceName() {
    return "int_sync_06";
  }

  @Override
  public ResponseEntity<OrderDetailsResponse> sendRequest(OrderDetailsDto requestDto, Class responseClass) {

    requestDto.setCustomerViewFlag(ErpintegrationConstants.REQUEST.DEFAULT_ERP_FLAG_TRUE);
    requestDto.setOrderType(ErpintegrationConstants.REQUEST.DEFAULT_DOC_TYPE);
    return super.sendRequest(requestDto, responseClass);
  }

  @Override
  OrderDetailsResponse createFailedResponseBody(OrderDetailsResponse body) {
    OrderDetailsResponse result = body;
    if (body == null) {
      result = new OrderDetailsResponse();
    }
    if (result.getEtReturn() == null) {
      result.setEtReturn(Collections.singletonList(new ETReturnDto(ErpintegrationConstants.RESPONSE.ET_RETURN.ERROR_TYPE)));
    } else {
      result.getEtReturn().setType(ErpintegrationConstants.RESPONSE.ET_RETURN.ERROR_TYPE);
    }
    return result;
  }

}
