package com.bridgex.integration.domain;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Created by konstantin.pavlyukov on 10/26/2017.
 */
public class MultiBrandCartResponse implements Serializable {
  public MultiBrandCartResponse() {
  }

  @JsonProperty("ET_OUTPUT")
  private MultiBrandCartOutput multiBrandCartOutput;

  @JsonProperty("ET_RETURN")
  private List<ETReturnDto> etReturnList;

  public List<ETReturnDto> getEtReturnList() {
    return etReturnList;
  }

  public void setEtReturnList(List<ETReturnDto> etReturnList) {
    this.etReturnList = etReturnList;
  }

  public MultiBrandCartOutput getMultiBrandCartOutput() {
    return multiBrandCartOutput;
  }

  public void setMultiBrandCartOutput(MultiBrandCartOutput multiBrandCartOutput) {
    this.multiBrandCartOutput = multiBrandCartOutput;
  }
}
