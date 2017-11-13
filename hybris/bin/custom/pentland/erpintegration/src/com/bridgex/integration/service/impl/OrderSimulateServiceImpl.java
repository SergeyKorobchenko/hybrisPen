package com.bridgex.integration.service.impl;

import com.bridgex.integration.constants.ErpintegrationConstants;
import com.bridgex.integration.domain.ETReturnDto;
import com.bridgex.integration.domain.MultiBrandCartDto;
import com.bridgex.integration.domain.MultiBrandCartResponse;

/**
 * @author Created by konstantin.pavlyukov on 10/26/2017.
 */

public class OrderSimulateServiceImpl extends AbstractIntegrationService<MultiBrandCartDto, MultiBrandCartResponse> {

  @Override
  public String getServiceName() {
    return "int_sync_04";
  }

  @Override
  MultiBrandCartResponse createFailedResponseBody(MultiBrandCartResponse body) {
    MultiBrandCartResponse result = body;
    if (body == null) {
      result = new MultiBrandCartResponse();
    }
    if (result.getEtReturn() == null) {
      result.setEtReturn(new ETReturnDto(ErpintegrationConstants.RESPONSE.ET_RETURN.ERROR_TYPE));
    } else {
      result.getEtReturn().setType(ErpintegrationConstants.RESPONSE.ET_RETURN.ERROR_TYPE);
    }
    return result;
  }

}
