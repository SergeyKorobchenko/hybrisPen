package com.bridgex.facades.customer;

import de.hybris.platform.commercefacades.customer.CustomerFacade;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/24/2017.
 */
public interface PentlandCustomerFacade extends CustomerFacade{

  void setCustomerHidePricesOption(boolean hidePrices);
}
