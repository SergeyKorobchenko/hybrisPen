package com.bridgex.integration.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Goncharenko Mikhail, created on 09.11.2017.
 */
public class AccountSummaryDto {

  @JsonProperty("I_CUSTOMER_NUMBER")
  private String sapCustomerId;

  @JsonProperty("I_SERVICE_CUSTOMER")
  private String serviceCustomer;

  @JsonProperty("I_LANGUAGE")
  private String language;

  @JsonProperty("BRANDS")
  private List<BrandDto> brands;

  public String getSapCustomerId() {
    return sapCustomerId;
  }

  public void setSapCustomerId(String sapCustomerId) {
    this.sapCustomerId = sapCustomerId;
  }

  public String getServiceCustomer() {
    return serviceCustomer;
  }

  public void setServiceCustomer(String serviceCustomer) {
    this.serviceCustomer = serviceCustomer;
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

