package com.bridgex.core.search.solrfacetsearch.populator;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.ObjectUtils;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.solrfacetsearch.config.FacetType;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider.FieldType;
import de.hybris.platform.solrfacetsearch.search.FacetField;
import de.hybris.platform.solrfacetsearch.search.FacetValueField;
import de.hybris.platform.solrfacetsearch.search.QueryField;
import de.hybris.platform.solrfacetsearch.search.RawQuery;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchQuery.Operator;
import de.hybris.platform.solrfacetsearch.search.impl.SearchQueryConverterData;
import de.hybris.platform.solrfacetsearch.search.impl.populators.FacetSearchQueryFacetsPopulator;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/24/2017.
 */
public class PentlandFacetSearchQueryFacetsPopulator extends FacetSearchQueryFacetsPopulator {

  private UserService userService;

  private static final Logger LOG = Logger.getLogger(PentlandFacetSearchQueryFacetsPopulator.class);

  @Override
	public void populate(SearchQueryConverterData source, SolrQuery target) {
	  SearchQuery searchQuery = source.getSearchQuery();
	  Set<String> fae=new HashSet<>();
	  fae.add("false");
	  QueryField smuFacetFilterQuery = new QueryField("smu", Operator.AND, fae);
	  String smuTranslatedField = this.getFieldNameTranslator().translate(searchQuery, "smu",
				FieldType.INDEX);
	  if(isSmuUser())
	  {
	  String smuFilterQuery = this.convertQueryField(searchQuery, smuFacetFilterQuery);
	  target.addFilterQuery(new String[]{"{!tag=fk" + 1 + "}" + smuFilterQuery});
	 
		target.addFacetField(new String[]{"{!ex=fk" + 1 + "}" + smuTranslatedField});
	  }
	 
		Map facets = this.prepareFacets(searchQuery);
		int index = 0;

		for (Iterator arg6 = facets.values().iterator(); arg6.hasNext(); ++index) {
			FacetSearchQueryFacetsPopulator.FacetInfo facet = (FacetSearchQueryFacetsPopulator.FacetInfo) arg6.next();
			String translatedField = this.getFieldNameTranslator().translate(searchQuery, facet.getField(),
					FieldType.INDEX);
			FacetType facetType = facet.getFacetType();
			if (CollectionUtils.isEmpty(facet.getValues())) {
				target.addFacetField(new String[]{translatedField});
			} else {
				QueryField facetFilterQuery;
				String filterQuery;
				if (FacetType.REFINE.equals(facetType)) {
					if (facet.getValues().size() > 1) {
						LOG.warn("Multiple values found for facet with type REFINE [field: " + facet.getField() + "]");
					}

					facetFilterQuery = new QueryField(facet.getField(), Operator.AND, facet.getValues());
					filterQuery = this.convertQueryField(searchQuery, facetFilterQuery);
					target.addFilterQuery(new String[]{filterQuery});
					target.addFacetField(new String[]{translatedField});
				} else if (FacetType.MULTISELECTAND.equals(facetType)) {
					facetFilterQuery = new QueryField(facet.getField(), Operator.AND, facet.getValues());
					filterQuery = this.convertQueryField(searchQuery, facetFilterQuery);
					target.addFilterQuery(new String[]{"{!tag=fk" + index + "}" + filterQuery});
					target.addFacetField(new String[]{"{!ex=fk" + index + "}" + translatedField});
				} else if (FacetType.MULTISELECTOR.equals(facetType)) {
					facetFilterQuery = new QueryField(facet.getField(), Operator.OR, facet.getValues());
					filterQuery = this.convertQueryField(searchQuery, facetFilterQuery);
					target.addFilterQuery(new String[]{"{!tag=fk" + index + "}" + filterQuery});
					target.addFacetField(new String[]{"{!ex=fk" + index + "}" + translatedField});
				} else {
					LOG.warn("Unknown facet type [field: " + facet.getField() + "]");
				}
			}
		}

		target.setFacetSort(this.resolveFacetSort());
		target.setFacetMinCount(MINIMUM_COUNT.intValue());
		target.setFacetLimit(DEFAULT_LIMIT.intValue());
	}
  
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
        if(CollectionUtils.isNotEmpty(facetValue.getValues())) {
          facetInfo.getValues().addAll(facetValue.getValues());
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
  
  private boolean isSmuUser(){
	  UserModel currentUser = userService.getCurrentUser();
	  if(currentUser instanceof B2BCustomerModel){
	    Collection<CategoryModel> accessibleCategories = currentUser.getAccessibleCategories();
	    CategoryModel categoryModel = accessibleCategories.stream().filter(category ->category.getCode().contains("_SMU")).findFirst().orElse(null);
	    if(categoryModel==null && ObjectUtils.isEmpty(categoryModel))
	    {
	    	return true;
	    }
	  }
	  return false;
  }

  @Required
  public void setUserService(UserService userService) {
    this.userService = userService;
  }
}
