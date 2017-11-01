package com.bridgex.integration.util;

import java.io.IOException;

import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Created by konstantin.pavlyukov on 10/25/2017.
 */
public class IntegrationUtils {

  public static String toJSON(Object object, Logger LOG) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.writeValueAsString(object);
    }
    catch (IOException e) {
      LOG.error("JSON conversion error: " + e.getMessage());
      return "converting error";
    }
  }

}
