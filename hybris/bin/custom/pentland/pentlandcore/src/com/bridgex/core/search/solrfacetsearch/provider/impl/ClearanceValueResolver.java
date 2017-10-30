package com.bridgex.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/23/2017.
 */
public class ClearanceValueResolver extends AbstractBaseProductValueResolver{

  @Override
  protected void addFieldValues(InputDocument inputDocument,
                                IndexerBatchContext indexerBatchContext,
                                IndexedProperty indexedProperty,
                                ProductModel productModel,
                                ValueResolverContext<Object, Object> valueResolverContext) throws FieldValueProviderException
  {
   ProductModel product = getBaseProductModel(productModel);
    if(product.isClearance()){
      inputDocument.addField(indexedProperty, Boolean.TRUE, valueResolverContext.getFieldQualifier());
    }
  }
}
