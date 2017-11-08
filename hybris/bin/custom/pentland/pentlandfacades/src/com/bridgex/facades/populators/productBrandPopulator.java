package com.bridgex.facades.populators;

import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.category.PentlandCategoryService;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

/**
 * @author Created by konstantin.pavlyukov on 11/8/2017.
 */
public class ProductBrandPopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends AbstractProductPopulator<SOURCE, TARGET> {

  PentlandCategoryService categoryService;

  @Override
  public void populate(SOURCE source, TARGET target) throws ConversionException {
    CategoryModel brand = categoryService.getBrandCategoryForProduct(source);
    if (brand != null) {
      target.setBrand(brand.getName());
    }
  }

  public PentlandCategoryService getCategoryService() {
    return categoryService;
  }

  @Required
  public void setCategoryService(PentlandCategoryService categoryService) {
    this.categoryService = categoryService;
  }
}
