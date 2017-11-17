package com.bridgex.integration.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Goncharenko Mikhail, created on 01.11.2017.
 */
public class SizeVariantDto {
  public SizeVariantDto() {
  }

  @JsonProperty("SCHEDULE_LINE")
  private String lineNumber;

  @JsonProperty("EAN")
  private String ean;

  @JsonProperty("QUANTITY")
  private String totalQuantity;

  @JsonProperty("SHIPPED_DATE")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
  private Date shipDate;

  @JsonProperty("SHIPPED_QUANTITY")
  private String shipQty;

  @JsonProperty("STATUS")
  private String shipStatus;

  public String getLineNumber() {
    return lineNumber;
  }

  public void setLineNumber(String lineNumber) {
    this.lineNumber = lineNumber;
  }

  public String getEan() {
    return ean;
  }

  public void setEan(String ean) {
    this.ean = ean;
  }

  public String getTotalQuantity() {
    return totalQuantity;
  }

  public void setTotalQuantity(String totalQuantity) {
    this.totalQuantity = totalQuantity;
  }

  public Date getShipDate() {
    return shipDate;
  }

  public void setShipDate(Date shipDate) {
    this.shipDate = shipDate;
  }

  public String getShipQty() {
    return shipQty;
  }

  public void setShipQty(String shipQty) {
    this.shipQty = shipQty;
  }

  public String getShipStatus() {
    return shipStatus;
  }

  public void setShipStatus(String shipStatus) {
    this.shipStatus = shipStatus;
  }
}
