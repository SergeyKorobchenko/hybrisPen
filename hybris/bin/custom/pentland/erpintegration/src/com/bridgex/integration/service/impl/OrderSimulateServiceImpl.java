package com.bridgex.integration.service.impl;

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

}
