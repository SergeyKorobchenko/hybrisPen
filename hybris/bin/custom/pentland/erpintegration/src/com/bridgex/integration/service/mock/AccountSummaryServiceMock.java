package com.bridgex.integration.service.mock;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.bridgex.integration.domain.AccountSummaryDto;
import com.bridgex.integration.domain.AccountSummaryResponse;
import com.bridgex.integration.service.impl.AbstractIntegrationService;

/**
 * @author Goncharenko Mikhail, created on 09.11.2017.
 */
public class AccountSummaryServiceMock extends AbstractIntegrationService<AccountSummaryDto, AccountSummaryResponse> {

  @Override
  public String getServiceName() {
    return "int_sync_02";
  }

  @Override
  public ResponseEntity<AccountSummaryResponse> sendRequest(AccountSummaryDto request, Class responseClass) {
    AccountSummaryResponse responseBody = new AccountSummaryResponse();
    return new ResponseEntity<AccountSummaryResponse>(responseBody, HttpStatus.OK);
  }
}
