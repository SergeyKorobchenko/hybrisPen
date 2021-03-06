package com.bridgex.integration.domain;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/17/2017.
 */
public class SchedLinesDto {

  public SchedLinesDto(){
  }

  @JsonProperty("REQUESTED_DELIVERY_DATE")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
  private Date rdd;

  @JsonProperty("EAN")
  private String ean = "";

  @JsonProperty("SIZE")
  private String size = "";

  @JsonProperty("REQUESTED_QUANTITY")
  private String quantity = "";

  //not used
  @JsonProperty("REFERENCE_DOCUMENT")
  private String referenceDocument = "";

  public Date getRdd() {
    return rdd;
  }

  public void setRdd(Date rdd) {
    this.rdd = rdd;
  }

  public String getEan() {
    return ean;
  }

  public void setEan(String ean) {
    this.ean = ean;
  }

  public String getQuantity() {
    return quantity;
  }

  public void setQuantity(String quantity) {
    this.quantity = quantity;
  }

  public String getSize() {
    return size;
  }

  public void setSize(String size) {
    if(StringUtils.isNotEmpty(size)) {
      this.size = size;
    }
  }

  public String getReferenceDocument() {
    return referenceDocument;
  }

  public void setReferenceDocument(String referenceDocument) {
    this.referenceDocument = referenceDocument;
  }
}
