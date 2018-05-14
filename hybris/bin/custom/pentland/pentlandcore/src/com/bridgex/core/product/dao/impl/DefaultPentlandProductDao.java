package com.bridgex.core.product.dao.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.bridgex.core.product.dao.PentlandProductDao;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.constants.CategoryConstants;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.daos.impl.DefaultProductDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

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
  
  @SuppressWarnings("deprecation")
  @Override
 public ProductModel checkProductWithCurrentUserSMUCategory(String productCode, String categoryCode,
 		CatalogVersionModel catalogVersion) 
 {
 	StringBuilder queryBuilder = new StringBuilder(
 			"SELECT {P.pk} FROM {" + CategoryConstants.Relations.CATEGORYPRODUCTRELATION);
 	queryBuilder.append(" AS C2PRel").append(" JOIN ").append(CategoryModel._TYPECODE);
 	queryBuilder.append(" AS C ").append("ON {C2PRel.source}={C.pk}");
 	queryBuilder.append(" JOIN ").append(ProductModel._TYPECODE + " AS P");
 	queryBuilder.append(" ON {P.pk}={C2PRel.target}").append("}").append(" WHERE");
 	queryBuilder.append(" {P.code}=?productCode").append(" AND").append(" {C.smu}=?isSmu").append(" AND").append(" {C.code}=?categoryCode");
 	queryBuilder.append(" AND {C." + ProductModel.CATALOGVERSION + "} = ?" + ProductModel.CATALOGVERSION );

 	FlexibleSearchQuery query = new FlexibleSearchQuery(queryBuilder.toString());
 	query.addQueryParameter("productCode", productCode);
 	query.addQueryParameter("isSmu", true);
 	query.addQueryParameter("categoryCode", categoryCode);
 	query.addQueryParameter(ProductModel.CATALOGVERSION, catalogVersion);
 	final SearchResult<ProductModel> searchRes = getFlexibleSearchService().search(query);
 	return CollectionUtils.isNotEmpty(searchRes.getResult())?searchRes.getResult().get(0):null;
 }
  
}
