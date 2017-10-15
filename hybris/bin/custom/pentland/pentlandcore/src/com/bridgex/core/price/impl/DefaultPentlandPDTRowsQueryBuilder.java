package com.bridgex.core.price.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;

import com.bridgex.core.price.PentlandPDTRowsQueryBuilder;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;

import de.hybris.platform.core.PK;
import de.hybris.platform.europe1.jalo.Europe1PriceFactory;

/**
 * Created by Lenar on 10/14/2017.
 */
public class DefaultPentlandPDTRowsQueryBuilder implements PentlandPDTRowsQueryBuilder {

  private static final String PRODUCT_ID = "productId";
  private final String         type;
  private       boolean        anyProduct;
  private       PK             productPk;
  private       PK             productGroupPk;
  private       String         productId;
  private       boolean        anyUser;
  private       PK             userPk;
  private       PK             userGroupPk;
  private       List<PK>       userGroupsPk;

  public DefaultPentlandPDTRowsQueryBuilder(String type) {
    this.type = Objects.requireNonNull(type);
  }

  @Override
  public PentlandPDTRowsQueryBuilder withAnyProduct() {
    this.anyProduct = true;
    return this;
  }

  @Override
  public PentlandPDTRowsQueryBuilder withProduct(PK productPk) {
    this.productPk = productPk;
    return this;
  }

  @Override
  public PentlandPDTRowsQueryBuilder withProductGroup(PK productGroupPk) {
    this.productGroupPk = productGroupPk;
    return this;
  }

  @Override
  public PentlandPDTRowsQueryBuilder withProductId(String productId) {
    this.productId = productId;
    return this;
  }

  @Override
  public PentlandPDTRowsQueryBuilder withAnyUser() {
    this.anyUser = true;
    return this;
  }

  @Override
  public PentlandPDTRowsQueryBuilder withUser(PK userPk) {
    this.userPk = userPk;
    return this;
  }

  @Override
  public PentlandPDTRowsQueryBuilder withUserGroup(PK userGroupPk) {
    this.userGroupPk = userGroupPk;
    return this;
  }

  @Override
  public PentlandPDTRowsQueryBuilder withUserGroups(List<PK> upgs) {
    this.userGroupsPk = upgs;
    return this;
  }

  @Override
  public QueryWithParams build() {
    StringBuilder query = new StringBuilder();
    ImmutableMap.Builder<String, Object> params = ImmutableMap.builder();
    Map<String, Object> productParams = getProductRelatedParameters();
    Map<String, Object> userParams = getUserRelatedParameters();
    boolean addPricesByProductId = productId != null;
    boolean isUnion = false;
    boolean matchByProduct = !productParams.isEmpty();
    boolean matchByUser = !userParams.isEmpty();
    if (!matchByProduct && !matchByUser && !addPricesByProductId) {
      return new QueryWithParams("select {PK} from {" + type + "}", Collections.emptyMap());
    }
    else {

      if (matchByProduct || matchByUser) {
        query.append("select {PK} from {").append(type).append("} where ");
        if (matchByProduct) {
          query.append("{").append("productMatchQualifier").append("} in (?");
          query.append(Joiner.on(", ?").join(productParams.keySet())).append(")");
          params.putAll(productParams);
          if (matchByUser) {
            query.append(" and ");
          }
        }

        if (matchByUser) { //userMatchQualifier - it can be user or priceList or -1 (price row for all)
          query.append("{").append("userMatchQualifier").append("} in (?");
          query.append(Joiner.on(", ?").join(userParams.keySet())).append(")");
          params.putAll(userParams);
        }

        if (CollectionUtils.isNotEmpty(userGroupsPk)) {
          query.append(" AND {").append("ug").append("} IN (?").append("userGroups").append(") ");
          params.put("userGroups", userGroupsPk);
        }

      }

      if (addPricesByProductId) { //TODO is there really need for two queries?
        if (matchByProduct || matchByUser) {
          query.append("}} UNION {{");
          isUnion = true;
        }

        query.append("select {PK} from {").append(type).append("} where {");
        query.append("productMatchQualifier").append("}=?matchByProductId and {");
        query.append(PRODUCT_ID).append("}=?").append(PRODUCT_ID);
        params.put("matchByProductId", Europe1PriceFactory.MATCH_BY_PRODUCT_ID);
        params.put(PRODUCT_ID, productId);
        if (matchByUser) {
          query.append(" and {").append("userMatchQualifier").append("} in (?");
          query.append(Joiner.on(", ?").join(userParams.keySet())).append(")");
        }

        if (CollectionUtils.isNotEmpty(userGroupsPk)) {
          query.append(" AND {").append("ug").append("} IN (?").append("userGroups").append(") ");
        }
      }

      StringBuilder resultQuery;
      if (isUnion) {
        resultQuery = (new StringBuilder("select x.PK from ({{")).append(query).append("}}) x");
      }
      else {
        resultQuery = query;
      }

      return new QueryWithParams(resultQuery.toString(), params.build());
    }
  }

  private Map<String, Object> getProductRelatedParameters() {
    ImmutableMap.Builder<String, Object> params = ImmutableMap.builder();
    if (anyProduct) {
      params.put("anyProduct", Europe1PriceFactory.MATCH_ANY);
    }

    if (productPk != null) {
      params.put("product", productPk.getLong());
    }

    if (productGroupPk != null) {
      params.put("productGroup", productGroupPk.getLong());
    }

    return params.build();
  }

  private Map<String, Object> getUserRelatedParameters() {
    ImmutableMap.Builder<String, Object> params = ImmutableMap.builder();
    if (anyUser) {
      params.put("anyUser", Europe1PriceFactory.MATCH_ANY);
    }

    if (userPk != null) {
      params.put("user", userPk.getLong());
    }

    if (userGroupPk != null) {
      params.put("userGroup", userGroupPk.getLong());
    }

    return params.build();
  }

}
