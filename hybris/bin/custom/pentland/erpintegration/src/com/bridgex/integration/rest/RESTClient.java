package com.bridgex.integration.rest;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

/**
 * @author Created by konstantin.pavlyukov on 10/24/2017.
 */
public interface RESTClient {

  ResponseEntity sendPostRequest(RequestEntity requestEntity, Class responseClass);

}
