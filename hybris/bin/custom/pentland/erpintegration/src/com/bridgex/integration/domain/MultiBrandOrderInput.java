package com.bridgex.integration.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/17/2017.
 */
public class MultiBrandOrderInput {

  @JsonProperty("MATERIAL_NUMBER")
  private String materialNumber;

  @JsonProperty("CUSTOMER_NUMBER")
  private String customerSapId;

  @JsonProperty("SALES_ORG")
  private String salesOrg;

  @JsonProperty("DISTRIBUTION_CHANNEL")
  private String distrChannel;

  @JsonProperty("PRICE_LIST")
  private String priceList;

  @JsonProperty("BRAND")
  private String brandCode;

  @JsonProperty("IT_SIZE_DATA")
  private List<SizeDataDto> sizeData;

  @JsonProperty("SALES_UNIT")
  private String salesUnit;

  //not used
  @JsonProperty("REQUESTED_DELIVERY_DATE")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private Date rdd;

  //not used
  @JsonProperty("CANCEL_DATE")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private Date cancelDate;

  @JsonProperty("IT_SCHED_LINES")
  private SchedLinesDto schedLines;

  public String getMaterialNumber() {
    return materialNumber;
  }

  public void setMaterialNumber(String materialNumber) {
    this.materialNumber = materialNumber;
  }

  public String getCustomerSapId() {
    return customerSapId;
  }

  public void setCustomerSapId(String customerSapId) {
    this.customerSapId = customerSapId;
  }

  public String getSalesOrg() {
    return salesOrg;
  }

  public void setSalesOrg(String salesOrg) {
    this.salesOrg = salesOrg;
  }

  public String getDistrChannel() {
    return distrChannel;
  }

  public void setDistrChannel(String distrChannel) {
    this.distrChannel = distrChannel;
  }

  public String getPriceList() {
    return priceList;
  }

  public void setPriceList(String priceList) {
    this.priceList = priceList;
  }

  public String getBrandCode() {
    return brandCode;
  }

  public void setBrandCode(String brandCode) {
    this.brandCode = brandCode;
  }

  public List<SizeDataDto> getSizeData() {
    return sizeData;
  }

  public void setSizeData(List<SizeDataDto> sizeData) {
    this.sizeData = sizeData;
  }

  public String getSalesUnit() {
    return salesUnit;
  }

  public void setSalesUnit(String salesUnit) {
    this.salesUnit = salesUnit;
  }

  public Date getRdd() {
    return rdd;
  }

  public void setRdd(Date rdd) {
    this.rdd = rdd;
  }

  public Date getCancelDate() {
    return cancelDate;
  }

  public void setCancelDate(Date cancelDate) {
    this.cancelDate = cancelDate;
  }

  public SchedLinesDto getSchedLines() {
    return schedLines;
  }

  public void setSchedLines(SchedLinesDto schedLines) {
    this.schedLines = schedLines;
  }
}