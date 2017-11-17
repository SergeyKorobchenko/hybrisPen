package com.bridgex.facades.populators;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.model.ApparelSizeVariantProductModel;
import com.bridgex.core.model.ApparelStyleVariantProductModel;
import com.bridgex.core.product.util.ProductSizeComparator;

import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.*;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.variants.model.VariantProductModel;

/**
 * Created by dmitry.konovalov@masterdata.ru on 03.11.2017.
 */
public class PentlandProductVariantMatrixPopulator<SOURCE extends ProductModel, TARGET extends ProductData>
  extends AbstractProductPopulator<SOURCE, TARGET>
{

  private Populator<VariantProductModel, VariantOptionData> variantOptionDataMediaPopulator;

  @Override
  public void populate(ProductModel productModel, ProductData productData) throws ConversionException {
    final Collection<VariantProductModel> variants = productModel.getVariants();
    productData.setMultidimensional(Boolean.valueOf(CollectionUtils.isNotEmpty(variants)));
    ProductModel baseProductModel = getBaseProduct(productModel);
    List<VariantMatrixElementData> variantMatrixElementDataList = new ArrayList<>();

    int maxVariants = 0;

    for (VariantProductModel styleLevelVariantModel : baseProductModel.getVariants()) {
      ApparelStyleVariantProductModel styleVariantProductModel = (ApparelStyleVariantProductModel) styleLevelVariantModel;
      VariantMatrixElementData styleElementData = createNode(styleVariantProductModel);
      styleElementData.setVariantName(styleVariantProductModel.getStyle());
      styleElementData.setIsLeaf(Boolean.FALSE);
      variantMatrixElementDataList.add(styleElementData);


      List<ApparelSizeVariantProductModel> sizeLevelVariants = styleVariantProductModel.getVariants().stream().map(e -> (ApparelSizeVariantProductModel)e)
                                                                                       .sorted(new ProductSizeComparator()).collect(Collectors.toList());

      for (ApparelSizeVariantProductModel sizeVariantProductModel : sizeLevelVariants) {
        VariantMatrixElementData sizeElementData = createNode(sizeVariantProductModel);
        sizeElementData.setVariantName(sizeVariantProductModel.getSize());
        sizeElementData.setIsLeaf(Boolean.TRUE);
        styleElementData.getElements().add(sizeElementData);
      }
      if (maxVariants < styleElementData.getElements().size()) {
        maxVariants = styleElementData.getElements().size();
      }
    }
    for (VariantMatrixElementData variantMatrixElementData: variantMatrixElementDataList) {
      variantMatrixElementData.setMaxVariants(maxVariants);
    }
    VariantMatrixElementData root = createNode(baseProductModel);
    root.setElements(variantMatrixElementDataList);
    root.setIsLeaf(Boolean.FALSE);
    productData.setVariantMatrix(Collections.singletonList(root));
  }

  protected ProductModel getBaseProduct(ProductModel productModel) {
    ProductModel currentProduct = productModel;
    while (currentProduct instanceof VariantProductModel) {
      currentProduct = ((VariantProductModel) currentProduct).getBaseProduct();
    }
    return currentProduct;
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
    if (productModel instanceof VariantProductModel) {
      getVariantOptionDataMediaPopulator().populate((VariantProductModel) productModel, variantOptionData);
    }
  }

  protected VariantOptionData newVariantOptionData() {
    final VariantOptionData variantOptionData = new VariantOptionData();
    final VariantOptionQualifierData variantOptionQualifierData = new VariantOptionQualifierData();
    variantOptionQualifierData.setImage(new ImageData());
    variantOptionData.setVariantOptionQualifiers(Arrays.asList(new VariantOptionQualifierData[]
                                                                 { variantOptionQualifierData }));
    return variantOptionData;
  }

  protected Populator<VariantProductModel, VariantOptionData> getVariantOptionDataMediaPopulator() {
    return variantOptionDataMediaPopulator;
  }

  @Required
  public void setVariantOptionDataMediaPopulator(Populator<VariantProductModel, VariantOptionData> variantOptionDataMediaPopulator) {
    this.variantOptionDataMediaPopulator = variantOptionDataMediaPopulator;
  }
}
