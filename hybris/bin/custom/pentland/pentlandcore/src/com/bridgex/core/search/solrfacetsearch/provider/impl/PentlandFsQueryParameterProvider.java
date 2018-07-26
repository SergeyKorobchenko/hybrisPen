package com.bridgex.core.search.solrfacetsearch.provider.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.enums.DiscontinuedStatus;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.solrfacetsearch.provider.ParameterProvider;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/18/2017.
 */
public class PentlandFsQueryParameterProvider implements ParameterProvider{

  private static final String PRODUCT_CATALOG = "pentlandProductCatalog";
  private static final String VERSION = "Online";

  private CatalogVersionService catalogVersionService;

  @Override
  public Map<String, Object> createParameters() {
    HashMap<String, Object> parameters = new HashMap<>();

    parameters.put("discontinuedStates", Arrays.asList(DiscontinuedStatus.D03, DiscontinuedStatus.D04));
    parameters.put("approvalStatus", ArticleApprovalStatus.APPROVED);
    parameters.put("catalogVersion", catalogVersionService.getCatalogVersion(PRODUCT_CATALOG, VERSION));

    return parameters;
  }

  @Required
  public void setCatalogVersionService(CatalogVersionService catalogVersionService) {
    this.catalogVersionService = catalogVersionService;
  }
}
