package com.bridgex.integration.service.impl;

import com.bridgex.integration.domain.AccountSummaryDto;
import com.bridgex.integration.domain.AccountSummaryResponse;

/**
 * @author Goncharenko Mikhail, created on 09.11.2017.
 */
public class AccountSummaryServiceImpl extends AbstractIntegrationService<AccountSummaryDto, AccountSummaryResponse> {

  @Override
  public String getServiceName() {
    return "int_sync_02";
  }
}
