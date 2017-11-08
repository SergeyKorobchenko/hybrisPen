package com.bridgex.facades.populators;

import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

/**
 * @author Created by konstantin.pavlyukov on 11/8/2017.
 */
public class ProductFeaturePopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends AbstractProductPopulator<SOURCE, TARGET> {
  @Override
  public void populate(final SOURCE source, final TARGET target) throws ConversionException {
    target.setFeatureDescription((String)getProductAttribute(source, ProductModel.FEATUREDESCRIPTION));
  }
}
