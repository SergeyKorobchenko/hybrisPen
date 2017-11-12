package com.bridgex.facades.product;

import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.data.ProductData;

/**
 * @author Created by konstantin.pavlyukov on 11/8/2017.
 */
public interface PentlandProductFacade extends ProductFacade {

  boolean populateCustomerPrice(ProductData productData);

}
