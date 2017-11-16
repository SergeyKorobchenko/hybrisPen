package com.bridgex.integration.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Goncharenko Mikhail, created on 09.11.2017.
 */
public class AccountSummaryResponse {

  @JsonProperty("E_CUSTOMER_NUMBER")
  private String sapCustomerId;

  @JsonProperty("E_CUSTOMER_NAME")
  private String sapCustomerName;

  @JsonProperty("E_HOUSE_NUMBER")
  private String houseNumber;

  @JsonProperty("E_STREET")
  private String streetLine1;

  @JsonProperty("E_STREET2")
  private String streetLine2;

  @JsonProperty("E_STREET3")
  private String streetLine3;

  @JsonProperty("E_CITY")
  private String city;

  @JsonProperty("E_COUNTRY")
  private String country;

  @JsonProperty("E_REGION")
  private String region;

  @JsonProperty("E_POSTAL_CODE")
  private String postalCode;

  @JsonProperty("ET_DETAILS")
  private List<AccountSummaryDetailsDto> details;

  @JsonProperty("ET_RETURN")
  private ETReturnDto etReturn;

  public String getSapCustomerId() {
    return sapCustomerId;
  }

  public void setSapCustomerId(String sapCustomerId) {
    this.sapCustomerId = sapCustomerId;
  }

  public String getSapCustomerName() {
    return sapCustomerName;
  }

  public void setSapCustomerName(String sapCustomerName) {
    this.sapCustomerName = sapCustomerName;
  }

  public String getHouseNumber() {
    return houseNumber;
  }

  public void setHouseNumber(String houseNumber) {
    this.houseNumber = houseNumber;
  }

  public String getStreetLine1() {
    return streetLine1;
  }

  public void setStreetLine1(String streetLine1) {
    this.streetLine1 = streetLine1;
  }

  public String getStreetLine2() {
    return streetLine2;
  }

  public void setStreetLine2(String streetLine2) {
    this.streetLine2 = streetLine2;
  }

  public String getStreetLine3() {
    return streetLine3;
  }

  public void setStreetLine3(String streetLine3) {
    this.streetLine3 = streetLine3;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public List<AccountSummaryDetailsDto> getDetails() {
    return details;
  }

  public void setDetails(List<AccountSummaryDetailsDto> details) {
    this.details = details;
  }

  public ETReturnDto getEtReturn() {
    return etReturn;
  }

  public void setEtReturn(List<ETReturnDto> etReturn) {
    this.etReturn = etReturn.get(0);
  }
}
