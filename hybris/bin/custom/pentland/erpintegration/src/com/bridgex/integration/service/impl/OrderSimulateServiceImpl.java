package com.bridgex.integration.service.impl;

/**
 * @author Created by konstantin.pavlyukov on 10/26/2017.
 */

public class OrderSimulateServiceImpl<MultiBrandCartDto, MultiBrandCartResponse> extends AbstractIntegrationService<MultiBrandCartDto, MultiBrandCartResponse> {

  @Override
  public String getServiceName() {
    return "int_sync_04";
  }

}
