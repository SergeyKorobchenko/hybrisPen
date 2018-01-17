package com.bridgex.integration.domain;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Goncharenko Mikhail, created on 01.11.2017.
 */
public class OrderDetailsResponse {

  @JsonProperty("E_SDOC_NUMBER")
  private String code;

  @JsonProperty("E_PO_NUMBER")
  private String purshaseOrderNumber;

  @JsonProperty("E_SDOC_STATUS")
  private String status;

  @JsonProperty("E_CREATION_DATE")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
  private Date creationTime;

  @JsonProperty("E_RDD")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
  private Date rdd;

  @JsonProperty("E_SDOC_NET_VALUE")
  private String netPrice;

  @JsonProperty("E_SDOC_TAX_AMOUNT")
  private String taxPrice;

  @JsonProperty("E_SDOC_TOT_PRICE")
  private String totalPrice;

  @JsonProperty("E_SDOC_CURRENCY")
  private String currency;

  @JsonProperty("E_CUSTOMER_NAME")
  private String customerName;

  @JsonProperty("E_CUSTOMER_ID")
  private String customerId;

  @JsonProperty("E_SHIP_MARK_FOR_NAME")
  private String deliveryAddressMarkForName;

  @JsonProperty("E_SHIP_MARK_FOR_ID")
  private String deliveryAddressMarkForId;

  @JsonProperty("E_SHIP_TO_CODE")
  private String deliveryAddressId;

  @JsonProperty("E_SHIP_TO_STREET")
  private String deliveryAddressStreet;

  @JsonProperty("E_SHIP_TO_CITY")
  private String deliveryAddressCity;

  @JsonProperty("E_SHIP_TO_STATE")
  private String deliveryAddressState;

  @JsonProperty("E_SHIP_TO_COUNTRY")
  private String deliveryAddressCountry;

  @JsonProperty("E_SHIP_TO_POSTAL_CODE")
  private String deliveryAddressPostcode;

  @JsonProperty("E_SDOC_QUANTITY")
  private String totalQuantity;

  @JsonProperty("ET_ORDER_ITEMS")
  private List<OrderEntryDto> orderEntries;

  @JsonProperty("ET_RETURN")
  private List<ETReturnDto> etReturn;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getPurshaseOrderNumber() {
    return purshaseOrderNumber;
  }

  public void setPurshaseOrderNumber(String purshaseOrderNumber) {
    this.purshaseOrderNumber = purshaseOrderNumber;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Date getCreationTime() {
    return creationTime;
  }

  public void setCreationTime(Date creationTime) {
    this.creationTime = creationTime;
  }

  public Date getRdd() {
    return rdd;
  }

  public void setRdd(Date rdd) {
    this.rdd = rdd;
  }

  public String getNetPrice() {
    return netPrice;
  }

  public void setNetPrice(String netPrice) {
    this.netPrice = netPrice;
  }

  public String getTaxPrice() {
    return taxPrice;
  }

  public void setTaxPrice(String taxPrice) {
    this.taxPrice = taxPrice;
  }

  public String getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(String totalPrice) {
    this.totalPrice = totalPrice;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public String getDeliveryAddressMarkForName() {
    return deliveryAddressMarkForName;
  }

  public void setDeliveryAddressMarkForName(String deliveryAddressMarkForName) {
    this.deliveryAddressMarkForName = deliveryAddressMarkForName;
  }

  public String getDeliveryAddressMarkForId() {
    return deliveryAddressMarkForId;
  }

  public void setDeliveryAddressMarkForId(String deliveryAddressMarkForId) {
    this.deliveryAddressMarkForId = deliveryAddressMarkForId;
  }

  public String getDeliveryAddressStreet() {
    return deliveryAddressStreet;
  }

  public void setDeliveryAddressStreet(String deliveryAddressStreet) {
    this.deliveryAddressStreet = deliveryAddressStreet;
  }

  public String getDeliveryAddressCity() {
    return deliveryAddressCity;
  }

  public void setDeliveryAddressCity(String deliveryAddressCity) {
    this.deliveryAddressCity = deliveryAddressCity;
  }

  public String getDeliveryAddressState() {
    return deliveryAddressState;
  }

  public void setDeliveryAddressState(String deliveryAddressState) {
    this.deliveryAddressState = deliveryAddressState;
  }

  public String getDeliveryAddressCountry() {
    return deliveryAddressCountry;
  }

  public void setDeliveryAddressCountry(String deliveryAddressCountry) {
    this.deliveryAddressCountry = deliveryAddressCountry;
  }

  public String getDeliveryAddressPostcode() {
    return deliveryAddressPostcode;
  }

  public void setDeliveryAddressPostcode(String deliveryAddressPostcode) {
    this.deliveryAddressPostcode = deliveryAddressPostcode;
  }

  public String getDeliveryAddressId() {
    return deliveryAddressId;
  }

  public void setDeliveryAddressId(String deliveryAddressId) {
    this.deliveryAddressId = deliveryAddressId;
  }

  public String getTotalQuantity() {
    return totalQuantity;
  }

  public void setTotalQuantity(String totalQuantity) {
    this.totalQuantity = totalQuantity;
  }

  public List<OrderEntryDto> getOrderEntries() {
    return orderEntries;
  }

  public void setOrderEntries(List<OrderEntryDto> orderEntries) {
    this.orderEntries = orderEntries;
  }

  public List<ETReturnDto> getEtReturn() {
    return etReturn;
  }

  public void setEtReturn(List<ETReturnDto> etReturn) {
      this.etReturn = etReturn;
  }
}
