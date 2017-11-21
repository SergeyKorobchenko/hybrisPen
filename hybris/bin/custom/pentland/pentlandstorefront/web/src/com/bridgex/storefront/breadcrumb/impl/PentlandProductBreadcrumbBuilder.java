package com.bridgex.storefront.breadcrumb.impl;

import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.impl.ProductBreadcrumbBuilder;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.variants.model.VariantProductModel;

/**
 * @author Created by konstantin.pavlyukov on 11/20/2017.
 */
public class PentlandProductBreadcrumbBuilder extends ProductBreadcrumbBuilder {

  @Override
  protected ProductModel getBaseProduct(final ProductModel product)
  {
    if (product instanceof VariantProductModel)
    {
      return ((VariantProductModel) product).getBaseProduct();
    }
    return product;
  }
}
