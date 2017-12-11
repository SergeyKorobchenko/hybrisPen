package com.bridgex.core.search.solrfacetsearch.populator;

import java.util.*;
import java.util.stream.IntStream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.constants.PentlandcoreConstants;

import de.hybris.platform.core.PK;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.europe1.enums.UserPriceGroup;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.search.OrderField;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.impl.SearchQueryConverterData;
import de.hybris.platform.solrfacetsearch.search.impl.populators.FacetSearchQuerySortsPopulator;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 12/8/2017.
 */
public class PentlandFacetSearchQuerySortsPopulator extends FacetSearchQuerySortsPopulator {

  private static final String PRICE_FIELD = "def(field(priceValue_%s_%s_double),";


  private SessionService sessionService;

  @Override
  public void populate(SearchQueryConverterData source, SolrQuery target) throws ConversionException {
    SearchQuery searchQuery = source.getSearchQuery();
    List<OrderField> sorts = searchQuery.getSorts();
    List<PK> promotedItems = searchQuery.getPromotedItems();
    if(CollectionUtils.isEmpty(sorts)) {
      if(CollectionUtils.isNotEmpty(promotedItems)) {
        String scoreSort = this.buildPromotedItemsSort(searchQuery);
        target.addSort(scoreSort, SolrQuery.ORDER.desc);
        target.addSort("score", SolrQuery.ORDER.desc);
      }
    } else {
      Optional scoreSort1 = sorts.stream().filter((sort) -> {
        return Objects.equals(sort.getField(), "score");
      }).findFirst();
      if(scoreSort1.isPresent() && CollectionUtils.isNotEmpty(promotedItems)) {
        String sort = this.buildPromotedItemsSort(searchQuery);
        target.addSort(sort, SolrQuery.ORDER.desc);
      }

      Set<UserPriceGroup> sessionUPGs = new LinkedHashSet<>();
      sessionUPGs.addAll(sessionService.getAttribute(Europe1Constants.PARAMS.UPG));
      sessionUPGs.addAll(sessionService.getAttribute(PentlandcoreConstants.PARENT_UPG));
      String currency = source.getSearchQuery().getCurrency().toLowerCase();

      sorts.forEach(sort ->{
        if("priceValue".equals(sort.getField())){
          //query price as function
          List<String> priceFields = new ArrayList<>();
          sessionUPGs.forEach(upg -> {
            if(upg != null){
              priceFields.add(String.format(PRICE_FIELD, upg.getCode().toLowerCase(), currency));
            }
          });
          StringBuilder defQuery = new StringBuilder();
          priceFields.forEach(defQuery::append);
          defQuery.append("0");
          IntStream.rangeClosed(1,priceFields.size()).forEach(i -> defQuery.append(")"));
          target.addSort(defQuery.toString(), sort.isAscending() ? SolrQuery.ORDER.asc : SolrQuery.ORDER.desc);
        }else{
          //OOTB
          String field = Objects.equals(sort.getField(), "score")?sort.getField():getFieldNameTranslator().translate(searchQuery, sort.getField(), FieldNameProvider.FieldType.SORT);
          target.addSort(field, sort.isAscending() ? SolrQuery.ORDER.asc : SolrQuery.ORDER.desc);
        }
      });

    }

  }

  @Required
  public void setSessionService(SessionService sessionService) {
    this.sessionService = sessionService;
  }
}
