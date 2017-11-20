package com.bridgex.core.integration.impl;

import com.bridgex.integration.domain.ETReturnDto;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.client.ResourceAccessException;

import com.bridgex.core.integration.PentlandIntegrationService;
import com.bridgex.integration.constants.ErpintegrationConstants;
import com.bridgex.integration.domain.AccountSummaryDto;
import com.bridgex.integration.domain.AccountSummaryResponse;
import com.bridgex.integration.service.IntegrationService;

import de.hybris.platform.servicelayer.i18n.CommonI18NService;

/**
 * @author Goncharenko Mikhail, created on 13.11.2017.
 */
public class PentlandAccountSummaryService implements PentlandIntegrationService<AccountSummaryDto, AccountSummaryResponse> {

  private CommonI18NService commonI18NService;
  private IntegrationService<AccountSummaryDto,AccountSummaryResponse> integrationService;

  @Override
  public AccountSummaryResponse requestData(AccountSummaryDto request) {
    request.setLanguage(commonI18NService.getCurrentLanguage().getIsocode().toUpperCase());
    AccountSummaryResponse response = integrationService.sendRequest(request, AccountSummaryResponse.class).getBody();
    checkRequestSuccess(response);
    return response;
  }

  private void checkRequestSuccess(AccountSummaryResponse response) {
    if (response.getEtReturn() != null && !response.getEtReturn().isEmpty()) {
      ETReturnDto etReturn = response.getEtReturn().get(0);
      if (etReturn.getType().equals(ErpintegrationConstants.RESPONSE.ET_RETURN.ERROR_TYPE)) {
        throw new ResourceAccessException("ERP request failed with response: " + etReturn.getMessage());
      }
    }
  }

  @Required
  public void setCommonI18NService(CommonI18NService commonI18NService) {
    this.commonI18NService = commonI18NService;
  }

  @Required
  public void setIntegrationService(IntegrationService<AccountSummaryDto, AccountSummaryResponse> integrationService) {
    this.integrationService = integrationService;
  }
}
