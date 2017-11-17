package com.bridgex.integration.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Goncharenko Mikhail, created on 01.11.2017.
 */
public class OrderDetailsDto {

  @JsonProperty("I_SDOC_NUMBER")
  private String orderCode;

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
