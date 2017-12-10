package com.bridgex.integration.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/17/2017.
 */
public class MultiBrandOrderInput {

  @JsonProperty("SALES_ORG")
  private String salesOrg = "";

  @JsonProperty("DISTRIBUTION_CHANNEL")
  private String distrChannel = "";

  @JsonProperty("BRAND")
  private String brandCode = "";

  @JsonProperty("MATERIAL_NUMBER")
  private String materialNumber = "";

  @JsonProperty("SALES_UNIT")
  private String salesUnit = "";

  //not used
  @JsonProperty("CANCEL_DATE")
//  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
  private String cancelDate = "00000000";

  //not used
  @JsonProperty("REQUESTED_DELIVERY_DATE")
//  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
  private String rdd = "00000000";

  //not used
  @JsonProperty("DIM1")
  private String dim1 = "";

  //not used
  @JsonProperty("REFERENCE_DOCUMENT")
  private String referenceDocument = "";

  //not used
  @JsonProperty("REFERENCE_DOCUMENT_ITEM")
  private String referenceDocumentItem = "000000";

  //not used
  @JsonProperty("DLV_GROUP")
  private String dlvGroup = "000";

  @JsonProperty("IT_SCHED_LINES")
  private List<SchedLinesDto> schedLines;

  public String getMaterialNumber() {
    return materialNumber;
  }

  public void setMaterialNumber(String materialNumber) {
    if(materialNumber != null) {
      this.materialNumber = materialNumber;
    }
  }

  public String getSalesOrg() {
    return salesOrg;
  }

  public void setSalesOrg(String salesOrg) {
    if(salesOrg != null) {
      this.salesOrg = salesOrg;
    }
  }

  public String getDistrChannel() {
    return distrChannel;
  }

  public void setDistrChannel(String distrChannel) {
    if(distrChannel != null) {
      this.distrChannel = distrChannel;
    }
  }

  public String getBrandCode() {
    return brandCode;
  }

  public void setBrandCode(String brandCode) {
    if(brandCode != null) {
      this.brandCode = brandCode;
    }
  }

  public String getSalesUnit() {
    return salesUnit;
  }

  public void setSalesUnit(String salesUnit) {
    if(salesUnit != null) {
      this.salesUnit = salesUnit;
    }
  }

  public String getRdd() {
    return rdd;
  }

  public void setRdd(String rdd) {
    if(rdd != null) {
      this.rdd = rdd;
    }
  }

  public String getCancelDate() {
    return cancelDate;
  }

  public void setCancelDate(String cancelDate) {
    if(cancelDate != null) {
      this.cancelDate = cancelDate;
    }
  }

  public List<SchedLinesDto> getSchedLines() {
    return schedLines;
  }

  public void setSchedLines(List<SchedLinesDto> schedLines) {
    this.schedLines = schedLines;
  }

  public String getDim1() {
    return dim1;
  }

  public void setDim1(String dim1) {
    this.dim1 = dim1;
  }

  public String getReferenceDocument() {
    return referenceDocument;
  }

  public void setReferenceDocument(String referenceDocument) {
    this.referenceDocument = referenceDocument;
  }

  public String getReferenceDocumentItem() {
    return referenceDocumentItem;
  }

  public void setReferenceDocumentItem(String referenceDocumentItem) {
    this.referenceDocumentItem = referenceDocumentItem;
  }

  public String getDlvGroup() {
    return dlvGroup;
  }

  public void setDlvGroup(String dlvGroup) {
    this.dlvGroup = dlvGroup;
  }
}