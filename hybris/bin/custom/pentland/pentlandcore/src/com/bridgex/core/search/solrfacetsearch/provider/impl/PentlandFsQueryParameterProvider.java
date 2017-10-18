package com.bridgex.core.search.solrfacetsearch.provider.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.bridgex.core.enums.DiscontinuedStatus;

import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.solrfacetsearch.provider.ParameterProvider;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/18/2017.
 */
public class PentlandFsQueryParameterProvider implements ParameterProvider{
  @Override
  public Map<String, Object> createParameters() {
    HashMap<String, Object> parameters = new HashMap<>();

    parameters.put("discontinuedStates", Arrays.asList(DiscontinuedStatus.D03, DiscontinuedStatus.D04, DiscontinuedStatus.D05));
    parameters.put("approvalStatus", ArticleApprovalStatus.APPROVED);

    return parameters;
  }
}
