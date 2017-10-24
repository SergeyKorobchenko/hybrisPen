package com.bridgex.core.search.solrfacetsearch.provider.impl;

import com.bridgex.core.model.ApparelSizeVariantProductModel;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/23/2017.
 */
public class ClearanceValueResolver extends AbstractValueResolver<ProductModel, Object, Boolean>{
  @Override
  protected void addFieldValues(InputDocument inputDocument,
                                IndexerBatchContext indexerBatchContext,
                                IndexedProperty indexedProperty,
                                ProductModel productModel,
                                ValueResolverContext<Object, Boolean> valueResolverContext) throws FieldValueProviderException
  {
   ProductModel product = getBaseProductModel(productModel);
    if(product.isClearance()){
      inputDocument.addField(indexedProperty, Boolean.TRUE, valueResolverContext.getFieldQualifier());
    }
  }

  protected ProductModel getBaseProductModel(final ProductModel model)
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
