package com.bridgex.core.order;

import de.hybris.platform.commerceservices.order.CommerceCheckoutService;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/31/2017.
 */
public interface PentlandCommerceCheckoutService extends CommerceCheckoutService{

  boolean setMarkForAddress(final CommerceCheckoutParameter parameter);
}
