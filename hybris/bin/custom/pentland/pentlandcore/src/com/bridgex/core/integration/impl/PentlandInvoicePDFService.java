package com.bridgex.core.integration.impl;

import com.bridgex.integration.domain.ETReturnDto;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.client.ResourceAccessException;

import com.bridgex.core.integration.PentlandIntegrationService;
import com.bridgex.integration.constants.ErpintegrationConstants;
import com.bridgex.integration.domain.DocumentDto;
import com.bridgex.integration.domain.DocumentResponse;
import com.bridgex.integration.service.IntegrationService;

import de.hybris.platform.servicelayer.i18n.CommonI18NService;

/**
 * @author Goncharenko Mikhail, created on 15.11.2017.
 */
public class PentlandInvoicePDFService implements PentlandIntegrationService<DocumentDto,DocumentResponse> {

  private IntegrationService<DocumentDto, DocumentResponse> integrationService;
  private CommonI18NService commonI18NService;

  @Override
  public DocumentResponse requestData(DocumentDto request) {
    request.setLanguage(commonI18NService.getCurrentLanguage().getIsocode().toUpperCase());
    DocumentResponse response = integrationService.sendRequest(request, DocumentResponse.class).getBody();
    checkRequestSuccess(response);
    return response;
  }

  private void checkRequestSuccess(DocumentResponse response) {
    if (response.getEtReturn() != null && !response.getEtReturn().isEmpty()) {
      ETReturnDto etReturn = response.getEtReturn().get(0);
      if (etReturn.getType().equals(ErpintegrationConstants.RESPONSE.ET_RETURN.ERROR_TYPE)) {
        throw new ResourceAccessException("ERP request failed with response: " + etReturn.getMessage());
      }
    }
  }

  @Required
  public void setIntegrationService(IntegrationService<DocumentDto, DocumentResponse> integrationService) {
    this.integrationService = integrationService;
  }

  @Required
  public void setCommonI18NService(CommonI18NService commonI18NService) {
    this.commonI18NService = commonI18NService;
  }
}
