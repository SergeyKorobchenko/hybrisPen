package com.bridgex.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.user.impl.DefaultUserService;
import de.hybris.platform.site.BaseSiteService;

/**
 * @author Goncharenko Mikhail, created on 30.07.2018.
 */
public class SiteBasedUserService extends DefaultUserService {

  private FlexibleSearchService flexibleSearchService;
  private BaseSiteService       baseSiteService;

  private static final String QUERY_USER_FOR_SITE = "SELECT " + UserModel.PK + " FROM {" + UserModel._TYPECODE + "}" +
                                                            "WHERE {" + UserModel.LOGIN + "} = ?login" +
                                                            "AND {"+ UserModel.REGISTRATIONSITE +"} = ?baseSite";

  @Override
  public UserModel getUserForUID(String userId) {
    Map<String, Object> params = new HashMap<>();
    params.put("login", userId);
    params.put("baseSite", baseSiteService.getCurrentBaseSite());
    return flexibleSearchService.searchUnique(new FlexibleSearchQuery(QUERY_USER_FOR_SITE, params));
  }

  @Required
  public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
    this.flexibleSearchService = flexibleSearchService;
  }

  @Required
  public void setBaseSiteService(BaseSiteService baseSiteService) {
    this.baseSiteService = baseSiteService;
  }
}
