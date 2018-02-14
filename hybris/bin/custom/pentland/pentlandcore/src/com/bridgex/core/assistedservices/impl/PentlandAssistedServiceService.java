package com.bridgex.core.assistedservices.impl;

import static de.hybris.platform.assistedserviceservices.constants.AssistedserviceservicesConstants.*;
import static de.hybris.platform.assistedserviceservices.constants.AssistedserviceservicesConstants.SORT_BY_UID_ASC;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import de.hybris.platform.assistedserviceservices.impl.DefaultAssistedServiceService;
import de.hybris.platform.commerceservices.search.flexiblesearch.data.SortQueryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.user.UserConstants;

/**
 * @author Created by ekaterina.agievich@masterdata.ru on 2/14/2018.
 */
public class PentlandAssistedServiceService extends DefaultAssistedServiceService {

  private static final String USERNAME = "username";
  public static final String LOGIN_DISABLED = "loginDisabled";

  @Override
  public SearchPageData<CustomerModel> getCustomers(final String searchCriteria, final PageableData pageableData)
  {
    final StringBuilder builder = getCustomerSearchQuery(searchCriteria);

    final Map<String, Object> params = new HashMap<>();
    if (StringUtils.isNotBlank(searchCriteria))
    {
      params.put(USERNAME, searchCriteria.toLowerCase());
    }

    params.put(LOGIN_DISABLED, Boolean.FALSE);

    final List<SortQueryData> sortQueries = Arrays.asList(
      createSortQueryData(SORT_BY_UID_ASC, builder.toString() + " ORDER BY {p." + CustomerModel.UID + "} ASC"),
      createSortQueryData(SORT_BY_UID_DESC, builder.toString() + " ORDER BY {p." + CustomerModel.UID + "} DESC"),
      createSortQueryData(SORT_BY_NAME_ASC, builder.toString() + " ORDER BY {p." + CustomerModel.NAME + "} ASC"),
      createSortQueryData(SORT_BY_NAME_DESC, builder.toString() + " ORDER BY {p." + CustomerModel.NAME + "} DESC"));

    return getPagedFlexibleSearchService().search(sortQueries, SORT_BY_UID_ASC, params, pageableData);
  }


  @Override
  protected StringBuilder getCustomerSearchQuery(final String searchCriteria)
  {
    final StringBuilder builder = new StringBuilder();

    builder.append("SELECT ");
    builder.append("{p:" + CustomerModel.PK + "} ");
    builder.append("FROM {" + CustomerModel._TYPECODE + " AS p} ");
    builder.append("WHERE NOT {" + CustomerModel.UID + "}='" + UserConstants.ANONYMOUS_CUSTOMER_UID + "' ");
    builder.append("AND {" + CustomerModel.LOGINDISABLED + "}=?loginDisabled ");
    if (!StringUtils.isBlank(searchCriteria))
    {
      builder.append("AND (LOWER({p:" + CustomerModel.UID + "}) LIKE CONCAT(?username, '%') ");
      builder.append("OR LOWER({p:name}) LIKE CONCAT('%', CONCAT(?username, '%'))) ");
    }
    return builder;
  }
}
