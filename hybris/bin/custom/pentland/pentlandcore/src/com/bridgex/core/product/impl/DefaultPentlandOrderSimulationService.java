package com.bridgex.core.product.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.ResponseEntity;

import com.bridgex.core.product.OrderSimulationService;
import com.bridgex.integration.constants.ErpintegrationConstants;
import com.bridgex.integration.domain.ETReturnDto;
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

  @Override
  public boolean successResponse(final MultiBrandCartResponse response) {
    boolean result = true;
    final List<ETReturnDto> returnList = response.getEtReturnList();
    if (CollectionUtils.isNotEmpty(returnList)) {
      for (final ETReturnDto returnDto : returnList) {
        if (ErpintegrationConstants.RESPONSE.ET_RETURN.SUCCESS_TYPE.equals(returnDto.getType())) {
          return true;
        }
        if (ErpintegrationConstants.RESPONSE.ET_RETURN.ERROR_TYPE.equals(returnDto.getType())) {
          result = false;
        }
      }
    }
    return result;
  }
}
