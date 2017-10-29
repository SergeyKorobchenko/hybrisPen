package com.bridgex.integration.service;

import org.springframework.http.ResponseEntity;

/**
 * @author Created by konstantin.pavlyukov on 10/26/2017.
 */
public interface IntegrationService<REQUEST> {

  ResponseEntity sendRequest(REQUEST requestDto, Class responseClass);
}
