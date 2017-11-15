package com.bridgex.core.integration.impl;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;

import com.bridgex.core.integration.PentlandOrderDetailsService;
import com.bridgex.integration.constants.ErpintegrationConstants;
import com.bridgex.integration.domain.AccountSummaryResponse;
import com.bridgex.integration.domain.OrderDetailsDto;
import com.bridgex.integration.domain.OrderDetailsResponse;
import com.bridgex.integration.service.IntegrationService;

/**
 * @author Goncharenko Mikhail, created on 02.11.2017.
 */
public class DefaultPentlandOrderDetailsService implements PentlandOrderDetailsService {

  private IntegrationService<OrderDetailsDto, OrderDetailsResponse> integrationService;

  @Override
  public OrderDetailsResponse requestData(OrderDetailsDto request) {
    OrderDetailsResponse response = integrationService.sendRequest(request, OrderDetailsResponse.class).getBody();
    checkRequestSuccess(response);
    return response;
  }

  private void checkRequestSuccess(OrderDetailsResponse response) {
    if (response.getEtReturn().getType().equals(ErpintegrationConstants.RESPONSE.ET_RETURN.ERROR_TYPE)) {
      throw new ResourceAccessException("ERP request failed with response: " + response.getEtReturn().getMessage());
    }
  }

  @Required
  public void setIntegrationService(IntegrationService<OrderDetailsDto, OrderDetailsResponse> integrationService) {
    this.integrationService = integrationService;
  }

}