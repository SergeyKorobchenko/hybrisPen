package com.bridgex.integration.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Created by konstantin.pavlyukov on 10/26/2017.
 */
public class MultiBrandCartDto {
  public MultiBrandCartDto() {
  }

  @JsonProperty("I_DOCUMENT_TYPE")
  private String docType;

  @JsonProperty("SERVICE_CONSUMER")
  private String serviceConsumer;

  @JsonProperty("I_LANGUAGE")
  private String lang;

  @JsonProperty("I_DOCUMENT_NUMBER")
  private String docNumber;

  //Not used
  @JsonProperty("I_SALES_REP")
  private String salesRep;
  //Not used
  @JsonProperty("I_SHIP_TO")
  private String shippingAddress;
  //Not used
  @JsonProperty("I_CANCEL_DATE")
  private String cancelDate;

  @JsonProperty("I_RDD")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private Date rdd;

  @JsonProperty("I_INITIAL_CHECK_FLAG")
  private String initialCheck;
  @JsonProperty("I_AVAILABILITY_CHECK_FLAG")
  private String availabilityCheck;
  @JsonProperty("I_PRICING_CHECK_FLAG")
  private String pricingCheck;
  @JsonProperty("I_CREDIT_CHECK_FLAG")
  private String creditCheck;

  @JsonProperty("I_MULTI_BRAND_CART_INPUT_T")
  private List<MultiBrandCartInput> cartInput;

  public String getDocType() {
    return docType;
  }

  public void setDocType(String docType) {
    this.docType = docType;
  }

  public String getServiceConsumer() {
    return serviceConsumer;
  }

  public void setServiceConsumer(String serviceConsumer) {
    this.serviceConsumer = serviceConsumer;
  }

  public String getLang() {
    return lang;
  }

  public void setLang(String lang) {
    this.lang = lang;
  }

  public String getDocNumber() {
    return docNumber;
  }

  public void setDocNumber(String docNumber) {
    this.docNumber = docNumber;
  }

  public String getSalesRep() {
    return salesRep;
  }

  public void setSalesRep(String salesRep) {
    this.salesRep = salesRep;
  }

  public String getShippingAddress() {
    return shippingAddress;
  }

  public void setShippingAddress(String shippingAddress) {
    this.shippingAddress = shippingAddress;
  }

  public String getCancelDate() {
    return cancelDate;
  }

  public void setCancelDate(String cancelDate) {
    this.cancelDate = cancelDate;
  }

  public Date getRdd() {
    return rdd;
  }

  public void setRdd(Date rdd) {
    this.rdd = rdd;
  }

  public String getInitialCheck() {
    return initialCheck;
  }

  public void setInitialCheck(String initialCheck) {
    this.initialCheck = initialCheck;
  }

  public String getAvailabilityCheck() {
    return availabilityCheck;
  }

  public void setAvailabilityCheck(String availabilityCheck) {
    this.availabilityCheck = availabilityCheck;
  }

  public String getPricingCheck() {
    return pricingCheck;
  }

  public void setPricingCheck(String pricingCheck) {
    this.pricingCheck = pricingCheck;
  }

  public String getCreditCheck() {
    return creditCheck;
  }

  public void setCreditCheck(String creditCheck) {
    this.creditCheck = creditCheck;
  }

  public List<MultiBrandCartInput> getCartInput() {
    return cartInput;
  }

  public void setCartInput(List<MultiBrandCartInput> cartInput) {
    this.cartInput = cartInput;
  }
}
