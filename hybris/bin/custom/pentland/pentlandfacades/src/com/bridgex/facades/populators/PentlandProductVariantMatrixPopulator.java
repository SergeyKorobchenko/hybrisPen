package com.bridgex.facades.populators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;

import com.bridgex.core.model.ApparelSizeVariantProductModel;
import com.bridgex.core.model.ApparelStyleVariantProductModel;

import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.*;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.variants.model.VariantProductModel;

/**
 * Created by dmitry.konovalov@masterdata.ru on 03.11.2017.
 */
public class PentlandProductVariantMatrixPopulator<SOURCE extends ProductModel, TARGET extends ProductData>
  extends AbstractProductPopulator<SOURCE, TARGET>
{
  @Override
  public void populate(ProductModel productModel, ProductData productData) throws ConversionException {
    final Collection<VariantProductModel> variants = productModel.getVariants();
    productData.setMultidimensional(Boolean.valueOf(CollectionUtils.isNotEmpty(variants)));
    ProductModel baseProductModel = getBaseProduct(productModel);
    VariantMatrixElementData rootElementData = createNode(baseProductModel);

    for (VariantProductModel styleLevelVariantModel : baseProductModel.getVariants()) {
      ApparelStyleVariantProductModel styleVariantProductModel = (ApparelStyleVariantProductModel) styleLevelVariantModel;
      VariantMatrixElementData styleElementData = createNode(styleVariantProductModel);
      rootElementData.getElements().add(styleElementData);

      for (VariantProductModel sizeLevelVariantModel : styleVariantProductModel.getVariants()) {
        ApparelSizeVariantProductModel sizeVariantProductModel = (ApparelSizeVariantProductModel) sizeLevelVariantModel;
        VariantMatrixElementData sizeElementData = createNode(sizeVariantProductModel);
        sizeElementData.setIsLeaf(Boolean.TRUE);
        styleElementData.getElements().add(sizeElementData);
      }
    }

    productData.setVariantMatrix(rootElementData.getElements());
  }

  protected ProductModel getBaseProduct(ProductModel productModel) {
    if (productModel instanceof ApparelSizeVariantProductModel) {
      return ((ApparelStyleVariantProductModel)((ApparelSizeVariantProductModel) productModel).getBaseProduct()).getBaseProduct();
    } else if (productModel instanceof ApparelStyleVariantProductModel) {
      return ((ApparelStyleVariantProductModel) productModel).getBaseProduct();
    } else if (productModel instanceof ProductModel) {
      return productModel;
    }
    return productModel;
  }

  protected VariantMatrixElementData createNode(ProductModel productModel) {
    VariantMatrixElementData elementData = new VariantMatrixElementData();
    VariantOptionData variantOptionData = newVariantOptionData();
    populateVariantOptionData(productModel, variantOptionData);
    elementData.setVariantOption(variantOptionData);
    elementData.setElements(new ArrayList<>());
    elementData.setIsLeaf(Boolean.FALSE);
    return elementData;
  }

  protected void populateVariantOptionData(ProductModel productModel, VariantOptionData variantOptionData) {
    variantOptionData.setCode(productModel.getCode());
  }

  protected VariantOptionData newVariantOptionData() {
    final VariantOptionData variantOptionData = new VariantOptionData();
    final VariantOptionQualifierData variantOptionQualifierData = new VariantOptionQualifierData();
    variantOptionQualifierData.setImage(new ImageData());
    variantOptionData.setVariantOptionQualifiers(Arrays.asList(new VariantOptionQualifierData[]
                                                                 { variantOptionQualifierData }));
    return variantOptionData;
  }

}
