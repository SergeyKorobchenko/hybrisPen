package com.bridgex.integration.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Created by konstantin.pavlyukov on 11/22/2017.
 */
public class MultiBrandCartOutput {

  @JsonProperty("E_TOTAL_NET_PRICE")
  private String subtotalPrice;

  @JsonProperty("E_TOTAL_NET_CURRENCY")
  private String subtotalCurrency;

  @JsonProperty("E_TOTAL_NET_VALUE")
  private String totalPrice;

  @JsonProperty("E_TOTAL_TAX_VALUE")
  private String totalTaxPrice;
  
  @JsonProperty("E_SURCHARGE_VALUE")
  private String surCharge;

  @JsonProperty("ET_MATERIAL_INFO_LIST")
  private List<MaterialInfoDto> materialInfo;

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

  public String getSurCharge() {
	return surCharge;
}

public void setSurCharge(String surCharge) {
	this.surCharge = surCharge;
}

public List<MaterialInfoDto> getMaterialInfo() {
    return materialInfo;
  }

  public void setMaterialInfo(List<MaterialInfoDto> materialInfo) {
    this.materialInfo = materialInfo;
  }
}
