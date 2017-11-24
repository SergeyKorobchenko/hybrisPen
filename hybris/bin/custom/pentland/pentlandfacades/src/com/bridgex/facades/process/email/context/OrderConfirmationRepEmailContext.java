package com.bridgex.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.b2b.jalo.B2BCustomer;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;

/**
 * @author Goncharenko Mikhail, created on 22.11.2017.
 */
public class OrderConfirmationRepEmailContext extends OrderNotificationEmailContext {

  public static final String SALES_REP_FLAG = "salesRep";
  public static final String ACCOUNT_NAME = "account";

  @Override
  public void init(OrderProcessModel orderProcessModel, EmailPageModel emailPageModel) {
    super.init(orderProcessModel, emailPageModel);
    put(SALES_REP_FLAG, Boolean.TRUE);
    putCustomerAccount(orderProcessModel);
  }

  private void putCustomerAccount(OrderProcessModel orderProcess) {
    B2BCustomerModel user = (B2BCustomerModel) (orderProcess.getOrder().getUser());
    user.getAllGroups().stream()
        .filter(B2BUnitModel.class::isInstance)
        .map(B2BUnitModel.class::cast)
        .map(B2BUnitModel::getName)
        .findFirst().ifPresent(name -> put(ACCOUNT_NAME, name));
  }

}
