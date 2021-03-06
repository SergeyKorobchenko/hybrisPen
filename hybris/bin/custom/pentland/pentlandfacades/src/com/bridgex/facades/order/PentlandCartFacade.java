package com.bridgex.facades.order;

import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.product.data.ProductData;

/**
 * Created by dmitry.konovalov@masterdata.ru on 27.10.2017.
 */
public interface PentlandCartFacade extends CartFacade {

  void saveB2BCartData(CartData cartData);

  void populateVariantMatrixQuantity(ProductData productData);

  void populateCart();

}
