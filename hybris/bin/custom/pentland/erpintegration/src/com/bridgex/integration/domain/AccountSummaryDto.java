package com.bridgex.integration.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Goncharenko Mikhail, created on 09.11.2017.
 */
public class AccountSummaryDto {

  @JsonProperty("I_CUSTOMER_NUMBER")
  private String sapCustomerId;

  @JsonProperty("IT_BRAND")
  private List<BrandDto> brands;

  @JsonProperty("I_LANGUAGE")
  private String language;

  public String getSapCustomerId() {
    return sapCustomerId;
  }

  public void setSapCustomerId(String sapCustomerId) {
    this.sapCustomerId = sapCustomerId;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public List<BrandDto> getBrands() {
    return brands;
  }

  public void setBrands(List<BrandDto> brands) {
    this.brands = brands;
  }
}

