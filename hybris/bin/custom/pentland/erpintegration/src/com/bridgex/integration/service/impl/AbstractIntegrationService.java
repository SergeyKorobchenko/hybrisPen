package com.bridgex.integration.service.impl;

import java.net.URI;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import com.bridgex.integration.constants.ErpintegrationConstants;
import com.bridgex.integration.domain.ETReturnDto;
import com.bridgex.integration.domain.MultiBrandCartResponse;
import com.bridgex.integration.rest.RESTClient;
import com.bridgex.integration.service.IntegrationService;
import com.bridgex.integration.service.processor.ResponseProcessor;

import de.hybris.platform.util.Config;

/**
 * @author Created by konstantin.pavlyukov on 10/26/2017.
 */
public abstract class AbstractIntegrationService<REQUEST, RESPONSE> implements IntegrationService<REQUEST, RESPONSE> {

  private final static String DEF_ERP_INT_PROP = "erp.integration.";
  private final static String DEF_URL_PROP = ".url";

  private RESTClient client;
  private ResponseProcessor responseProcessor;
  private HttpHeaders headers;
  private boolean basicAuth = true;

  public RESTClient getClient() {
    return client;
  }

  @Required
  public void setClient(RESTClient client) {
    this.client = client;
  }

  public ResponseProcessor getResponseProcessor() {
    return responseProcessor;
  }

  @Required
  public void setResponseProcessor(ResponseProcessor responseProcessor) {
    this.responseProcessor = responseProcessor;
  }

  public HttpHeaders getHeaders() {
    return headers = Optional.ofNullable(headers).orElse(new HttpHeaders());
  }

  public void setHeaders(HttpHeaders headers) {
    this.headers = headers;
  }

  public boolean isBasicAuth() {
    return basicAuth;
  }

  public void setBasicAuth(boolean basicAuth) {
    this.basicAuth = basicAuth;
  }

  public abstract String getServiceName();

  public HttpMethod getMethod() {
    return HttpMethod.POST;
  }

  public String getServiceUrl() {
    return Config.getString(DEF_ERP_INT_PROP + getServiceName() + DEF_URL_PROP, StringUtils.EMPTY);
  }

  @Override
  public ResponseEntity<RESPONSE> sendRequest(final REQUEST requestDto, final Class responseClass) {
    if (isBasicAuth()) {basicAuth();}
    final URI uri = UriComponentsBuilder.fromHttpUrl(getServiceUrl()).build().toUri();
    RequestEntity<REQUEST> request = new RequestEntity<>(requestDto, getHeaders(), getMethod(), uri);
    ResponseEntity<RESPONSE> response = getClient().sendPostRequest(request, responseClass);
    if (!(response.getStatusCode().is2xxSuccessful() || response.getStatusCode().is1xxInformational())) {
      RESPONSE body = response.getBody();
      body = createFailedResponseBody(body);
      response = new ResponseEntity<>(body, HttpStatus.ACCEPTED);
    }
    getResponseProcessor().process(response);
    return response;
  }

  public String getUsername() {
    return Config.getString(ErpintegrationConstants.ERP_USERNAME, StringUtils.EMPTY);
  }

  public String getPassword() {
    return Config.getString(ErpintegrationConstants.ERP_PASSWORD, StringUtils.EMPTY);
  }

  private void basicAuth() {
    String plainClientCredentials = getUsername() + ":" + getPassword();
    String base64ClientCredentials = new String(Base64.encodeBase64(plainClientCredentials.getBytes()));

    getHeaders().set("Authorization", "Basic " + base64ClientCredentials);
  }

  abstract RESPONSE createFailedResponseBody(final RESPONSE body);
}
