package com.bridgex.integration.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Goncharenko Mikhail, created on 01.11.2017.
 */
public class ShipmentDto {
  public ShipmentDto() {
  }

  @JsonProperty("E_SHIP_DATE")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private Date shipDate;

  @JsonProperty("E_SHIP_AMOUNT")
  private String shipQty;

  @JsonProperty("E_SHIP_STATUS")
  private String shipStatus;

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
