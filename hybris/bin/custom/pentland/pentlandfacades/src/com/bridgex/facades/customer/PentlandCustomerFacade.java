package com.bridgex.facades.customer;

import java.util.List;

import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/24/2017.
 */
public interface PentlandCustomerFacade extends CustomerFacade{

  void setCustomerHidePricesOption(boolean hidePrices);

  boolean hasMarkFors();
  
  public List<AddressData> getDeliveryAddressesForCustomer();
}
