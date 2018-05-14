package com.bridgex.core.category.dao.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.bridgex.core.category.dao.PentlandCategoryDao;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.constants.CategoryConstants;
import de.hybris.platform.category.daos.impl.DefaultCategoryDao;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

public class DefaultPentlandCategoryDao extends DefaultCategoryDao implements PentlandCategoryDao {
	
	@Override
  public Collection<CategoryModel> findRootCategoriesByCatalogVersionNotHidden(CatalogVersionModel catalogVersion) {
    String query = "SELECT {cat." + CategoryModel.PK + "} " + "FROM {" + CategoryModel._TYPECODE + " AS cat} " +
                   "WHERE NOT EXISTS ({{" +
                       "SELECT {pk} FROM {" + CategoryModel._CATEGORYCATEGORYRELATION + " AS rel " +
                       "JOIN " + CategoryModel._TYPECODE + " AS super ON {rel.source}={super.PK} } " +
                       "WHERE {rel:target}={cat.pk} " + "AND {super." + CategoryModel.CATALOGVERSION + "}={cat." + CategoryModel.CATALOGVERSION + "} }}) " +
                   "AND {cat." + CategoryModel.CATALOGVERSION + "} = ?" + CategoryModel.CATALOGVERSION + " " +
                   "AND {cat." + CategoryModel.HIDDEN + "} = 0 " +
                    "order by {cat." + CategoryModel.ORDER + "} asc";

    final Map<String, Object> params = (Map) Collections.singletonMap(CategoryModel.CATALOGVERSION, catalogVersion);

    final SearchResult<CategoryModel> searchRes = search(query, params);
    return searchRes.getResult();
  }

  @Override
  public Collection<CategoryModel> findCategoriesWithFlag(String flagField, Boolean value, Collection<CatalogVersionModel> catalogVersions) {
    StringBuilder queryBuilder = new StringBuilder("select {pk} from {" + CategoryModel._TYPECODE + "} ");
    queryBuilder.append("where {" + flagField + "}=?state ");
    if(CollectionUtils.isNotEmpty(catalogVersions)){
      queryBuilder.append(" AND {" + CategoryModel.CATALOGVERSION + "} in (?catalogVersions)");
    }

    FlexibleSearchQuery query = new FlexibleSearchQuery(queryBuilder.toString());
    query.addQueryParameter("state", value);
    if(CollectionUtils.isNotEmpty(catalogVersions)){
      query.addQueryParameter("catalogVersions", catalogVersions);
    }

    final SearchResult<CategoryModel> searchRes = getFlexibleSearchService().search(query);
    return searchRes.getResult();
  }
  
 @SuppressWarnings("deprecation")
@Override
public Collection<CategoryModel> getSMUCategoriesForCurrentUser(String userId,CatalogVersionModel catalogVersion)
{
		StringBuilder queryBuilder = new StringBuilder(
				"SELECT {C.pk} FROM {" + CategoryConstants.Relations.CATEGORY2PRINCIPALRELATION);
		queryBuilder.append(" AS C2PRel").append(" JOIN ").append(CategoryModel._TYPECODE);
		queryBuilder.append(" AS C ").append("ON {C2PRel.source}={C.pk}");
		queryBuilder.append(" JOIN ").append(B2BCustomerModel._TYPECODE + " AS CUS");
		queryBuilder.append(" ON {CUS.pk}={C2PRel.target}").append("}").append(" WHERE");
		queryBuilder.append(" {CUS.uid}=?customerId").append(" AND").append(" {C.smu}=?isSmu");
		queryBuilder.append(" AND {C." + CategoryModel.CATALOGVERSION + "} = ?" + CategoryModel.CATALOGVERSION );

		FlexibleSearchQuery query = new FlexibleSearchQuery(queryBuilder.toString());
		query.addQueryParameter("customerId",  userId);
		query.addQueryParameter("isSmu", true);
		query.addQueryParameter(CategoryModel.CATALOGVERSION, catalogVersion);
		final SearchResult<CategoryModel> searchRes = getFlexibleSearchService().search(query);
		return searchRes.getResult();
}
 
}
