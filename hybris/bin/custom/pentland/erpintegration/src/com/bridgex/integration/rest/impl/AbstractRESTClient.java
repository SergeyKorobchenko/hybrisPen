package com.bridgex.integration.rest.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownHttpStatusCodeException;

import com.bridgex.integration.rest.RESTClient;
import com.bridgex.integration.util.IntegrationUtils;
import com.google.common.base.Stopwatch;

/**
 * @author Created by konstantin.pavlyukov on 10/25/2017.
 */
public abstract class AbstractRESTClient implements RESTClient {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractRESTClient.class.getName());
  private RestTemplate restTemplate;

  @Override
  public ResponseEntity sendPostRequest(RequestEntity requestEntity, Class responseClass) {
    return sendPostRequest(requestEntity, responseClass, getRestTemplate());
  }

  private ResponseEntity sendPostRequest(RequestEntity requestEntity, Class responseClass, RestTemplate restTemplate) {
    final Stopwatch stopwatch = Stopwatch.createUnstarted();
    stopwatch.start();
    try {
      LOG.info(IntegrationUtils.toJSON(requestEntity, LOG));

      ResponseEntity responseEntity = restTemplate.exchange(requestEntity, responseClass);

      LOG.info(IntegrationUtils.toJSON(responseEntity, LOG));

      return responseEntity;
    } catch (HttpClientErrorException e) {
      LOG.error("4xx error", e);
      LOG.error(e.getResponseBodyAsString());
      return ResponseEntity.status(e.getStatusCode()).body(null);
    }
    catch (HttpServerErrorException e) {
      LOG.error("5xx error", e);
      return ResponseEntity.status(e.getStatusCode()).body(null);
    }
    catch (ResourceAccessException e) {
      LOG.error("I/O error", e);
    }
    catch (UnknownHttpStatusCodeException e) {
      LOG.error("custom http status error", e);
    }
    catch (HttpMessageNotReadableException e) {
      LOG.error("json message is corrupted.", e);
    }
    finally{
      stopwatch.stop();
      LOG.info("Request to " + requestEntity.getUrl() + " took " + stopwatch.toString());
    }
    return ResponseEntity.badRequest().body(null);
  }

  @Required
  public void setRestTemplate(RestTemplate template) {
    this.restTemplate = template;
  }

  public RestTemplate getRestTemplate() {
    return this.restTemplate;
  }

}
