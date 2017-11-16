package com.bridgex.integration.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Goncharenko Mikhail, created on 15.11.2017.
 */
public class DocumentResponse {

  @JsonProperty("ET_OUTPUT")
  private ETOutputDto etOutput;

  @JsonProperty("ET_RETURN")
  private ETReturnDto etReturn;

  public ETOutputDto getEtOutput() {
    return etOutput;
  }

  public void setEtOutput(ETOutputDto etOutput) {
    this.etOutput = etOutput;
  }

  public ETReturnDto getEtReturn() {
    return etReturn;
  }

  public void setEtReturn(List<ETReturnDto> etReturn) {
    this.etReturn = etReturn.get(0);
  }
}
