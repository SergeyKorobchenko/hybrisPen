package com.bridgex.core.search.solrfacetsearch.indexer.spi.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.solr.common.SolrInputDocument;

import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.RangeNameProvider;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/19/2017.
 */
public class PentlandSolrPartialUpdateInputDocument extends PentlandDefaultSolrInputDocument{

  private final Set<String> indexedFields;
  private      Set<String> notUpdatedIndexedFields;

  public PentlandSolrPartialUpdateInputDocument(SolrInputDocument delegate, IndexerBatchContext batchContext, FieldNameProvider fieldNameProvider, RangeNameProvider rangeNameProvider, Set<String> indexedFields) {
    super(delegate, batchContext, fieldNameProvider, rangeNameProvider);
    this.indexedFields = indexedFields;
  }

  public Set<String> getIndexedFields() {
    return this.indexedFields;
  }

  public Set<String> getNotUpdatedIndexedFields() {
    return this.notUpdatedIndexedFields;
  }

  public void addField(String fieldName, Object value) throws FieldValueProviderException {
    this.notUpdatedIndexedFields.remove(fieldName);
    HashMap<String, Object> fieldModifier = new HashMap<>(1);
    fieldModifier.put("set", value);
    super.addField(fieldName, fieldModifier);
  }

  protected void startDocument() {
    this.notUpdatedIndexedFields = new HashSet<>(this.indexedFields);
  }

  protected void endDocument() {
    Iterator var2 = this.notUpdatedIndexedFields.iterator();

    while(var2.hasNext()) {
      String fieldName = (String)var2.next();
      HashMap<String, Object> fieldModifier = new HashMap<>(1);
      fieldModifier.put("set", null);
      this.getDelegate().addField(fieldName, fieldModifier);
    }

  }

}
