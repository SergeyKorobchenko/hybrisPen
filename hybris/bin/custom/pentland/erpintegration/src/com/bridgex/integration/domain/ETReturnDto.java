package com.bridgex.integration.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Created by konstantin.pavlyukov on 10/26/2017.
 */
public class ETReturnDto {
  public ETReturnDto() {
  }

  public ETReturnDto(final String type) {
    this.type = type;
  }

  @JsonProperty("TYPE")
  private String type;

  @JsonProperty("ID")
  private String id;

  @JsonProperty("NUMBER")
  private String number;

  @JsonProperty("MESSAGE")
  private String message;

  @JsonProperty("LOG_NO")
  private String logNo;
  @JsonProperty("LOG_MSG_NO")
  private String logMsgNo;
  @JsonProperty("MESSAGE_V1")
  private String messageV1;
  @JsonProperty("MESSAGE_V2")
  private String messageV2;
  @JsonProperty("MESSAGE_V3")
  private String messageV3;
  @JsonProperty("MESSAGE_V4")
  private String messageV4;
  @JsonProperty("PARAMETER")
  private String parameter;
  @JsonProperty("ROW")
  private String row;
  @JsonProperty("FIELD")
  private String field;
  @JsonProperty("SYSTEM")
  private String system;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getLogNo() {
    return logNo;
  }

  public void setLogNo(String logNo) {
    this.logNo = logNo;
  }

  public String getLogMsgNo() {
    return logMsgNo;
  }

  public void setLogMsgNo(String logMsgNo) {
    this.logMsgNo = logMsgNo;
  }

  public String getMessageV1() {
    return messageV1;
  }

  public void setMessageV1(String messageV1) {
    this.messageV1 = messageV1;
  }

  public String getMessageV2() {
    return messageV2;
  }

  public void setMessageV2(String messageV2) {
    this.messageV2 = messageV2;
  }

  public String getMessageV3() {
    return messageV3;
  }

  public void setMessageV3(String messageV3) {
    this.messageV3 = messageV3;
  }

  public String getMessageV4() {
    return messageV4;
  }

  public void setMessageV4(String messageV4) {
    this.messageV4 = messageV4;
  }

  public String getParameter() {
    return parameter;
  }

  public void setParameter(String parameter) {
    this.parameter = parameter;
  }

  public String getRow() {
    return row;
  }

  public void setRow(String row) {
    this.row = row;
  }

  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  public String getSystem() {
    return system;
  }

  public void setSystem(String system) {
    this.system = system;
  }
}