package com.bridgex.core.search.solrfacetsearch.provider.impl;

import com.bridgex.core.model.ApparelSizeVariantProductModel;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/24/2017.
 */
public abstract class AbstractBaseProductValueResolver extends AbstractValueResolver<ProductModel, Object, Object> {

  public ProductModel getBaseProductModel(final ProductModel model)
  {
    if (model instanceof ApparelSizeVariantProductModel) {
      //prices are stored at style level
      return ((ApparelSizeVariantProductModel) model).getBaseProduct();
    }else{
      //in case of non-variant or style-only products
      return model;
    }
  }
}
