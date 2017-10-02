package com.bridgex.core.category.dao;

import java.util.Collection;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.daos.CategoryDao;
import de.hybris.platform.category.model.CategoryModel;

public interface PentlandCategoryDao extends CategoryDao {

  Collection<CategoryModel> findRootCategoriesByCatalogVersionNotHidden(CatalogVersionModel catalogVersion);

}
