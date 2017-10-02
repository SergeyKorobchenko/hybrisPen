package com.bridgex.core.category.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.category.PentlandCategoryService;
import com.bridgex.core.category.dao.PentlandCategoryDao;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.impl.DefaultCategoryService;
import de.hybris.platform.category.model.CategoryModel;

public class DefaultPentlandCategoryService extends DefaultCategoryService implements PentlandCategoryService {

  private PentlandCategoryDao categoryDao;

  @Override
  public Collection<CategoryModel> getRootCategoriesForCatalogVersionNotHidden(CatalogVersionModel catalogVersion) {
    validateParameterNotNull(catalogVersion, "Parameter 'catalogVersion' was null.");
    return getCategoryDao().findRootCategoriesByCatalogVersionNotHidden(catalogVersion);
  }

  protected PentlandCategoryDao getCategoryDao() {
    return categoryDao;
  }

  @Required
  public void setCategoryDao(PentlandCategoryDao categoryDao) {
    super.setCategoryDao(categoryDao);
    this.categoryDao = categoryDao;
  }
}
