package com.bridgex.core.search.solrfacetsearch.populator;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.solrfacetsearch.search.impl.SearchQueryConverterData;

/**
 * @author Created by ekaterina.agievich@masterdata.ru on 9/25/2017.
 */
public class AdditionalFiltersQueryPopulator implements Populator<SearchQueryConverterData, SolrQuery> {

  private static final String QUERY = "categoryVisibility_string_mv:%s";

  private UserService userService;

  @Override
  public void populate(SearchQueryConverterData searchQueryConverterData, SolrQuery solrQuery) throws ConversionException {
    UserModel currentUser = userService.getCurrentUser();
    Set<PrincipalGroupModel> allGroups = currentUser.getAllGroups();
    List<String> principals = allGroups.stream().map(PrincipalModel::getUid).collect(Collectors.toList());
    principals.add(currentUser.getUid());

    solrQuery.addFilterQuery(String.format(QUERY, "(\"" + principals.stream().collect(Collectors.joining("\" or \"")) + "\")"));
  }

  @Required
  public void setUserService(UserService userService) {
    this.userService = userService;
  }
}

