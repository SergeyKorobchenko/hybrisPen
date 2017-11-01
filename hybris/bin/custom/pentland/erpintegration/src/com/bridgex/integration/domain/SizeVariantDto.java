package com.bridgex.integration.domain;

import java.util.List;

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

  @JsonProperty("TOTAL_QUANTITY")
  private String totalQuantity;

  @JsonProperty("ET_SHIPMENTS")
  private List<ShipmentDto> shipments;

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

  public List<ShipmentDto> getShipments() {
    return shipments;
  }

  public void setShipments(List<ShipmentDto> shipments) {
    this.shipments = shipments;
  }
}
