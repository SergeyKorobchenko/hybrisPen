package com.bridgex.integration.domain;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/7/2017.
 */
public class ExportOrderResponse implements Serializable {

  public ExportOrderResponse(){
  }

  @JsonProperty("E_DOC_NUMBER_T")
  private List<SapOrderDTO> sapOrderDTOList;

  @JsonProperty("ET_RETURN")
  private ETReturnDto etReturn;

  public ETReturnDto getEtReturn() {
    return etReturn;
  }

  public void setEtReturn(ETReturnDto etReturn) {
    this.etReturn = etReturn;
  }

  public List<SapOrderDTO> getSapOrderDTOList() {
    return sapOrderDTOList;
  }

  public void setSapOrderDTOList(List<SapOrderDTO> sapOrderDTOList) {
    this.sapOrderDTOList = sapOrderDTOList;
  }
}
