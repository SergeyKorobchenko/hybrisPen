package com.bridgex.integration.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Created by konstantin.pavlyukov on 10/29/2017.
 */
public class MultiBrandCartInput {

  //Not used
  @JsonProperty("REFERENCE_DOCUMENT_ITEM")
  private String refDocNumber = "";
  //Not used
  @JsonProperty("REQUIREMENT_CATEGORY")
  private String reqCategory = "";

  @JsonProperty("SALES_ORG")
  private String salesOrg = "";

  @JsonProperty("DISTRIBUTION_CHANNEL")
  private String distrChannel = "";

  @JsonProperty("BRAND")
  private String brandCode = "";

  @JsonProperty("MATERIAL_NUMBER")
  private String materialNumber = "";

  //Not used
  @JsonProperty("REQUESTED_DATE")
  private String requestedDate = "";

  @JsonProperty("IT_SIZE_DATA")
  private List<SizeDataDto> sizeData;

  //Not used
  @JsonProperty("SEASON")
  private String season = "";

  public String getMaterialNumber() {
    return materialNumber;
  }

  public void setMaterialNumber(String materialNumber) {
    this.materialNumber = materialNumber;
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

  public String getRefDocNumber() {
    return refDocNumber;
  }

  public void setRefDocNumber(String refDocNumber) {
    this.refDocNumber = refDocNumber;
  }

  public String getReqCategory() {
    return reqCategory;
  }

  public void setReqCategory(String reqCategory) {
    this.reqCategory = reqCategory;
  }

  public String getRequestedDate() {
    return requestedDate;
  }

  public void setRequestedDate(String requestedDate) {
    this.requestedDate = requestedDate;
  }

  public String getSeason() {
    return season;
  }

  public void setSeason(String season) {
    this.season = season;
  }
}