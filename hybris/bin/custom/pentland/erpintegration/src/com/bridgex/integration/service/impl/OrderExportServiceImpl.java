package com.bridgex.integration.service.impl;

import java.util.Collections;

import com.bridgex.integration.constants.ErpintegrationConstants;
import com.bridgex.integration.domain.ETReturnDto;
import com.bridgex.integration.domain.ExportOrderResponse;
import com.bridgex.integration.domain.OrderExportDto;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/7/2017.
 */
public class OrderExportServiceImpl extends AbstractIntegrationService<OrderExportDto, ExportOrderResponse> {
  @Override
  public String getServiceName() {
    return "int_sync_05";
  }

  @Override
  ExportOrderResponse createFailedResponseBody(ExportOrderResponse body) {
    ExportOrderResponse result = body;
    if (body == null) {
      result = new ExportOrderResponse();
    }
    if (result.getEtReturn() == null) {
      result.setEtReturn(Collections.singletonList(new ETReturnDto(ErpintegrationConstants.RESPONSE.ET_RETURN.ERROR_TYPE)));
    } else {
      result.getEtReturn().add(new ETReturnDto(ErpintegrationConstants.RESPONSE.ET_RETURN.ERROR_TYPE));
    }
    return result;
  }
}
