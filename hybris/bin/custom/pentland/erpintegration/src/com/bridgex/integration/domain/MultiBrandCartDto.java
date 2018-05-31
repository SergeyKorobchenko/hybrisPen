package com.bridgex.integration.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bridgex.integration.constants.ErpintegrationConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Created by konstantin.pavlyukov on 10/26/2017.
 */
public class MultiBrandCartDto {
  public MultiBrandCartDto() {
  }

  @JsonProperty("I_DOCUMENT_TYPE")
  private String docType = "";

  @JsonProperty("I_SERVICE_CONSUMER")
  private String serviceConsumer = ErpintegrationConstants.REQUEST.DEFAULT_SERVICE_CONSUMER;

  @JsonProperty("I_LANGUAGE")
  private String lang = ErpintegrationConstants.REQUEST.DEFAULT_LANGUAGE;

  @JsonProperty("I_DOCUMENT_NUMBER")
  private String docNumber = "";

  //Not used
  @JsonProperty("I_SALES_REP")
  private String salesRep = "";

  @JsonProperty("I_CUSTOMER_NUMBER")
  private String sapCustomerID = "";

  //Not used
  @JsonProperty("I_SHIP_TO")
  private String shippingAddress = "";
  //Not used
  @JsonProperty("I_CANCEL_DATE")
  private String cancelDate = "";

  @JsonProperty("I_RDD")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
  private Date rdd;

  //Not used
  @JsonProperty("I_INITIAL_CHECK_FLAG")
  private String initialCheck = "";

  @JsonProperty("I_AVAILABILITY_CHECK_FLAG")
  private String availabilityCheck = "";
  @JsonProperty("I_PRICING_CHECK_FLAG")
  private String pricingCheck = "";
  @JsonProperty("I_CREDIT_CHECK_FLAG")
  private String creditCheck = "";

  //Not used
  @JsonProperty("I_UPC_LIST_T")
  private List upcList = new ArrayList<>();
  //Not used
  @JsonProperty("I_ATS_FLAG")
  private String ats = "";
  //Not used
  @JsonProperty("I_DOC_REASON")
  private String docReason = "";
  //Not used
  @JsonProperty("I_PO_TYPE")
  private String poType = "";
  
  @JsonProperty("I_SURCHARGE_VALUE")
  private String surCharge= "";

  @JsonProperty("I_MULTI_BRAND_CART_INPUT_T")
  private List<MultiBrandCartInput> cartInput;

  //Not used
  @JsonProperty("IT_PARAMETERS")
  private List itParams = new ArrayList<>();

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

  public String getSapCustomerID() {
    return sapCustomerID;
  }

  public void setSapCustomerID(String sapCustomerID) {
    this.sapCustomerID = sapCustomerID;
  }

  public List getUpcList() {
    return upcList;
  }

  public void setUpcList(List upcList) {
    this.upcList = upcList;
  }

  public String getAts() {
    return ats;
  }

  public void setAts(String ats) {
    this.ats = ats;
  }

  public String getDocReason() {
    return docReason;
  }

  public void setDocReason(String docReason) {
    this.docReason = docReason;
  }

  public String getPoType() {
    return poType;
  }

  public void setPoType(String poType) {
    this.poType = poType;
  }
  
	public String getSurCharge() {
		return surCharge;
	}

  public void setSurCharge(String surCharge) {
		this.surCharge = surCharge;
	}

  public List getItParams() {
    return itParams;
  }

  public void setItParams(List itParams) {
    this.itParams = itParams;
  }
}
