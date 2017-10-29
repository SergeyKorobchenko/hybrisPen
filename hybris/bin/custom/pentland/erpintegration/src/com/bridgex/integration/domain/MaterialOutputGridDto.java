package com.bridgex.integration.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Created by konstantin.pavlyukov on 10/29/2017.
 */
public class MaterialOutputGridDto {

  @JsonProperty("MATERIAL_NUMBER")
  private String materialNumber;

  @JsonProperty("EAN")
  private String ean;

  @JsonProperty("AVAILABLE_QUANTITY")
  private String availableQty;

  @JsonProperty("USER_REQUESTED_QTY")
  private String userRequestedQty;

  @JsonProperty("MESSAGE_ID")
  private String messageId;

  @JsonProperty("MESSAGE_DESCRIPTION")
  private String messageDescription;

  @JsonProperty("NEXT_AVAILABLE_DATES")
  private List<FutureStocksDto> futureStocksDtoList;

  public String getMaterialNumber() {
    return materialNumber;
  }

  public void setMaterialNumber(String materialNumber) {
    this.materialNumber = materialNumber;
  }

  public String getEan() {
    return ean;
  }

  public void setEan(String ean) {
    this.ean = ean;
  }

  public String getAvailableQty() {
    return availableQty;
  }

  public void setAvailableQty(String availableQty) {
    this.availableQty = availableQty;
  }

  public String getUserRequestedQty() {
    return userRequestedQty;
  }

  public void setUserRequestedQty(String userRequestedQty) {
    this.userRequestedQty = userRequestedQty;
  }

  public String getMessageId() {
    return messageId;
  }

  public void setMessageId(String messageId) {
    this.messageId = messageId;
  }

  public String getMessageDescription() {
    return messageDescription;
  }

  public void setMessageDescription(String messageDescription) {
    this.messageDescription = messageDescription;
  }

  public List<FutureStocksDto> getFutureStocksDtoList() {
    return futureStocksDtoList;
  }

  public void setFutureStocksDtoList(List<FutureStocksDto> futureStocksDtoList) {
    this.futureStocksDtoList = futureStocksDtoList;
  }
}
