package com.bridgex.core.product.url.impl;

import de.hybris.platform.commerceservices.url.impl.DefaultProductModelUrlResolver;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.variants.model.VariantProductModel;

/**
 * @author Created by konstantin.pavlyukov@masterdata.ru on 11/20/2017.
 */
public class DefaultPentlandProductModelUrlResolver extends DefaultProductModelUrlResolver {

  protected ProductModel getBaseProduct(final ProductModel product)
  {
    if (product instanceof VariantProductModel)
    {
      return ((VariantProductModel) product).getBaseProduct();
    }
    return product;
  }
}
