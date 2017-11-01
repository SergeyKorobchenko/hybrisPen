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

  @JsonProperty("E_TOTAL_NET_PRICE")
  private String subtotalPrice;

  @JsonProperty("E_TOTAL_NET_CURRENCY")
  private String subtotalCurrency;

  @JsonProperty("E_TOTAL_NET_VALUE")
  private String totalPrice;

  @JsonProperty("E_TOTAL_TAX_VALUE")
  private String totalTaxPrice;

  @JsonProperty("MATERIAL_INFO_LIST")
  private List<MaterialInfoDto> materialInfo;

  @JsonProperty("ET_RETURN")
  private ETReturnDto etReturn;

  public String getSubtotalPrice() {
    return subtotalPrice;
  }

  public void setSubtotalPrice(String subtotalPrice) {
    this.subtotalPrice = subtotalPrice;
  }

  public String getSubtotalCurrency() {
    return subtotalCurrency;
  }

  public void setSubtotalCurrency(String subtotalCurrency) {
    this.subtotalCurrency = subtotalCurrency;
  }

  public String getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(String totalPrice) {
    this.totalPrice = totalPrice;
  }

  public String getTotalTaxPrice() {
    return totalTaxPrice;
  }

  public void setTotalTaxPrice(String totalTaxPrice) {
    this.totalTaxPrice = totalTaxPrice;
  }

  public List<MaterialInfoDto> getMaterialInfo() {
    return materialInfo;
  }

  public void setMaterialInfo(List<MaterialInfoDto> materialInfo) {
    this.materialInfo = materialInfo;
  }

  public ETReturnDto getEtReturn() {
    return etReturn;
  }

  public void setEtReturn(ETReturnDto etReturn) {
    this.etReturn = etReturn;
  }
}
