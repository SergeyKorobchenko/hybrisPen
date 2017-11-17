package com.bridgex.core.integration;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;

import com.bridgex.integration.domain.OrderDetailsDto;
import com.bridgex.integration.domain.OrderDetailsResponse;
import com.bridgex.integration.service.IntegrationService;

/**
 * @author Goncharenko Mikhail, created on 02.11.2017.
 */
public class OrderDetailsService {

  private IntegrationService<OrderDetailsDto, OrderDetailsResponse> integrationService;

  public OrderDetailsResponse requestOrderDetails(OrderDetailsDto request) {
    ResponseEntity<OrderDetailsResponse> response = integrationService.sendRequest(request, OrderDetailsResponse.class);
    return response.getBody();
  }

  @Required
  public void setIntegrationService(IntegrationService<OrderDetailsDto, OrderDetailsResponse> integrationService) {
    this.integrationService = integrationService;
  }
}
