package com.bridgex.core.search.solrfacetsearch.indexer.spi.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.solr.common.SolrInputDocument;

import com.bridgex.core.search.solrfacetsearch.indexer.spi.PentlandInputDocument;

import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.impl.DefaultSolrInputDocument;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.RangeNameProvider;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/19/2017.
 */
public class PentlandDefaultSolrInputDocument extends DefaultSolrInputDocument implements PentlandInputDocument {

  public PentlandDefaultSolrInputDocument(SolrInputDocument delegate, IndexerBatchContext batchContext, FieldNameProvider fieldNameProvider, RangeNameProvider rangeNameProvider)
  {
    super(delegate, batchContext, fieldNameProvider, rangeNameProvider);
  }

  @Override
  public void addField(IndexedProperty indexedProperty, Object value, String valueQualifier, String rangeQualifier) throws FieldValueProviderException {
    Collection fieldNames = getFieldNameProvider().getFieldNames(indexedProperty, valueQualifier);
    List rangeNameList = getRangeNameProvider().getRangeNameList(indexedProperty, value, rangeQualifier);
    Iterator var7 = fieldNames.iterator();

    while(true) {
      while(var7.hasNext()) {
        String fieldName = (String)var7.next();
        if(rangeNameList.isEmpty()) {
          this.addField(fieldName, value);
        } else {
          Iterator var9 = rangeNameList.iterator();

          while(var9.hasNext()) {
            String rangeName = (String)var9.next();
            this.addField(fieldName, rangeName == null?value:rangeName);
          }
        }
      }

      return;
    }
  }
}
