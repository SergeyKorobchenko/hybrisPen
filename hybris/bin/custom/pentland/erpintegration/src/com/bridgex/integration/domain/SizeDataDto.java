package com.bridgex.integration.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Created by konstantin.pavlyukov on 10/29/2017.
 */
public class SizeDataDto {

  @JsonProperty("EAN")
  private String ean;

  //Not used
  @JsonProperty("REQUESTED_SIZE")
  private String size;

  @JsonProperty("REQUESTED_QUANTITY")
  private String quantity;

  @JsonProperty("UOM")
  private String unit;

  public String getEan() {
    return ean;
  }

  public void setEan(String ean) {
    this.ean = ean;
  }

  public String getQuantity() {
    return quantity;
  }

  public void setQuantity(String quantity) {
    this.quantity = quantity;
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public String getSize() {
    return size;
  }

  public void setSize(String size) {
    this.size = size;
  }
}
