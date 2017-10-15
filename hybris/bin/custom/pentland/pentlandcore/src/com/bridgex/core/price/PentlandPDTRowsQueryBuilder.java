package com.bridgex.core.price;

import java.util.List;

import com.bridgex.core.price.impl.DefaultPentlandPDTRowsQueryBuilder;

import de.hybris.platform.core.PK;
import de.hybris.platform.europe1.jalo.PDTRowsQueryBuilder;

/**
 * Created by Lenar on 10/14/2017.
 */
public interface PentlandPDTRowsQueryBuilder extends PDTRowsQueryBuilder {

  /**
   * Adds list of user groups to query
   * @param upgs list of upg
   * @return query builder
   */
  PentlandPDTRowsQueryBuilder withUserGroups(List<PK> upgs);


  static PentlandPDTRowsQueryBuilder defaultBuilder(String type) {
    return new DefaultPentlandPDTRowsQueryBuilder(type);
  }

  PentlandPDTRowsQueryBuilder withAnyProduct();

  PentlandPDTRowsQueryBuilder withProduct(PK var1);

  PentlandPDTRowsQueryBuilder withProductGroup(PK var1);

  PentlandPDTRowsQueryBuilder withProductId(String var1);

  PentlandPDTRowsQueryBuilder withAnyUser();

  PentlandPDTRowsQueryBuilder withUser(PK var1);

  PentlandPDTRowsQueryBuilder withUserGroup(PK var1);

  PentlandPDTRowsQueryBuilder.QueryWithParams build();
}
