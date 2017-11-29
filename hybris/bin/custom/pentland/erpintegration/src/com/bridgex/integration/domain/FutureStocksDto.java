package com.bridgex.integration.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Created by konstantin.pavlyukov on 10/29/2017.
 */
public class FutureStocksDto {

  @JsonProperty("NEXT_AVAILABLE_DATE")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
  private Date futureDate;

  @JsonProperty("NEXT_AVAILABLE_QUANTITY")
  private String futureQty;

  @JsonProperty("NEXT_AVAIL_CUMULATIVE_QTY")
  private String futureCumQty;

  public Date getFutureDate() {
    return futureDate;
  }

  public void setFutureDate(Date futureDate) {
    this.futureDate = futureDate;
  }

  public String getFutureQty() {
    return futureQty;
  }

  public void setFutureQty(String futureQty) {
    this.futureQty = futureQty;
  }

  public String getFutureCumQty() {
    return futureCumQty;
  }

  public void setFutureCumQty(String futureCumQty) {
    this.futureCumQty = futureCumQty;
  }
}
