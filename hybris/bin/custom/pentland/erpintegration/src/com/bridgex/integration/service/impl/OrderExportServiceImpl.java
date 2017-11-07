package com.bridgex.integration.service.impl;

import com.bridgex.integration.domain.ExportOrderResponse;
import com.bridgex.integration.domain.MultiBrandCartDto;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/7/2017.
 */
public class OrderExportServiceImpl extends AbstractIntegrationService<MultiBrandCartDto, ExportOrderResponse> {
  @Override
  public String getServiceName() {
    return "int_sync_05";
  }
}
