package com.bridgex.core.integration;

/**
 * @author Goncharenko Mikhail, created on 17.11.2017.
 */
public interface PentlandIntegrationService<REQUEST, RESPONSE> {

  RESPONSE requestData(REQUEST requestDto);

}
