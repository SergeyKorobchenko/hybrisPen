package com.bridgex.integration.service.impl;

import org.springframework.http.ResponseEntity;

import com.bridgex.integration.constants.ErpintegrationConstants;
import com.bridgex.integration.domain.DocumentDto;
import com.bridgex.integration.domain.DocumentResponse;
import com.bridgex.integration.domain.ETReturnDto;
import com.bridgex.integration.domain.OrderDetailsResponse;

/**
 * @author Goncharenko Mikhail, created on 15.11.2017.
 */
public class InvoicePDFServiceImpl extends AbstractIntegrationService<DocumentDto, DocumentResponse> {

  @Override
  public String getServiceName() {
    return "int_sync_03";
  }

  @Override
  DocumentResponse createFailedResponseBody(DocumentResponse body) {
    DocumentResponse result = body;
    if (body == null) {
      result = new DocumentResponse();
    }
    if (result.getEtReturn() == null) {
      result.setEtReturn(new ETReturnDto(ErpintegrationConstants.RESPONSE.ET_RETURN.ERROR_TYPE));
    } else {
      result.getEtReturn().setType(ErpintegrationConstants.RESPONSE.ET_RETURN.ERROR_TYPE);
    }
    return result;
  }

  @Override
  public ResponseEntity<DocumentResponse> sendRequest(DocumentDto requestDto, Class responseClass) {
    requestDto.setServiceCustomer(ErpintegrationConstants.REQUEST.DEFAULT_SERVICE_CONSUMER);
    requestDto.setAppKey(ErpintegrationConstants.REQUEST.DEFAULT_APPLICATION_KEY);
    return super.sendRequest(requestDto, responseClass);
  }
}
