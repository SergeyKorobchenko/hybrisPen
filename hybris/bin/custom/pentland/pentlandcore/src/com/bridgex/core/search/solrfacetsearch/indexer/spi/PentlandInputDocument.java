package com.bridgex.core.search.solrfacetsearch.indexer.spi;

import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/19/2017.
 */
public interface PentlandInputDocument extends InputDocument{

  /**
   * Resolve value for ranged properties using separate qualifier
   * @param indexedProperty
   * @param value
   * @param valueQualifier
   * @param rangeQualifier
   * @throws FieldValueProviderException
   */
  void addField(IndexedProperty indexedProperty, Object value, String valueQualifier, String rangeQualifier) throws FieldValueProviderException;
}
