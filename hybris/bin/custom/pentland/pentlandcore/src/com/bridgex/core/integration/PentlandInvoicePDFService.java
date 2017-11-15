package com.bridgex.core.integration;

import com.bridgex.integration.domain.DocumentDto;
import com.bridgex.integration.domain.DocumentResponse;

/**
 * @author Goncharenko Mikhail, created on 15.11.2017.
 */
public interface PentlandInvoicePDFService {
  DocumentResponse requestData(DocumentDto request);
}