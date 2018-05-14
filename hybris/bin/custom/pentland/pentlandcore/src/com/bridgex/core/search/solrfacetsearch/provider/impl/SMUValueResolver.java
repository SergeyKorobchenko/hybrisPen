package com.bridgex.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;


public class SMUValueResolver extends AbstractBaseProductValueResolver
{

	protected void addFieldValues(InputDocument inputDocument,
            IndexerBatchContext indexerBatchContext,
            IndexedProperty indexedProperty,
            ProductModel productModel,
            ValueResolverContext<Object, Object> valueResolverContext) throws FieldValueProviderException
	{
		 ProductModel product = getBaseProductModel(productModel);
		    if(product.isSmu()){
		      inputDocument.addField(indexedProperty, Boolean.TRUE, valueResolverContext.getFieldQualifier());
		
	}
		    else
		    {
		    	inputDocument.addField(indexedProperty, Boolean.FALSE, valueResolverContext.getFieldQualifier());
		    }

	}
}
