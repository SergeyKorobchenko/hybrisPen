package com.bridgex.facades.populators;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import com.bridgex.core.model.ApparelSizeVariantProductModel;

import de.hybris.platform.commercefacades.product.converters.populator.ProductPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 12/13/2017.
 */
public class PentlandProductPopulator extends ProductPopulator {
  @Override
  public void populate(final ProductModel source, final ProductData target)
  {
    Assert.notNull(source, "Parameter source cannot be null.");
    Assert.notNull(target, "Parameter target cannot be null.");

    getProductBasicPopulator().populate(source, target);
    getVariantSelectedPopulator().populate(source, target);
    getProductPrimaryImagePopulator().populate(source, target);

    target.setCode(source.getCode());
    if(StringUtils.isEmpty(target.getName())) {

      if (source instanceof ApparelSizeVariantProductModel) {
        target.setName(((ApparelSizeVariantProductModel) source).getBaseProduct().getName());
      } else {
        target.setName(source.getName());
      }
    }
    target.setUrl(getProductModelUrlResolver().resolve(source));
  }
}
