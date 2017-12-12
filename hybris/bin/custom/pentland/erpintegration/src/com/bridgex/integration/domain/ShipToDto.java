package com.bridgex.integration.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/17/2017.
 */
public class ShipToDto {

  public ShipToDto(){
  }

  @JsonProperty("ID")
  private String id = "";

  @JsonProperty("NAME")
  private String name = "";

  @JsonProperty("STREET")
  private String street = "";

  @JsonProperty("CITY")
  private String city = "";

  @JsonProperty("REGION")
  private String region = "";

  @JsonProperty("COUNTRY")
  private String country = "";

  @JsonProperty("POSTL_CODE")
  private String postalCode = "";

  @JsonProperty("MARK_FOR")
  private String markForFlag = "";

  public String getId() {
    return id;
  }

  public void setId(String id) {
    if(id != null) {
      this.id = id;
    }
  }

  public String getMarkForFlag() {
    return markForFlag;
  }

  public void setMarkForFlag(String markForFlag) {
    if(markForFlag != null) {
      this.markForFlag = markForFlag;
    }
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    if(name != null) {
      this.name = name;
    }
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    if(street != null) {
      this.street = street;
    }
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    if(city != null) {
      this.city = city;
    }
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    if(region != null) {
      this.region = region;
    }
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    if(country != null) {
      this.country = country;
    }
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    if(postalCode != null) {
      this.postalCode = postalCode;
    }
  }
}
