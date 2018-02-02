package com.bridgex.integration.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Created by konstantin.pavlyukov on 10/29/2017.
 */
public class MaterialInfoDto {

  @JsonProperty("MATERIAL_NUMBER")
  private String materialNumber;

  @JsonProperty("UNIT_NET_PRICE")
  private String unitPrice;

  @JsonProperty("IS_AVAILABLE")
  private String available;

  @JsonProperty("TOTAL_NET_PRICE")
  private String totalPrice;

  @JsonProperty("TOTAL_NET_VALUE")
  private String totalPriceWithTax;

  @JsonProperty("ET_MATERIAL_OUTPUT_GRID")
  private List<MaterialOutputGridDto> materialOutputGridList;

  public String getMaterialNumber() {
    return materialNumber;
  }

  public void setMaterialNumber(String materialNumber) {
    this.materialNumber = materialNumber;
  }

  public String getUnitPrice() {
    return unitPrice;
  }

  public void setUnitPrice(String unitPrice) {
    this.unitPrice = unitPrice;
  }

  public String getAvailable() {
    return available;
  }

  public void setAvailable(String available) {
    this.available = available;
  }

  public String getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(String totalPrice) {
    this.totalPrice = totalPrice;
  }

  public List<MaterialOutputGridDto> getMaterialOutputGridList() {
    return materialOutputGridList;
  }

  public void setMaterialOutputGridList(List<MaterialOutputGridDto> materialOutputGridList) {
    this.materialOutputGridList = materialOutputGridList;
  }

  public String getTotalPriceWithTax() {
    return totalPriceWithTax;
  }

  public void setTotalPriceWithTax(String totalPriceWithTax) {
    this.totalPriceWithTax = totalPriceWithTax;
  }
}

