package com.bridgex.integration.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Goncharenko Mikhail, created on 09.11.2017.
 */
public class AccountSummaryDetailsDto {

  @JsonProperty("BRAND")
  private String brand;

  @JsonProperty("CREDIT_REP")
  private String creditRep;

  @JsonProperty("CREDIT_LIMIT")
  private String creditLimit;

  @JsonProperty("CURRENT_BALANCE")
  private String currentBalance;

  @JsonProperty("OPEN_BALANCE")
  private String openBalance;

  @JsonProperty("1_TO_30_DAYS")
  private String days1to30;

  @JsonProperty("31_TO_60_DAYS")
  private String days31to60;

  @JsonProperty("61_TO_90_DAYS")
  private String days61to90;

  @JsonProperty("MORE_THAN_90_DAYS")
  private String daysOver90;

  @JsonProperty("PAST_DUE_BALANCE")
  private String pastDueBalance;

  @JsonProperty("CURRENCY")
  private String currency;

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public String getCreditRep() {
    return creditRep;
  }

  public void setCreditRep(String creditRep) {
    this.creditRep = creditRep;
  }

  public String getCreditLimit() {
    return creditLimit;
  }

  public void setCreditLimit(String creditLimit) {
    this.creditLimit = creditLimit;
  }

  public String getCurrentBalance() {
    return currentBalance;
  }

  public void setCurrentBalance(String currentBalance) {
    this.currentBalance = currentBalance;
  }

  public String getOpenBalance() {
    return openBalance;
  }

  public void setOpenBalance(String openBalance) {
    this.openBalance = openBalance;
  }

  public String getDays1to30() {
    return days1to30;
  }

  public void setDays1to30(String days1to30) {
    this.days1to30 = days1to30;
  }

  public String getDays31to60() {
    return days31to60;
  }

  public void setDays31to60(String days31to60) {
    this.days31to60 = days31to60;
  }

  public String getDays61to90() {
    return days61to90;
  }

  public void setDays61to90(String days61to90) {
    this.days61to90 = days61to90;
  }

  public String getDaysOver90() {
    return daysOver90;
  }

  public void setDaysOver90(String daysOver90) {
    this.daysOver90 = daysOver90;
  }

  public String getPastDueBalance() {
    return pastDueBalance;
  }

  public void setPastDueBalance(String pastDueBalance) {
    this.pastDueBalance = pastDueBalance;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }
}
