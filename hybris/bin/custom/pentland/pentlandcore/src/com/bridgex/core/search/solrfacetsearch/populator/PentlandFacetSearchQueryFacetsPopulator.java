package com.bridgex.core.search.solrfacetsearch.populator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.solrfacetsearch.search.FacetField;
import de.hybris.platform.solrfacetsearch.search.FacetValueField;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.impl.populators.FacetSearchQueryFacetsPopulator;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/24/2017.
 */
public class PentlandFacetSearchQueryFacetsPopulator extends FacetSearchQueryFacetsPopulator {

  private UserService userService;

  private static final Logger LOG = Logger.getLogger(PentlandFacetSearchQueryFacetsPopulator.class);

  @Override
  protected Map<String, FacetInfo> prepareFacets(SearchQuery searchQuery) {
    HashMap<String, FacetInfo> facets = new HashMap<>();

    List<FacetField> facetList = searchQuery.getFacets();
    for(FacetField facet : facetList) {
      if("price".equals(facet.getField()) && isHidePrices()){
        continue;
      }
      FacetInfo facetInfo = new FacetInfo(facet.getField(), facet.getFacetType());
      facets.put(facet.getField(), facetInfo);
    }

    List<FacetValueField> facetValues = searchQuery.getFacetValues();
    for(FacetValueField facetValue: facetValues){
      if("price".equals(facetValue.getField()) && isHidePrices()){
        continue;
      }
      FacetInfo facetInfo = facets.get(facetValue.getField());
      if(facetInfo != null) {
        if(CollectionUtils.isNotEmpty(facetInfo.getValues())) {
          facetInfo.getValues().addAll(facetInfo.getValues());
        }
      } else {
        LOG.warn("Search query contains value for facet but facet was not added [field: " + facetInfo.getField() + "]");
      }
    }

    return facets;
  }

  private boolean isHidePrices(){
    UserModel currentUser = userService.getCurrentUser();
    if(currentUser instanceof B2BCustomerModel){
      B2BCustomerModel currentCustomer = (B2BCustomerModel) currentUser;
      return currentCustomer.isHidePrices();
    }
    return false;
  }


  @Required
  public void setUserService(UserService userService) {
    this.userService = userService;
  }
}
