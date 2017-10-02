package com.bridgex.core.category;

import java.util.Collection;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;

public interface PentlandCategoryService extends CategoryService {

  Collection<CategoryModel> getRootCategoriesForCatalogVersionNotHidden(final CatalogVersionModel catalogVersion);

}
