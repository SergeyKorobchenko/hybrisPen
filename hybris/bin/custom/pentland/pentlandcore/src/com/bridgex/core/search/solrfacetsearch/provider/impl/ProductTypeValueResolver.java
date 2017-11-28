package com.bridgex.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/23/2017.
 */
public class ProductTypeValueResolver extends AbstractProductTypeResolver{

  protected void addFieldValue(InputDocument inputDocument, IndexedProperty indexedProperty, ValueResolverContext<Object, Object> valueResolverContext, CategoryModel cat)
    throws FieldValueProviderException {

    inputDocument.addField(indexedProperty, cat.getCode(), valueResolverContext.getFieldQualifier());
  }

}
