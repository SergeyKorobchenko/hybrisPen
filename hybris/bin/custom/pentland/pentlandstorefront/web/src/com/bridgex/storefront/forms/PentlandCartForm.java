package com.bridgex.storefront.forms;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.format.annotation.DateTimeFormat;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;

/**
 * Created by dmitry.konovalov@masterdata.ru on 26.10.2017.
 */
public class PentlandCartForm {

  private String purchaseOrderNumber;
  private String customerNotes;
  private String minDate = LocalDate.now().plusDays(2).format(DateTimeFormatter.ISO_LOCAL_DATE);
  private String bankHolidays;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date requestedDeliveryDate;

  private List<OrderEntryData> cartEntries = new ArrayList<>();

  public PentlandCartForm() {}

  public PentlandCartForm(CartData cartData) {
    purchaseOrderNumber = cartData.getPurchaseOrderNumber();
    requestedDeliveryDate = cartData.getRdd();
    customerNotes = cartData.getCustomerNotes();
  }

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

  public String getMinDate() {
    return minDate;
  }

  public String getBankHolidays() {
    return bankHolidays;
  }

  public void setBankHolidays(String bankHolidays) {
    this.bankHolidays = bankHolidays;
  }

  public List<OrderEntryData> getCartEntries() {
    return cartEntries;
  }

  public void setCartEntries(List<OrderEntryData> cartEntries) {
    this.cartEntries = cartEntries;
  }

}
