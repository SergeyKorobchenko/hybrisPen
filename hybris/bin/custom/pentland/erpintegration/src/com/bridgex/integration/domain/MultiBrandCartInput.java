package com.bridgex.integration.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Created by konstantin.pavlyukov on 10/29/2017.
 */
public class MultiBrandCartInput {

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
}