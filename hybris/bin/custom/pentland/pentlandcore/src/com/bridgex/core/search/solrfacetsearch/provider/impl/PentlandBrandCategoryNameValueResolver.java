package com.bridgex.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/18/2017.
 */
public class PentlandBrandCategoryNameValueResolver extends AbstractBrandCategoryResolver {

  @Override
  protected void addFieldValues(InputDocument inputDocument,
                                IndexerBatchContext indexerBatchContext,
                                IndexedProperty indexedProperty,
                                ProductModel productModel,
                                ValueResolverContext<Object, Object> valueResolverContext) throws FieldValueProviderException
  {
      CategoryModel brandCategory = this.getBrandCategoryForProduct(productModel);
      if(brandCategory != null){
        inputDocument.addField(indexedProperty, brandCategory.getName(), valueResolverContext.getFieldQualifier());
      }
  }

}
