package com.bridgex.integration.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Goncharenko Mikhail, created on 15.11.2017.
 */
public class DocumentResponse {

  @JsonProperty("E_BIN_FILE")
  private byte[] binaryData;

  @JsonProperty("E_PDF_AVAIL")
  private String pdfAvailability;

  @JsonProperty("ET_RETURN")
  private ETReturnDto etReturn;

  public byte[] getBinaryData() {
    return binaryData;
  }

  public void setBinaryData(byte[] binaryData) {
    this.binaryData = binaryData;
  }

  public String getPdfAvailability() {
    return pdfAvailability;
  }

  public void setPdfAvailability(String pdfAvailability) {
    this.pdfAvailability = pdfAvailability;
  }

  public ETReturnDto getEtReturn() {
    return etReturn;
  }

  public void setEtReturn(ETReturnDto etReturn) {
    this.etReturn = etReturn;
  }
}
