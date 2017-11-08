package com.bridgex.core.category.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.category.PentlandCategoryService;
import com.bridgex.core.category.dao.PentlandCategoryDao;
import com.bridgex.core.product.PentlandProductService;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.impl.DefaultCategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

public class DefaultPentlandCategoryService extends DefaultCategoryService implements PentlandCategoryService {

  private static final Logger LOG = Logger.getLogger(DefaultPentlandCategoryService.class);

  private PentlandCategoryDao    categoryDao;
  private PentlandProductService productService;


  @Override
  public Collection<CategoryModel> getRootCategoriesForCatalogVersionNotHidden(CatalogVersionModel catalogVersion) {
    validateParameterNotNull(catalogVersion, "Parameter 'catalogVersion' was null.");
    return getCategoryDao().findRootCategoriesByCatalogVersionNotHidden(catalogVersion);
  }

  public CategoryModel getBrandCategoryForProduct(ProductModel productModel){
    String sapBrand = (String)getProductService().getProductAttribute(productModel, ProductModel.SAPBRAND);
    if(StringUtils.isNotEmpty(sapBrand)) {
      try {
        return this.getCategoryForCode(productModel.getCatalogVersion(), sapBrand);
      }catch(UnknownIdentifierException e){
        LOG.debug("Missing category for sapBrand " + sapBrand);
      }catch(AmbiguousIdentifierException e){
        LOG.debug("Mode than one category found for sapBrand " + sapBrand);
      }
    }
    return null;
  }

  protected PentlandCategoryDao getCategoryDao() {
    return categoryDao;
  }

  @Required
  public void setCategoryDao(PentlandCategoryDao categoryDao) {
    super.setCategoryDao(categoryDao);
    this.categoryDao = categoryDao;
  }

  public PentlandProductService getProductService() {
    return productService;
  }

  @Required
  public void setProductService(PentlandProductService productService) {
    this.productService = productService;
  }
}
