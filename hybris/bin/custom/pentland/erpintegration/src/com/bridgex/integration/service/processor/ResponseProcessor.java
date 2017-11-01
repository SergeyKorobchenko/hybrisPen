package com.bridgex.integration.service.processor;

import org.springframework.http.ResponseEntity;

/**
 * @author Created by konstantin.pavlyukov on 10/27/2017.
 */
public interface ResponseProcessor {

  void process(ResponseEntity response);

}
