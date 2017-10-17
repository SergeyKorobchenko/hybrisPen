package com.bridgex.core.product.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.bridgex.core.product.dao.PentlandProductDao;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.daos.impl.DefaultProductDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/17/2017.
 */
public class DefaultPentlandProductDao extends DefaultProductDao implements PentlandProductDao {

  public DefaultPentlandProductDao(String typecode) {
    super(typecode);
  }

  @Override
  public List<ProductModel> findProductsForSapBrandAndCatalogVersion(String sapBrand, CatalogVersionModel catalogVersion, String flagField, Boolean flagValue) {
    StringBuilder queryBuilder = new StringBuilder("select {pk} from {" + ProductModel._TYPECODE + "} ");
    queryBuilder.append("where {" + ProductModel.SAPBRAND + "}=?sapBrand ");
    queryBuilder.append("and {" + ProductModel.CATALOGVERSION + "}=?catalogVersion ");
    if(StringUtils.isNotEmpty(flagField)){
      queryBuilder.append("and {" + flagField + "}=?flagState");
    }

    FlexibleSearchQuery query = new FlexibleSearchQuery(queryBuilder.toString());
    query.addQueryParameter("sapBrand", sapBrand);
    query.addQueryParameter("catalogVersion", catalogVersion);
    if(StringUtils.isNotEmpty(flagField)) {
      query.addQueryParameter("flagState", flagValue);
    }

    return getFlexibleSearchService().<ProductModel>search(query).getResult();
  }
}
