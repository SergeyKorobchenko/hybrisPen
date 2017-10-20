package com.bridgex.core.search.solrfacetsearch.indexer.impl;

import java.util.Set;

import org.apache.solr.common.SolrInputDocument;

import com.bridgex.core.search.solrfacetsearch.indexer.spi.impl.PentlandDefaultSolrInputDocument;
import com.bridgex.core.search.solrfacetsearch.indexer.spi.impl.PentlandSolrPartialUpdateInputDocument;

import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.impl.DefaultSolrDocumentFactory;
import de.hybris.platform.solrfacetsearch.indexer.impl.DefaultSolrInputDocument;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/19/2017.
 */
public class PentlandSolrDocumentFactory extends DefaultSolrDocumentFactory{

  @Override
  protected DefaultSolrInputDocument createWrappedDocument(IndexerBatchContext batchContext, SolrInputDocument delegate) {
    return new PentlandDefaultSolrInputDocument(delegate, batchContext, getFieldNameProvider(), getRangeNameProvider());
  }

  @Override
  protected DefaultSolrInputDocument createWrappedDocumentForPartialUpdates(IndexerBatchContext batchContext, SolrInputDocument delegate, Set<String> indexedPropertiesFields) {
    return new PentlandSolrPartialUpdateInputDocument(delegate, batchContext, getFieldNameProvider(), getRangeNameProvider(), indexedPropertiesFields);
  }
}
