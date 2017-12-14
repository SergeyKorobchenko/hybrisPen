package com.bridgex.core.search.solrfacetsearch.populator;

import java.util.ArrayList;
import java.util.List;

import de.hybris.platform.commerceservices.search.solrfacetsearch.data.DocumentData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.populators.SearchResponseResultsPopulator;
import de.hybris.platform.solrfacetsearch.search.Document;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchResultGroup;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 12/14/2017.
 */
public class PentlandSearchResponseResultsPopulator extends SearchResponseResultsPopulator {

  @Override
  protected Object convertGroupResultDocuments(final SearchQuery searchQuery, final SearchResultGroup group)
  {
    final DocumentData<SearchQuery, Document> documentData = createDocumentData();
    documentData.setSearchQuery(searchQuery);
    int documentIndex = 0;
    final List<Document> variants = new ArrayList<>();

    for (final Document document : group.getDocuments())
    {
      if (documentIndex == 0)
      {
        documentData.setDocument(document);
      }

      variants.add(document);

      documentIndex++;
    }

    documentData.setVariants(variants);
    return getSearchResultConverter().convert(documentData);
  }
}
