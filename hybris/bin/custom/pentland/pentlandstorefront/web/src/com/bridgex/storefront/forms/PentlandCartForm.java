package com.bridgex.storefront.forms;

import java.util.Date;

/**
 * Created by dmitry.konovalov@masterdata.ru on 26.10.2017.
 */
public class PentlandCartForm {

  private String purchaseOrderNumber;
  private String customerNotes;
  private Date requestedDeliveryDate;



  public String getPurchaseOrderNumber() {
    return purchaseOrderNumber;
  }

  public void setPurchaseOrderNumber(String purchaseOrderNumber) {
    this.purchaseOrderNumber = purchaseOrderNumber;
  }

  public String getCustomerNotes() {
    return customerNotes;
  }

  public void setCustomerNotes(String customerNotes) {
    this.customerNotes = customerNotes;
  }

  public Date getRequestedDeliveryDate() {
    return requestedDeliveryDate;
  }

  public void setRequestedDeliveryDate(Date requestedDeliveryDate) {
    this.requestedDeliveryDate = requestedDeliveryDate;
  }
}
