package com.bridgex.integration.service.mock;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.bridgex.integration.domain.DocumentDto;
import com.bridgex.integration.domain.DocumentResponse;
import com.bridgex.integration.service.impl.InvoicePDFServiceImpl;

/**
 * @author Goncharenko Mikhail, created on 15.11.2017.
 */
public class InvoicePDFServiceMock extends InvoicePDFServiceImpl {

  @Override
  public ResponseEntity<DocumentResponse> sendRequest(DocumentDto requestDto, Class responseClass) {
    DocumentResponse body = new DocumentResponse();
    return new ResponseEntity<DocumentResponse>(body, HttpStatus.OK);
  }
}
