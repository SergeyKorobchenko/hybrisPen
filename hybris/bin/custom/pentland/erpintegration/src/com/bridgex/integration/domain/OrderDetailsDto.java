package com.bridgex.integration.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Goncharenko Mikhail, created on 01.11.2017.
 */
public class OrderDetailsDto {
  public OrderDetailsDto() {
  }

  @JsonProperty("I_SDOC_NUMBER")
  private String orderCode;

  @JsonProperty("I_SERVICE_CUSTOMER")
  private String serviceCustomer;

  @JsonProperty("I_VBTYP")
  private String orderType;

  @JsonProperty("I_LANGUAGE")
  private String language;

  @JsonProperty("I_CUSTOMER_VIEW_FLAG")
  private String customerViewFlag;

  public String getOrderCode() {
    return orderCode;
  }

  public void setOrderCode(String orderCode) {
    this.orderCode = orderCode;
  }

  public String getServiceCustomer() {
    return serviceCustomer;
  }

  public void setServiceCustomer(String serviceCustomer) {
    this.serviceCustomer = serviceCustomer;
  }

  public String getOrderType() {
    return orderType;
  }

  public void setOrderType(String orderType) {
    this.orderType = orderType;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getCustomerViewFlag() {
    return customerViewFlag;
  }

  public void setCustomerViewFlag(String customerViewFlag) {
    this.customerViewFlag = customerViewFlag;
  }
}
