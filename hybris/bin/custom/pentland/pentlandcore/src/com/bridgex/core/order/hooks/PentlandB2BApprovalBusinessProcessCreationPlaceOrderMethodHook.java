package com.bridgex.core.order.hooks;

import de.hybris.platform.b2b.order.hooks.B2BApprovalBusinessProcessCreationPlaceOrderMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/3/2017.
 */
public class PentlandB2BApprovalBusinessProcessCreationPlaceOrderMethodHook extends B2BApprovalBusinessProcessCreationPlaceOrderMethodHook {

  @Override
  public void afterPlaceOrder(CommerceCheckoutParameter commerceCheckoutParameter, CommerceOrderResult commerceOrderResult) {

    //do nothing for now, we don't use approval process for orders
    return;
  }
}
