package com.bridgex.core.integration.impl;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.client.ResourceAccessException;

import com.bridgex.core.integration.PentlandInvoicePDFService;
import com.bridgex.integration.constants.ErpintegrationConstants;
import com.bridgex.integration.domain.DocumentDto;
import com.bridgex.integration.domain.DocumentResponse;
import com.bridgex.integration.service.IntegrationService;

/**
 * @author Goncharenko Mikhail, created on 15.11.2017.
 */
public class DefaultPentlandInvoicePDFService implements PentlandInvoicePDFService {

  private IntegrationService<DocumentDto, DocumentResponse> integrationService;

  @Override
  public DocumentResponse requestData(DocumentDto request) {
    DocumentResponse response = integrationService.sendRequest(request, DocumentResponse.class).getBody();
    checkRequestSuccess(response);
    return response;
  }

  private void checkRequestSuccess(DocumentResponse response) {
    if (response.getEtReturn().getType().equals(ErpintegrationConstants.RESPONSE.ET_RETURN.ERROR_TYPE)) {
      throw new ResourceAccessException("ERP request failed with response: " + response.getEtReturn().getMessage());
    }
  }

  @Required
  public void setIntegrationService(IntegrationService<DocumentDto, DocumentResponse> integrationService) {
    this.integrationService = integrationService;
  }
}
