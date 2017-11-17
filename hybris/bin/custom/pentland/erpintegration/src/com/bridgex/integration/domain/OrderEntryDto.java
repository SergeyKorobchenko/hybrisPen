package com.bridgex.integration.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JacksonAnnotation;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;

/**
 * @author Goncharenko Mikhail, created on 01.11.2017.
 */
public class OrderEntryDto {
  public OrderEntryDto() {
  }

  @JsonProperty("ITEM_NUMBER")
  private String entryNumber;

  @JsonProperty("MATERIAL_NUMBER")
  private String product;

  @JsonProperty("LINE_QUANTITY")
  private String quantity;

  @JsonProperty("BASE_PRICE")
  private String price;

  @JsonProperty("UNIT_NET_PRICE")
  private String unitNetPrice;

  @JsonProperty("NET_PRICE")
  private String netPrice;

  @JsonProperty("STATUS")
  private String entryStatus;

  @JsonProperty("UOM")
  private String unit;

  @JsonProperty("REQ_DEL_DATE")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
  private Date rdd;

  @JsonProperty("ZSCHEDULE")
  private List<SizeVariantDto> sizeVariants;

  public String getEntryNumber() {
    return entryNumber;
  }

  public void setEntryNumber(String entryNumber) {
    this.entryNumber = entryNumber;
  }

  public String getProduct() {
    return product;
  }

  public void setProduct(String product) {
    this.product = product;
  }

  public String getQuantity() {
    return quantity;
  }

  public void setQuantity(String quantity) {
    this.quantity = quantity;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public String getUnitNetPrice() {
    return unitNetPrice;
  }

  public void setUnitNetPrice(String unitNetPrice) {
    this.unitNetPrice = unitNetPrice;
  }

  public String getNetPrice() {
    return netPrice;
  }

  public void setNetPrice(String netPrice) {
    this.netPrice = netPrice;
  }

  public String getEntryStatus() {
    return entryStatus;
  }

  public void setEntryStatus(String entryStatus) {
    this.entryStatus = entryStatus;
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public Date getRdd() {
    return rdd;
  }

  public void setRdd(Date rdd) {
    this.rdd = rdd;
  }

  public List<SizeVariantDto> getSizeVariants() {
    return sizeVariants;
  }

  public void setSizeVariants(List<SizeVariantDto> sizeVariants) {
    this.sizeVariants = sizeVariants;
  }
}
