package com.bridgex.integration.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Goncharenko Mikhail, created on 15.11.2017.
 */
public class DocumentDto {

   @JsonProperty("I_DOCUMENT_NO")
   private String invoiceCode;

   @JsonProperty("I_SERVICE_CUSTOMER")
   private String serviceCustomer;

   @JsonProperty("I_LANGUAGE")
   private String language;

   @JsonProperty("I_APPL_KEY")
   private String appKey;

  public String getInvoiceCode() {
    return invoiceCode;
  }

  public void setInvoiceCode(String invoiceCode) {
    this.invoiceCode = invoiceCode;
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

  public String getAppKey() {
    return appKey;
  }

  public void setAppKey(String appKey) {
    this.appKey = appKey;
  }
}
