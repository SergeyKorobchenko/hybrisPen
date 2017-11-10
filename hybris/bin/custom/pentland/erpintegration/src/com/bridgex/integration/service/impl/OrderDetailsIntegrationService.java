package com.bridgex.integration.service.impl;

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

}
