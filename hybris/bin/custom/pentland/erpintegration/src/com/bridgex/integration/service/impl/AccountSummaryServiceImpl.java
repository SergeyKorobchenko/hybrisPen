package com.bridgex.integration.service.impl;

import java.util.Collections;

import org.springframework.http.ResponseEntity;

import com.bridgex.integration.constants.ErpintegrationConstants;
import com.bridgex.integration.domain.AccountSummaryDto;
import com.bridgex.integration.domain.AccountSummaryResponse;
import com.bridgex.integration.domain.ETReturnDto;

/**
 * @author Goncharenko Mikhail, created on 09.11.2017.
 */
public class AccountSummaryServiceImpl extends AbstractIntegrationService<AccountSummaryDto, AccountSummaryResponse> {

  @Override
  public String getServiceName() {
    return "int_sync_02";
  }

  @Override
  public ResponseEntity<AccountSummaryResponse> sendRequest(AccountSummaryDto requestDto, Class responseClass) {
    return super.sendRequest(requestDto, responseClass);
  }

  @Override
  AccountSummaryResponse createFailedResponseBody(AccountSummaryResponse body) {
    AccountSummaryResponse result = body;
    if (body == null) {
      result = new AccountSummaryResponse();
    }
    if (result.getEtReturn() == null) {
      result.setEtReturn(Collections.singletonList(new ETReturnDto(ErpintegrationConstants.RESPONSE.ET_RETURN.ERROR_TYPE)));
    } else {
      result.getEtReturn().setType(ErpintegrationConstants.RESPONSE.ET_RETURN.ERROR_TYPE);
    }
    return result;
  }
}
