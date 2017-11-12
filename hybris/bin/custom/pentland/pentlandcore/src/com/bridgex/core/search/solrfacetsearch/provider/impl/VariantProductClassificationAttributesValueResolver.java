package com.bridgex.core.search.solrfacetsearch.provider.impl;

import java.util.Collection;

import com.bridgex.core.model.ApparelSizeVariantProductModel;

import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.commerceservices.search.solrfacetsearch.provider.impl.ProductClassificationAttributesValueResolver;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/10/2017.
 */
public class VariantProductClassificationAttributesValueResolver extends ProductClassificationAttributesValueResolver {
  @Override
  protected FeatureList loadData(final IndexerBatchContext batchContext, final Collection<IndexedProperty> indexedProperties,
                                 final ProductModel product) {

    ProductModel baseProductModel = getBaseProductModel(product);
    return loadFeatures(indexedProperties, baseProductModel);
  }

  public ProductModel getBaseProductModel(final ProductModel model)
  {
    if (model instanceof ApparelSizeVariantProductModel) {
      //most attributes are stored at style level
      return ((ApparelSizeVariantProductModel) model).getBaseProduct();
    }else{
      //in case of non-variant or style-only products
      return model;
    }
  }
}
