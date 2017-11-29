package com.bridgex.facades.order;

import java.util.List;

import de.hybris.platform.b2bacceleratorfacades.api.cart.CheckoutFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.user.data.AddressData;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/31/2017.
 */
public interface PentlandAcceleratorCheckoutFacade extends CheckoutFacade {

  List<AddressData> findMarkForAddressesForShippingAddress(String addressId);

  boolean setMarkForAddressForCart(String addressId);

  boolean removeMarkForAddressFromCart();

  void cleanupZeroQuantityEntries();
}
