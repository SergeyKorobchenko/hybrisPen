package com.bridgex.integration.service.processor.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.bridgex.integration.constants.ErpintegrationConstants;
import com.bridgex.integration.domain.ETReturnDto;
import com.bridgex.integration.domain.MultiBrandCartResponse;
import com.bridgex.integration.service.processor.ResponseProcessor;

/**
 * @author Created by konstantin.pavlyukov on 10/27/2017.
 */
public class AbstractResponseProcessor implements ResponseProcessor {

  @Override
  public void process(final ResponseEntity response) {
  }
}
