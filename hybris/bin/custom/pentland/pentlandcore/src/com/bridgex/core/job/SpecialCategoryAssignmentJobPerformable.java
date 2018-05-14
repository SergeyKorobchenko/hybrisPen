package com.bridgex.core.job;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.category.PentlandCategoryService;
import com.bridgex.core.model.CatalogVersionAwareCronJobModel;
import com.bridgex.core.product.PentlandProductService;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/17/2017.
 */
public class SpecialCategoryAssignmentJobPerformable extends AbstractJobPerformable<CatalogVersionAwareCronJobModel> {

  private PentlandCategoryService categoryService;
  private PentlandProductService productService;

  private static final Logger LOG = Logger.getLogger(SpecialCategoryAssignmentJobPerformable.class);
  @Override
  public PerformResult perform(CatalogVersionAwareCronJobModel cronJobModel) {

    Collection<CatalogVersionModel> catalogVersions = cronJobModel.getCatalogVersions();
    for(CatalogVersionModel catalogVersion: catalogVersions){
      Collection<CategoryModel> rootCategories = categoryService.getRootCategoriesForCatalogVersion(catalogVersion);
      for(CategoryModel brand: rootCategories){
        CategoryModel smuCategory = brand.getCategories().stream().filter(CategoryModel::isSmu).findFirst().orElse(null);
        CategoryModel clearanceCategory = brand.getCategories().stream().filter(CategoryModel::isClearance).findFirst().orElse(null);
        processSMUCategory(smuCategory, brand, catalogVersion);
        processClearanceCategory(clearanceCategory, brand, catalogVersion);
      }
    }

    return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
  }

  void processSMUCategory(CategoryModel category, CategoryModel brand, CatalogVersionModel catalogVersion){
    if(category == null){
      category = modelService.create(CategoryModel.class);
      category.setSmu(true);
      category.setCatalogVersion(catalogVersion);
      category.setSupercategories(Collections.singletonList(brand));
      category.setCode(brand.getCode() + "_SMU");
      category.setName("SMU");
    }

    List<ProductModel> smuProductsForSapBrandAndCatalogVersion = productService.findSMUProductsForSapBrandAndCatalogVersion(brand.getCode(), catalogVersion);
    category.setProducts(smuProductsForSapBrandAndCatalogVersion);

    modelService.save(category);
  }

  void processClearanceCategory(CategoryModel category, CategoryModel brand, CatalogVersionModel catalogVersion){
    if(category == null){
      category = modelService.create(CategoryModel.class);
      category.setClearance(true);
      category.setCatalogVersion(catalogVersion);
      category.setSupercategories(Collections.singletonList(brand));
      category.setCode(brand.getCode() + "_Clearance");
      category.setName("Clearance");
      category.setAllowedPrincipals(brand.getAllowedPrincipals());
    }

    List<ProductModel> clearanceProductsForSapBrandAndCatalogVersion = productService.findClearanceProductsForSapBrandAndCatalogVersion(brand.getCode(), catalogVersion);
    for(ProductModel productModel: clearanceProductsForSapBrandAndCatalogVersion){
      productModel.setSupercategories(null);
    }
    modelService.saveAll(clearanceProductsForSapBrandAndCatalogVersion);
    category.setProducts(clearanceProductsForSapBrandAndCatalogVersion);

    modelService.save(category);
  }

  @Required
  public void setCategoryService(PentlandCategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @Required
  public void setProductService(PentlandProductService productService) {
    this.productService = productService;
  }
}
