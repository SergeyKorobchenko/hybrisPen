package com.bridgex.integration.service.impl;

import com.bridgex.integration.constants.ErpintegrationConstants;
import com.bridgex.integration.domain.ETReturnDto;
import com.bridgex.integration.domain.OrderDetailsDto;
import com.bridgex.integration.domain.OrderDetailsResponse;

/**
 * @author Goncharenko Mikhail, created on 01.11.2017.
 */
public class OrderDetailsIntegrationService extends AbstractIntegrationService<OrderDetailsDto, OrderDetailsResponse> {

  @Override
  public String getServiceName() {
    return "int_sync_06";
  }

  @Override
  OrderDetailsResponse createFailedResponseBody(OrderDetailsResponse body) {
    OrderDetailsResponse result = body;
    if (body == null) {
      result = new OrderDetailsResponse();
    }
    if (result.getEtReturn() == null) {
      result.setEtReturn(new ETReturnDto(ErpintegrationConstants.RESPONSE.ET_RETURN.ERROR_TYPE));
    } else {
      result.getEtReturn().setType(ErpintegrationConstants.RESPONSE.ET_RETURN.ERROR_TYPE);
    }
    return result;
  }

}
