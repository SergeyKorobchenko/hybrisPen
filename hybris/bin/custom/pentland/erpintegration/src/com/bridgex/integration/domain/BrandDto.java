package com.bridgex.integration.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BrandDto {

  @JsonProperty("BRAND")
  private String brand;

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

}
