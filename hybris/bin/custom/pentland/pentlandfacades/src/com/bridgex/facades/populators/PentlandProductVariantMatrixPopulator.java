package com.bridgex.facades.populators;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.category.PentlandCategoryService;
import com.bridgex.core.enums.DiscontinuedStatus;
import com.bridgex.core.model.ApparelSizeVariantProductModel;
import com.bridgex.core.model.ApparelStyleVariantProductModel;
import com.bridgex.core.product.util.ProductSizeComparator;

import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.*;
import de.hybris.platform.commerceservices.price.CommercePriceService;
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

  private Set<DiscontinuedStatus> onlineStatusSet;

  private Populator<VariantProductModel, VariantOptionData> variantOptionDataMediaPopulator;
  private CommercePriceService                              commercePriceService;

  @Override
  public void populate(ProductModel productModel, ProductData productData) throws ConversionException {
    final Collection<VariantProductModel> variants = productModel.getVariants();
    productData.setMultidimensional(CollectionUtils.isNotEmpty(variants));
    ProductModel baseProductModel = getBaseProduct(productModel);
    List<VariantMatrixElementData> variantMatrixElementDataList = new ArrayList<>();

    int maxVariants = 0;

    for (VariantProductModel styleLevelVariantModel : baseProductModel.getVariants()) {
      if (isOnlineProduct(styleLevelVariantModel) && hasPrice(styleLevelVariantModel)) {
        ApparelStyleVariantProductModel styleVariantProductModel = (ApparelStyleVariantProductModel) styleLevelVariantModel;
        VariantMatrixElementData styleElementData = createNode(styleVariantProductModel);
        styleElementData.setVariantName(styleVariantProductModel.getStyle());
        styleElementData.setVariantCode(styleVariantProductModel.getCode());
        styleElementData.setIsLeaf(Boolean.FALSE);
        styleElementData.setPackSize(styleVariantProductModel.getPackSize());
        final VariantOptionData styleVariantOptionData = Optional.ofNullable(styleElementData.getVariantOption()).orElse(new VariantOptionData());
        styleVariantOptionData.setCode(styleLevelVariantModel.getCode());
        styleVariantOptionData.setBrandCode(styleLevelVariantModel.getSapBrand());
        styleElementData.setVariantOption(styleVariantOptionData);

        List<ApparelSizeVariantProductModel> sizeLevelVariants =
          styleVariantProductModel.getVariants().stream().filter(this::isOnlineProduct).map(e -> (ApparelSizeVariantProductModel) e).sorted(new ProductSizeComparator()).collect(Collectors.toList());

        for (ApparelSizeVariantProductModel sizeVariantProductModel : sizeLevelVariants) {
          VariantMatrixElementData sizeElementData = createNode(sizeVariantProductModel);
          sizeElementData.setVariantName(sizeVariantProductModel.getSize());
          sizeElementData.setIsLeaf(Boolean.TRUE);
          sizeElementData.setPackSize(styleVariantProductModel.getPackSize());
          final VariantOptionData sizeVariantOptionData = Optional.ofNullable(sizeElementData.getVariantOption()).orElse(new VariantOptionData());
          sizeVariantOptionData.setCode(sizeVariantProductModel.getCode());
          sizeElementData.setVariantOption(sizeVariantOptionData);
          styleElementData.getElements().add(sizeElementData);
        }
        if (maxVariants < styleElementData.getElements().size()) {
          maxVariants = styleElementData.getElements().size();
        }

        if(CollectionUtils.isNotEmpty(sizeLevelVariants)) {
          variantMatrixElementDataList.add(styleElementData);
        }

      }
      for (VariantMatrixElementData variantMatrixElementData : variantMatrixElementDataList) {
        variantMatrixElementData.setMaxVariants(maxVariants);
      }
      VariantMatrixElementData root = createNode(baseProductModel);
      root.setElements(variantMatrixElementDataList);
      root.setIsLeaf(Boolean.FALSE);
      productData.setVariantMatrix(Collections.singletonList(root));
    }
  }

  private boolean hasPrice(VariantProductModel styleLevelVariantModel) {
    return commercePriceService.getWebPriceForProduct(styleLevelVariantModel) != null;
  }

  private boolean isOnlineProduct(ProductModel productModel) {
    return productModel.getDiscontinuedStatus() == null || onlineStatusSet.contains(productModel.getDiscontinuedStatus());
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

  @Required
  public void setCommercePriceService(CommercePriceService commercePriceService) {
    this.commercePriceService = commercePriceService;
  }

  @Required
  public void setOnlineStatusSet(Set<DiscontinuedStatus> onlineStatusSet) {
    this.onlineStatusSet = onlineStatusSet;
  }
}
