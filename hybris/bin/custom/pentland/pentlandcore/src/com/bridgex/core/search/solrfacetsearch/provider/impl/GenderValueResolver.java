package com.bridgex.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.core.enums.Gender;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/24/2017.
 */
public class GenderValueResolver extends AbstractBaseProductValueResolver{
  @Override
  protected void addFieldValues(InputDocument inputDocument,
                                IndexerBatchContext indexerBatchContext,
                                IndexedProperty indexedProperty,
                                ProductModel productModel,
                                ValueResolverContext<Object, Object> valueResolverContext) throws FieldValueProviderException {

    ProductModel baseProductModel = this.getBaseProductModel(productModel);
    Gender gender = baseProductModel.getGender();
    if(gender != null) {
      inputDocument.addField(indexedProperty, gender.getCode(), valueResolverContext.getFieldQualifier());
    }

  }
}
