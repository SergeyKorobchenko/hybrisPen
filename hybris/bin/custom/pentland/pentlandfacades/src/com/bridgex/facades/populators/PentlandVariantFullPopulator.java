package com.bridgex.facades.populators;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.enums.DiscontinuedStatus;
import com.bridgex.core.model.ApparelStyleVariantProductModel;

import de.hybris.platform.commercefacades.product.data.BaseOptionData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.VariantOptionData;
import de.hybris.platform.commerceservices.price.CommercePriceService;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.variants.model.VariantProductModel;

/**
 * @author Created by ekaterina.agievich@masterdata.ru on 2/1/2018.
 */
public class PentlandVariantFullPopulator<SOURCE extends ProductModel, TARGET extends ProductData> implements Populator<SOURCE, TARGET> {

  private static final Set<DiscontinuedStatus> onlineStatusSet = new HashSet<>(Arrays.asList(DiscontinuedStatus.D03, DiscontinuedStatus.D04, DiscontinuedStatus.D05));

  private  Converter<VariantProductModel, VariantOptionData> variantOptionDataConverter;
  private  Converter<VariantProductModel, BaseOptionData>    baseOptionDataConverter;
  private CommercePriceService                              commercePriceService;

  @Override
  public void populate(final SOURCE productModel, final TARGET productData) throws ConversionException {
    // Populate the list of available child variant options
    if (productModel.getVariantType() != null && CollectionUtils.isNotEmpty(productModel.getVariants())) {
      final List<VariantOptionData> variantOptions =
        productModel.getVariants().stream().filter(this::isOnlineProduct).map(variantProductModel -> getVariantOptionDataConverter().convert(variantProductModel)).collect(Collectors.toList());
      productData.setVariantOptions(variantOptions);
    }

    // Populate the list of base options
    final List<BaseOptionData> baseOptions = new ArrayList<>();
    ProductModel currentProduct = productModel;

    while (currentProduct instanceof VariantProductModel) {
      final ProductModel baseProduct = ((VariantProductModel) currentProduct).getBaseProduct();

      final BaseOptionData baseOptionData = getBaseOptionDataConverter().convert((VariantProductModel) currentProduct);

      // Fill out the list of available product options

      Collection<VariantProductModel> variants = baseProduct.getVariants().stream().filter(this::isOnlineProduct).collect(Collectors.toList());
      if(CollectionUtils.isNotEmpty(variants)) {
        List<VariantOptionData> options = Converters.convertAll(variants, getVariantOptionDataConverter());
        baseOptionData.setOptions(options);
      }

      baseOptions.add(baseOptionData);
      currentProduct = baseProduct;
    }
    productData.setBaseOptions(baseOptions);
  }

  private boolean hasPrice(VariantProductModel styleLevelVariantModel) {
    return commercePriceService.getWebPriceForProduct(styleLevelVariantModel) != null;
  }

  private boolean isOnlineProduct(ProductModel productModel) {
    if(productModel.getDiscontinuedStatus() == null || onlineStatusSet.contains(productModel.getDiscontinuedStatus())){
      if(productModel instanceof ApparelStyleVariantProductModel){
        ApparelStyleVariantProductModel style = (ApparelStyleVariantProductModel)productModel;
        long activeSizes = style.getVariants().stream().filter(size -> (size.getDiscontinuedStatus() == null || onlineStatusSet.contains(size.getDiscontinuedStatus()))).count();
        if(activeSizes == 0){
          return false;
        }else{
          return true;
        }
      }else{
        return true;
      }
    }
    return false;
  }


  protected Converter<VariantProductModel, VariantOptionData> getVariantOptionDataConverter()
  {
    return variantOptionDataConverter;
  }

  @Required
  public void setVariantOptionDataConverter(final Converter<VariantProductModel, VariantOptionData> variantOptionDataConverter)
  {
    this.variantOptionDataConverter = variantOptionDataConverter;
  }

  protected Converter<VariantProductModel, BaseOptionData> getBaseOptionDataConverter()
  {
    return baseOptionDataConverter;
  }

  @Required
  public void setBaseOptionDataConverter(final Converter<VariantProductModel, BaseOptionData> baseOptionDataConverter)
  {
    this.baseOptionDataConverter = baseOptionDataConverter;
  }

  public void setCommercePriceService(CommercePriceService commercePriceService) {
    this.commercePriceService = commercePriceService;
  }
}
