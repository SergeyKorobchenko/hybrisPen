package com.bridgex.integration.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/17/2017.
 */
public class ShipToDto {

  public ShipToDto(){
  }

  @JsonProperty("ID")
  private String id;

  @JsonProperty("MARK_FOR_FLAG")
  private String markForFlag;

  @JsonProperty("NAME")
  private String name;

  @JsonProperty("STREET")
  private String street;

  @JsonProperty("CITY")
  private String city;

  @JsonProperty("REGION")
  private String region;

  @JsonProperty("COUNTRY")
  private String country;

  @JsonProperty("POSTL_CODE")
  private String postalCode;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getMarkForFlag() {
    return markForFlag;
  }

  public void setMarkForFlag(String markForFlag) {
    this.markForFlag = markForFlag;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }
}
