package com.bridgex.core.product.impl;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.ResponseEntity;

import com.bridgex.core.product.OrderSimulationService;
import com.bridgex.integration.domain.MultiBrandCartDto;
import com.bridgex.integration.domain.MultiBrandCartResponse;
import com.bridgex.integration.service.IntegrationService;

/**
 * @author Created by konstantin.pavlyukov on 11/8/2017.
 */
public class DefaultPentlandOrderSimulationService implements OrderSimulationService {

  private IntegrationService<MultiBrandCartDto, MultiBrandCartResponse> integrationService;

  public IntegrationService<MultiBrandCartDto, MultiBrandCartResponse> getIntegrationService() {
    return integrationService;
  }

  @Required
  public void setIntegrationService(IntegrationService<MultiBrandCartDto, MultiBrandCartResponse> integrationService) {
    this.integrationService = integrationService;
  }

  @Override
  public MultiBrandCartResponse simulateOrder(MultiBrandCartDto request) {
    ResponseEntity<MultiBrandCartResponse> response = integrationService.sendRequest(request, MultiBrandCartResponse.class);
    return response.getBody();
  }
}
