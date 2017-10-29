package com.bridgex.facades.order.impl;

import com.bridgex.facades.order.PentlandCartFacade;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.impl.DefaultCartFacade;
import de.hybris.platform.core.model.order.CartModel;

/**
 * Created by dmitry.konovalov@masterdata.ru on 30.10.2017.
 */
public class DefaultPentlandCartFacade extends DefaultCartFacade implements PentlandCartFacade {

  @Override
  public void saveB2BCartData(CartData cartData) {
    if (hasSessionCart()) {
      final CartModel cart = getCartService().getSessionCart();
      cart.setPurchaseOrderNumber(cartData.getPurchaseOrderNumber());
      cart.setRdd(cartData.getRdd());
      cart.setCustomerNotes(cartData.getCustomerNotes());
      getModelService().save(cart);
    }
  }

}
