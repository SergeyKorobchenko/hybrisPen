package com.bridgex.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;

/**
 * @author Goncharenko Mikhail, created on 22.11.2017.
 */
public class OrderConfirmationRepEmailContext extends OrderNotificationEmailContext {

  public static final String SALES_REP_FLAG = "salesRep";

  @Override
  public void init(OrderProcessModel orderProcessModel, EmailPageModel emailPageModel) {
    super.init(orderProcessModel, emailPageModel);
    put(SALES_REP_FLAG, Boolean.TRUE);
  }

}
