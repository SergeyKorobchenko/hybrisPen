package com.bridgex.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/18/2017.
 */
public class PentlandBrandCategoryCodeValueResolver extends AbstractBrandCategoryResolver {

  @Override
  protected void addFieldValues(InputDocument inputDocument,
                                IndexerBatchContext indexerBatchContext,
                                IndexedProperty indexedProperty,
                                ProductModel productModel,
                                AbstractValueResolver.ValueResolverContext<Object, Object> valueResolverContext) throws FieldValueProviderException
  {
    CategoryModel brandCategory = this.getBrandCategoryForProduct(productModel);
    if (brandCategory != null) {
      inputDocument.addField(indexedProperty, brandCategory.getCode(), valueResolverContext.getFieldQualifier());
    }
  }
}
