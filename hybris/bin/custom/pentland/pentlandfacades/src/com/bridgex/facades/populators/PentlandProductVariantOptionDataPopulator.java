package com.bridgex.facades.populators;

import java.util.Collection;

import de.hybris.platform.commercefacades.product.converters.populator.ProductVariantOptionDataPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.variants.model.VariantProductModel;

/**
 * @author Created by dmitry.konovalov@masterdata.ru on 08.11.2017.
 */
public class PentlandProductVariantOptionDataPopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends ProductVariantOptionDataPopulator<SOURCE, TARGET> {

  @Override
  public void populate(final ProductModel productModel, final ProductData productData) throws ConversionException
  {
    final Collection<VariantProductModel> variants = getVariants(productModel);
    for (final VariantProductModel variant : variants)
    {
      populateNodes(productData.getVariantMatrix(), variant, productModel);
    }
  }
}
