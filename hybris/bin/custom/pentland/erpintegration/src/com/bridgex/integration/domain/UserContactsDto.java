package com.bridgex.integration.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/17/2017.
 */
public class UserContactsDto {

  public UserContactsDto(){
  }

  @JsonProperty("EMAIL")
  private String email = "";

  @JsonProperty("I_SHIP_TO_ADDRESS")
  private ShipToDto shipToAddress;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public ShipToDto getShipToAddress() {
    return shipToAddress;
  }

  public void setShipToAddress(ShipToDto shipToAddress) {
    this.shipToAddress = shipToAddress;
  }
}
