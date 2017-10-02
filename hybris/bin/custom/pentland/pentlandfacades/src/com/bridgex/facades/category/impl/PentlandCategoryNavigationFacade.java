package com.bridgex.facades.category.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.category.PentlandCategoryService;
import com.bridgex.facades.category.CategoryNavigationFacade;
import com.bridgex.facades.category.data.CategoryNavigationData;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

public class PentlandCategoryNavigationFacade implements CategoryNavigationFacade {

  private static final String CATEGORY_CATALOG = "pentlandProductCatalog";
  private static final String CATEGORY_CATALOG_VERSION = "Online";
  private static final String ROOT_CODE = "root";


  private Converter<CategoryModel, CategoryNavigationData> categoryNavigationConverter;
  private PentlandCategoryService                          categoryService;
  private CatalogVersionService                            catalogVersionService;

  @Override
  public CategoryNavigationData getRootCategoryNavigation() {

    CategoryNavigationData root = new CategoryNavigationData();
    root.setCode(ROOT_CODE);
    root.setChildren(new ArrayList<>());
    CatalogVersionModel catalogVersion = getCatalogVersionService().getCatalogVersion(CATEGORY_CATALOG, CATEGORY_CATALOG_VERSION);
    Collection<CategoryModel> rootCategories = getCategoryService().getRootCategoriesForCatalogVersionNotHidden(catalogVersion);
    constructCategoryNavigation(rootCategories, root);
    return root;
  }

  private void constructCategoryNavigation(Collection<CategoryModel> cat, CategoryNavigationData root) {

    // level1 - brands
    cat.forEach(brandCat -> {
      CategoryNavigationData level1NavigationData = getCategoryNavigationConverter().convert(brandCat);
      level1NavigationData.setChildren(new ArrayList<>());
      // level2
      getVisibleChildren(brandCat).forEach(level2Cat -> {
        CategoryNavigationData leve2NavigationData = getCategoryNavigationConverter().convert(level2Cat);
        leve2NavigationData.setChildren(new ArrayList<>());
        // level3
        getVisibleChildren(level2Cat).forEach(level3Cat -> {
          CategoryNavigationData leve3NavigationData = getCategoryNavigationConverter().convert(level3Cat);
          leve2NavigationData.getChildren().add(leve3NavigationData);
        });
        level1NavigationData.getChildren().add(leve2NavigationData);
      });
      root.getChildren().add(level1NavigationData);
    });

  }


  private List<CategoryModel> getVisibleChildren(CategoryModel cat) {
    // todo possible place for optimization - create dao method to get not hidden child categories
    return cat.getCategories().stream().filter(childCat -> ! childCat.isHidden()).collect(Collectors.toList());
  }

  public Converter<CategoryModel, CategoryNavigationData> getCategoryNavigationConverter() {
    return categoryNavigationConverter;
  }

  @Required
  public void setCategoryNavigationConverter(Converter<CategoryModel, CategoryNavigationData> categoryNavigationConverter) {
    this.categoryNavigationConverter = categoryNavigationConverter;
  }

  public PentlandCategoryService getCategoryService() {
    return categoryService;
  }

  @Required
  public void setCategoryService(PentlandCategoryService categoryService) {
    this.categoryService = categoryService;
  }

  public CatalogVersionService getCatalogVersionService() {
    return catalogVersionService;
  }

  @Required
  public void setCatalogVersionService(CatalogVersionService catalogVersionService) {
    this.catalogVersionService = catalogVersionService;
  }
}
