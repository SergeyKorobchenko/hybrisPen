package com.bridgex.core.category.dao;

import java.util.Collection;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.daos.CategoryDao;
import de.hybris.platform.category.model.CategoryModel;

public interface PentlandCategoryDao extends CategoryDao {

  Collection<CategoryModel> findRootCategoriesByCatalogVersionNotHidden(CatalogVersionModel catalogVersion);

  /**
   * Find categories with specific flag set/unset
   * @param flagField valid category field
   * @param value flag state
   * @param catalogVersions if empty, search for all catalog versions
   * @return
   */
  Collection<CategoryModel> findCategoriesWithFlag(String flagField, Boolean value, Collection<CatalogVersionModel> catalogVersions);

}
