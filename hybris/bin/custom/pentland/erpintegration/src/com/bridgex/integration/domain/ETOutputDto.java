package com.bridgex.integration.domain;

import org.springframework.security.crypto.codec.Hex;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Goncharenko Mikhail, created on 16.11.2017.
 */
public class ETOutputDto {

  @JsonProperty("E_BIN_FILE")
  private byte[] binaryData;

  @JsonProperty("E_PDF_AVAIL")
  private String pdfAvailability;

  public byte[] getBinaryData() {
    return binaryData;
  }

  public void setBinaryData(String binaryData) {
    this.binaryData = Hex.decode(binaryData);
  }

  public String getPdfAvailability() {
    return pdfAvailability;
  }

  public void setPdfAvailability(String pdfAvailability) {
    this.pdfAvailability = pdfAvailability;
  }

}
