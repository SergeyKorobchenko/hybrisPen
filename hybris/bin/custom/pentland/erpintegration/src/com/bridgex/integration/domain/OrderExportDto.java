package com.bridgex.integration.domain;

import java.util.Date;
import java.util.List;

import com.bridgex.integration.constants.ErpintegrationConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/17/2017.
 */
public class OrderExportDto {
  public OrderExportDto(){
  }

  @JsonProperty("I_DOC_TYPE")
  private String docType = "";

  @JsonProperty("I_LANGUAGE")
  private String lang = ErpintegrationConstants.REQUEST.DEFAULT_LANGUAGE;

  @JsonProperty("I_SERVICE_CONSUMER")
  private String serviceConsumer = ErpintegrationConstants.REQUEST.DEFAULT_SERVICE_CONSUMER;

  @JsonProperty("I_CHANNEL_DOC_NO")
  private String docNumber = "";

  //not used
//  @JsonProperty("I_REFERENCE_USER")
//  private String referenceUser = "";

  @JsonProperty("I_CUSTOMER_NUMBER")
  private String sapCustomerID = "";

  @JsonProperty("I_SHIP_TO")
  private String shippingAddress = "";

  @JsonProperty("I_REQUESTED_DELIVERY_DATE")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
  private Date rdd;

  @JsonProperty("I_CANCEL_DATE")
//  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
  private String cancelDate = "00000000";

  @JsonProperty("I_PURCHASE_ORDER_NUMBER")
  private String purchaseOrderNumber = "";

  @JsonProperty("I_PURCHASE_ORDER_TYPE")
  private String poType = ErpintegrationConstants.REQUEST.DEFAULT_PO_TYPE;

  @JsonProperty("I_DOC_REASON")
  private String techOrderReason = "";

  @JsonProperty("I_PAYMENT_TYPE")
  private String paymentType = "";

  @JsonProperty("I_PAYMENT_TRANSACTION_CODE")
  private String paymentTransactionCode = "";

  @JsonProperty("I_ORDER_NOTES")
  private String customerComment = "";

  //not used
  @JsonProperty("I_REFERENCE_DOCUMENT")
  private String refDocNo = "";

  @JsonProperty("I_USER_EMAIL")
  private String email = "";

  @JsonProperty("I_MULTIBRAND_ORDER_INPUT_T")
  private List<MultiBrandOrderInput> orderEntries;

  @JsonProperty("IT_EMAIL")
  private List<UserContactsDto> userContacts;


  public String getDocType() {
    return docType;
  }

  public void setDocType(String docType) {
    this.docType = docType;
  }

  public String getLang() {
    return lang;
  }

  public void setLang(String lang) {
    if(lang != null) {
      this.lang = lang;
    }
  }

  public String getServiceConsumer() {
    return serviceConsumer;
  }

  public void setServiceConsumer(String serviceConsumer) {
    this.serviceConsumer = serviceConsumer;
  }

  public String getDocNumber() {
    return docNumber;
  }

  public void setDocNumber(String docNumber) {
    if(docNumber != null) {
      this.docNumber = docNumber;
    }
  }

  public String getPurchaseOrderNumber() {
    return purchaseOrderNumber;
  }

  public void setPurchaseOrderNumber(String purchaseOrderNumber) {
    if(purchaseOrderNumber != null) {
      this.purchaseOrderNumber = purchaseOrderNumber;
    }
  }

  public Date getRdd() {
    return rdd;
  }

  public void setRdd(Date rdd) {
    this.rdd = rdd;
  }

  public String getSapCustomerID() {
    return sapCustomerID;
  }

  public void setSapCustomerID(String sapCustomerID) {
    if(sapCustomerID != null) {
      this.sapCustomerID = sapCustomerID;
    }
  }

  public String getShippingAddress() {
    return shippingAddress;
  }

  public void setShippingAddress(String shippingAddress) {
    if(shippingAddress != null) {
      this.shippingAddress = shippingAddress;
    }
  }

//  public String getReferenceUser() {
//    return referenceUser;
//  }
//
//  public void setReferenceUser(String referenceUser) {
//    this.referenceUser = referenceUser;
//  }

  public String getPoType() {
    return poType;
  }

  public void setPoType(String poType) {
    if(poType != null) {
      this.poType = poType;
    }
  }

  public String getCancelDate() {
    return cancelDate;
  }

  public void setCancelDate(String cancelDate) {
    if(cancelDate != null) {
      this.cancelDate = cancelDate;
    }
  }

  public String getRefDocNo() {
    return refDocNo;
  }

  public void setRefDocNo(String refDocNo) {
    this.refDocNo = refDocNo;
  }

  public String getTechOrderReason() {
    return techOrderReason;
  }

  public void setTechOrderReason(String techOrderReason) {
    if(techOrderReason != null) {
      this.techOrderReason = techOrderReason;
    }
  }

  public String getPaymentType() {
    return paymentType;
  }

  public void setPaymentType(String paymentType) {
    if(paymentType != null) {
      this.paymentType = paymentType;
    }
  }

  public String getCustomerComment() {
    return customerComment;
  }

  public void setCustomerComment(String customerComment) {
    if(customerComment != null) {
      this.customerComment = customerComment;
    }
  }

  public List<MultiBrandOrderInput> getOrderEntries() {
    return orderEntries;
  }

  public void setOrderEntries(List<MultiBrandOrderInput> orderEntries) {
    this.orderEntries = orderEntries;
  }

  public List<UserContactsDto> getUserContacts() {
    return userContacts;
  }

  public void setUserContacts(List<UserContactsDto> userContacts) {
    this.userContacts = userContacts;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPaymentTransactionCode() {
    return paymentTransactionCode;
  }

  public void setPaymentTransactionCode(String paymentTransactionCode) {
    if(paymentTransactionCode != null){
      this.paymentTransactionCode = paymentTransactionCode;
    }
  }
}
