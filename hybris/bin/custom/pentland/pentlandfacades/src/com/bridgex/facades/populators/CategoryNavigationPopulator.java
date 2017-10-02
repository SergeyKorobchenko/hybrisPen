package com.bridgex.facades.populators;

import org.springframework.beans.factory.annotation.Required;

import com.bridgex.facades.category.data.CategoryNavigationData;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.url.impl.AbstractUrlResolver;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

public class CategoryNavigationPopulator implements Populator<CategoryModel, CategoryNavigationData> {

  private AbstractUrlResolver<CategoryModel> categoryUrlResolver;

  @Override
  public void populate(CategoryModel source, CategoryNavigationData target) throws ConversionException {
    target.setCode(source.getCode());
    target.setName(source.getName());
    target.setUrl(getCategoryUrlResolver().resolve(source));

  }

  public AbstractUrlResolver<CategoryModel> getCategoryUrlResolver() {
    return categoryUrlResolver;
  }

  @Required
  public void setCategoryUrlResolver(AbstractUrlResolver<CategoryModel> categoryUrlResolver) {
    this.categoryUrlResolver = categoryUrlResolver;
  }
}
