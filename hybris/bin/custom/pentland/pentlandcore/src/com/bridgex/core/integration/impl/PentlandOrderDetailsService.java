package com.bridgex.core.integration.impl;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.client.ResourceAccessException;

import com.bridgex.core.integration.PentlandIntegrationService;
import com.bridgex.integration.constants.ErpintegrationConstants;
import com.bridgex.integration.domain.OrderDetailsDto;
import com.bridgex.integration.domain.OrderDetailsResponse;
import com.bridgex.integration.service.IntegrationService;

import de.hybris.platform.servicelayer.i18n.CommonI18NService;

/**
 * @author Goncharenko Mikhail, created on 02.11.2017.
 */
public class PentlandOrderDetailsService implements PentlandIntegrationService<OrderDetailsDto,OrderDetailsResponse> {

  private IntegrationService<OrderDetailsDto, OrderDetailsResponse> integrationService;
  private CommonI18NService                                         commonI18NService;

  @Override
  public OrderDetailsResponse requestData(OrderDetailsDto request) {
    request.setLanguage(commonI18NService.getCurrentLanguage().getIsocode().toUpperCase());
    OrderDetailsResponse response = integrationService.sendRequest(request, OrderDetailsResponse.class).getBody();
    checkRequestSuccess(response);
    return response;
  }

  private void checkRequestSuccess(OrderDetailsResponse response) {
    if (response.getEtReturn() != null && response.getEtReturn().getType().equals(ErpintegrationConstants.RESPONSE.ET_RETURN.ERROR_TYPE)) {
      throw new ResourceAccessException("ERP request failed with response: " + response.getEtReturn().getMessage());
    }
  }

  @Required
  public void setIntegrationService(IntegrationService<OrderDetailsDto, OrderDetailsResponse> integrationService) {
    this.integrationService = integrationService;
  }

  @Required
  public void setCommonI18NService(CommonI18NService commonI18NService) {
    this.commonI18NService = commonI18NService;
  }
}