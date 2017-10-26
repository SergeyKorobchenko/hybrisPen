package com.bridgex.core.search.solrfacetsearch.provider.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.category.PentlandCategoryService;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/18/2017.
 */
public abstract class AbstractBrandCategoryResolver extends AbstractBaseProductValueResolver {

  private static final Logger LOG = Logger.getLogger(AbstractBrandCategoryResolver.class);

  private PentlandCategoryService categoryService;

  protected CategoryModel getBrandCategoryForProduct(ProductModel productModel){
    ProductModel baseProductModel = getBaseProductModel(productModel);
    String sapBrand = baseProductModel.getSapBrand();
    if(StringUtils.isNotEmpty(sapBrand)) {
      try {
        return categoryService.getCategoryForCode(productModel.getCatalogVersion(), sapBrand);
      }catch(UnknownIdentifierException e){
        LOG.debug("Missing category for sapBrand " + sapBrand);
      }catch(AmbiguousIdentifierException e){
        LOG.debug("Mode than one category found for sapBrand " + sapBrand);
      }
    }
    return null;
  }

  @Required
  public void setCategoryService(PentlandCategoryService categoryService) {
    this.categoryService = categoryService;
  }
}
