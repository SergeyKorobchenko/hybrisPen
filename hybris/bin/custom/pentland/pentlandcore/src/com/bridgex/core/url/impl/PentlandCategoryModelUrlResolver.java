package com.bridgex.core.url.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.url.impl.DefaultCategoryModelUrlResolver;

public class PentlandCategoryModelUrlResolver extends DefaultCategoryModelUrlResolver {

  @Override
  protected String resolveInternal(final CategoryModel source) {

    boolean isBrandCategory = CollectionUtils.isEmpty(source.getSupercategories());
    if (isBrandCategory) {
      return "/" + (StringUtils.isNotBlank(source.getName()) ? source.getName() : source.getCode());
    }
    return super.resolveInternal(source);
  }
}
