package com.bridgex.core.product.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.bridgex.core.product.PentlandProductService;
import com.bridgex.core.product.dao.PentlandProductDao;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.mediaconversion.MediaConversionService;
import de.hybris.platform.mediaconversion.enums.ConversionStatus;
import de.hybris.platform.product.impl.DefaultProductService;
import de.hybris.platform.variants.model.VariantProductModel;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/17/2017.
 */
public class DefaultPentlandProductService extends DefaultProductService implements PentlandProductService {

  private static final String PRIMARY_CONVERSION_GROUP = "productConversionGroup";

  private PentlandProductDao     pentlandProductDao;
  private MediaConversionService mediaConversionService;

  @Override
  public List<ProductModel> findSMUProductsForSapBrandAndCatalogVersion(String sapBrand, CatalogVersionModel catalogVersion) {
    return pentlandProductDao.findProductsForSapBrandAndCatalogVersion(sapBrand, catalogVersion, ProductModel.SMU, true);
  }

  @Override
  public List<ProductModel> findClearanceProductsForSapBrandAndCatalogVersion(String sapBrand, CatalogVersionModel catalogVersion) {
    return pentlandProductDao.findProductsForSapBrandAndCatalogVersion(sapBrand, catalogVersion, ProductModel.CLEARANCE, true);
  }

  @Override
  public boolean convertAssignedMedia(ProductModel product, String mediaContainerCode) {
    Assert.notNull(mediaContainerCode, "mediaContainerCode must not be null.");
    List<MediaContainerModel> galleryImages = new ArrayList<>(product.getGalleryImages());
    if(CollectionUtils.isEmpty(galleryImages)) {
      return true;
    } else{
      //gallery collection is of type list and doesn't check for multiple additions of the same container when impex is in append mode
      //so the same container could be added multiple times
      //important to remember: at this point the container has already been assigned to the product if everything went well with the impex
      List<MediaContainerModel> matchingContainers = galleryImages.stream().filter(mediaContainer ->
                                                                                     mediaContainerCode.equals(mediaContainer.getQualifier())).collect(Collectors.toList());
      if(CollectionUtils.isNotEmpty(matchingContainers)){
        MediaContainerModel updatedContainer = matchingContainers.get(0);
        if(matchingContainers.size() > 1){
          //found some duplicates, remove all but one - doesn't matter which, they're the same
          galleryImages.removeAll(matchingContainers);
          galleryImages.add(updatedContainer);
          product.setGalleryImages(galleryImages);
          getModelService().save(product);
        }
        if(updatedContainer.getConversionGroup() != null && ConversionStatus.EMPTY.equals(updatedContainer.getConversionStatus())){
          //a different master was added alongside the old one, there can only be one per container
          mediaConversionService.deleteConvertedMedias(updatedContainer);
          List<MediaModel> masterMedias = new ArrayList<>(updatedContainer.getMedias());
          if(CollectionUtils.isEmpty(masterMedias)){
            //container is empty
            getModelService().remove(updatedContainer);
            return false;
          }
          masterMedias.sort((m1, m2) -> m2.getModifiedtime().compareTo(m1.getModifiedtime()));
          updatedContainer.setMedias(Collections.singletonList(masterMedias.get(0)));
          getModelService().save(updatedContainer);
        }
        if(updatedContainer.getConversionGroup() != null
           && (ConversionStatus.PARTIALLY_CONVERTED.equals(updatedContainer.getConversionStatus()) || ConversionStatus.CONVERTED.equals(updatedContainer.getConversionStatus()))){
          //a new image with the same name is being imported and/or conversion group has changed, so we need to re-convert
          mediaConversionService.deleteConvertedMedias(updatedContainer);
        }
        mediaConversionService.convertMedias(updatedContainer);
        if(PRIMARY_CONVERSION_GROUP.equals(updatedContainer.getConversionGroup().getCode())){
          product.setPicture(updatedContainer.getMaster());
          getModelService().save(product);
        }
        return true;
      }
    }
    return false;
  }

  /**
   * Get an attribute value from a product.
   * If the attribute value is null and the product is a variant then the same attribute will be
   * requested from the base product.
   *
   * @param productModel the product
   * @param attribute    the name of the attribute to lookup
   *
   * @return the value of the attribute
   */
  public Object getProductAttribute(final ProductModel productModel, final String attribute)
  {
    final Object value = getModelService().getAttributeValue(productModel, attribute);
    if (value == null && productModel instanceof VariantProductModel) {
      final ProductModel baseProduct = ((VariantProductModel) productModel).getBaseProduct();
      if (baseProduct != null) {
        return getProductAttribute(baseProduct, attribute);
      }
    }
    return value;
  }


  @Required
  public void setPentlandProductDao(PentlandProductDao pentlandProductDao) {
    this.pentlandProductDao = pentlandProductDao;
  }

  @Required
  public void setMediaConversionService(MediaConversionService mediaConversionService) {
    this.mediaConversionService = mediaConversionService;
  }
}
