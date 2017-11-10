package com.bridgex.core.search.solrfacetsearch.populator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.constants.PentlandcoreConstants;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.europe1.enums.UserPriceGroup;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.solrfacetsearch.search.impl.SearchQueryConverterData;

/**
 * @author Created by ekaterina.agievich on 9/25/2017.
 */
public class AdditionalFiltersQueryPopulator implements Populator<SearchQueryConverterData, SolrQuery> {

  private static final String VISIBILITY_QUERY = "categoryVisibility_string_mv:%s";
  private static final String PRICE_QUERY = "priceValue_%s_%s_double:[0.01 TO *]";

  private UserService userService;
  private SessionService sessionService;

  @Override
  public void populate(SearchQueryConverterData searchQueryConverterData, SolrQuery solrQuery) throws ConversionException {
    String visibilityQuery = buildCategoryVisibilityQuery();
    String priceQuery = buildPriceQuery(searchQueryConverterData);
    solrQuery.addFilterQuery(visibilityQuery);
    solrQuery.addFilterQuery(priceQuery);
  }

  private String buildPriceQuery(SearchQueryConverterData searchQueryConverterData) {
    String currency = searchQueryConverterData.getSearchQuery().getCurrency().toLowerCase();
    Set<String> upgs = new HashSet<>();
    List<UserPriceGroup> sessionUPGs = sessionService.getAttribute(Europe1Constants.PARAMS.UPG);
    if(CollectionUtils.isNotEmpty(sessionUPGs)){
      upgs.addAll(sessionUPGs.stream().map(upg -> upg.getCode().toLowerCase()).collect(Collectors.toList()));
    }
    List<UserPriceGroup> parentUPG = sessionService.getAttribute(PentlandcoreConstants.PARENT_UPG);
    if(CollectionUtils.isNotEmpty(parentUPG)){
      upgs.addAll(parentUPG.stream().map(upg -> upg.getCode().toLowerCase()).collect(Collectors.toList()));
    }

    StringBuilder priceFilterQuery = new StringBuilder();
    String OR = "";
    for(String upg: upgs){
      priceFilterQuery.append(OR);
      priceFilterQuery.append(String.format(PRICE_QUERY, upg, currency));
      OR = " OR ";
    }

    return priceFilterQuery.toString();
  }

  private String buildCategoryVisibilityQuery() {
    UserModel currentUser = userService.getCurrentUser();
    Set<PrincipalGroupModel> allGroups = currentUser.getAllGroups();
    List<String> principals = allGroups.stream().map(PrincipalModel::getUid).collect(Collectors.toList());
    principals.add(currentUser.getUid());
    return String.format(VISIBILITY_QUERY, "(\"" + principals.stream().collect(Collectors.joining("\" or \"")) + "\")");
  }

  @Required
  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  @Required
  public void setSessionService(SessionService sessionService) {
    this.sessionService = sessionService;
  }
}

