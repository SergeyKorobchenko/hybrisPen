package com.bridgex.facades.populators;

import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.model.ApparelSizeVariantProductModel;

import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

/**
 * @author Created by konstantin.pavlyukov on 11/8/2017.
 */
public class ProductPDPPopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends AbstractProductPopulator<SOURCE, TARGET> {

  private Converter<MediaModel, ImageData> imageConverter;

  @Override
  public void populate(final SOURCE source, final TARGET target) throws ConversionException {
    if (source instanceof ApparelSizeVariantProductModel) {
      target.setMaterialKey(((ApparelSizeVariantProductModel) source).getBaseProduct().getCode());
    }

    target.setVideoURL((String) getProductAttribute(source, ProductModel.VIDEOURL));
    target.setExternalURL((String) getProductAttribute(source, ProductModel.EXTERNALURL));
    target.setSeason((String) getProductAttribute(source, ProductModel.SEASON));
    UnitModel unit = (UnitModel)getProductAttribute(source, ProductModel.UNIT);
    if(unit != null) {
      target.setUnit(unit.getName());
    }
    target.setMoq((int) getProductAttribute(source, ProductModel.PACKSIZE));

    MediaModel sizeChart = (MediaModel) getProductAttribute(source, ProductModel.SIZECHARTIMAGE);

    if (sizeChart != null) {
      target.setSizeChartGuide(getImageConverter().convert(sizeChart));
    }
  }

  public Converter<MediaModel, ImageData> getImageConverter() {
    return imageConverter;
  }

  @Required
  public void setImageConverter(Converter<MediaModel, ImageData> imageConverter) {
    this.imageConverter = imageConverter;
  }
}
