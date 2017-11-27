package com.bridgex.integration.domain;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/7/2017.
 */
public class SapOrderDto implements Serializable {

  public SapOrderDto(){
  }

  @JsonProperty("VBELN")
  private String orderCode;

  @JsonProperty("BRAND")
  private String sapBrand;

  @JsonProperty("STATUS")
  private String status;

  @JsonProperty("DOC_TYPE")
  private String orderType;

  @JsonProperty("TOTAL_NET_VALUE")
  private String totalPrice;

  @JsonProperty("TOTAL_QUANTITY")
  private String totalQty;

  @JsonProperty("BSTKD")
  private String poNumber;

  @JsonProperty("REQ_DATE_H")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
  private Date requestedDeliveryDate;

  @JsonProperty("MSG")
  private String sapMessage;

  public String getOrderCode() {
    return orderCode;
  }

  public void setOrderCode(String orderCode) {
    this.orderCode = orderCode;
  }

  public String getSapBrand() {
    return sapBrand;
  }

  public void setSapBrand(String sapBrand) {
    this.sapBrand = sapBrand;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getOrderType() {
    return orderType;
  }

  public void setOrderType(String orderType) {
    this.orderType = orderType;
  }

  public String getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(String totalPrice) {
    this.totalPrice = totalPrice;
  }

  public String getTotalQty() {
    return totalQty;
  }

  public void setTotalQty(String totalQty) {
    this.totalQty = totalQty;
  }

  public String getPoNumber() {
    return poNumber;
  }

  public void setPoNumber(String poNumber) {
    this.poNumber = poNumber;
  }

  public Date getRequestedDeliveryDate() {
    return requestedDeliveryDate;
  }

  public void setRequestedDeliveryDate(Date requestedDeliveryDate) {
    this.requestedDeliveryDate = requestedDeliveryDate;
  }

  public String getSapMessage() {
    return sapMessage;
  }

  public void setSapMessage(String sapMessage) {
    this.sapMessage = sapMessage;
  }
}
