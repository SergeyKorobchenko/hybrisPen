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
  private String docType = ErpintegrationConstants.REQUEST.DEFAULT_DOC_TYPE;

  @JsonProperty("I_LANGUAGE")
  private String lang = ErpintegrationConstants.REQUEST.DEFAULT_LANGUAGE;

  @JsonProperty("SERVICE_CONSUMER")
  private String serviceConsumer = ErpintegrationConstants.REQUEST.DEFAULT_SERVICE_CONSUMER;

  @JsonProperty("I_CHANNEL_DOC_NO")
  private String docNumber;

  //not used
  @JsonProperty("I_REFERENCE_USER")
  private String referenceUser;

  @JsonProperty("I_PO_NUMBER")
  private String purchaseOrderNumber;

  //not used
  @JsonProperty("I_PO_TYPE")
  private String poType;

  @JsonProperty("I_RDD")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private Date rdd;

  @JsonProperty("I_CANCEL_DATE")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private Date cancelDate;

  @JsonProperty("I_CUSTOMER_NUMBER")
  private String sapCustomerID;

  @JsonProperty("I_SHIP_TO")
  private String shippingAddress;

  //not used
  @JsonProperty("I_REF_DOC_NO")
  private String refDocNo;

  @JsonProperty("I_DOC_REASON")
  private String techOrderReason;

  @JsonProperty("PAYMENT_TYPE")
  private String paymentType;

  @JsonProperty("ORDER_NOTES")
  private String customerComment;

  @JsonProperty("I_MULTIBRAND_INPUT_T")
  private List<MultiBrandOrderInput> orderEntries;

  @JsonProperty("I_EMAIL_T")
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
    this.lang = lang;
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
    this.docNumber = docNumber;
  }

  public String getPurchaseOrderNumber() {
    return purchaseOrderNumber;
  }

  public void setPurchaseOrderNumber(String purchaseOrderNumber) {
    this.purchaseOrderNumber = purchaseOrderNumber;
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
    this.sapCustomerID = sapCustomerID;
  }

  public String getShippingAddress() {
    return shippingAddress;
  }

  public void setShippingAddress(String shippingAddress) {
    this.shippingAddress = shippingAddress;
  }

  public String getReferenceUser() {
    return referenceUser;
  }

  public void setReferenceUser(String referenceUser) {
    this.referenceUser = referenceUser;
  }

  public String getPoType() {
    return poType;
  }

  public void setPoType(String poType) {
    this.poType = poType;
  }

  public Date getCancelDate() {
    return cancelDate;
  }

  public void setCancelDate(Date cancelDate) {
    this.cancelDate = cancelDate;
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
    this.techOrderReason = techOrderReason;
  }

  public String getPaymentType() {
    return paymentType;
  }

  public void setPaymentType(String paymentType) {
    this.paymentType = paymentType;
  }

  public String getCustomerComment() {
    return customerComment;
  }

  public void setCustomerComment(String customerComment) {
    this.customerComment = customerComment;
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
}
