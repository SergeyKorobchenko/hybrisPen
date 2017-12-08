package com.bridgex.facades.populators;

import com.bridgex.core.enums.DiscontinuedStatus;
import com.bridgex.core.model.ApparelSizeVariantProductModel;
import com.bridgex.facades.utils.ProductUtils;

import de.hybris.platform.commercefacades.product.converters.populator.ProductBasicPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.variants.model.VariantProductModel;

/**
 * @author Created by konstantin.pavlyukov on 11/7/2017.
 */
public class PentlandProductBasicPopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends ProductBasicPopulator<SOURCE, TARGET> {

  @Override
  public void populate(final SOURCE productModel, final TARGET productData) throws ConversionException
  {
    if (productModel.getVariantType() != null)
    {
      productData.setVariantType(productModel.getVariantType().getCode());
    }
    if (productModel instanceof VariantProductModel)
    {
      final VariantProductModel variantProduct = (VariantProductModel) productModel;
      productData.setBaseProduct(variantProduct.getBaseProduct() != null ? variantProduct.getBaseProduct().getCode() : null);
    }
    if (productModel instanceof ApparelSizeVariantProductModel) {
      productData.setName(((ApparelSizeVariantProductModel) productModel).getBaseProduct().getName());
    } else {
      productData.setName((String) getProductAttribute(productModel, ProductModel.NAME));
    }
    productData.setPurchasable(isApproved(productModel) && ProductUtils.isNotDiscontinued((DiscontinuedStatus)getProductAttribute(productModel, ProductModel.DISCONTINUEDSTATUS)));
  }
}
