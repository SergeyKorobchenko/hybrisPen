package com.bridgex.facades.populators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.model.ApparelSizeVariantProductModel;

import de.hybris.platform.commercefacades.product.ImageFormatMapping;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.VariantOptionData;
import de.hybris.platform.commercefacades.product.data.VariantOptionQualifierData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.mediaconversion.enums.ConversionStatus;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.variants.model.VariantProductModel;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 12/22/2017.
 */
public class PentlandVariantOptionDataMediaPopulator<SOURCE extends VariantProductModel, TARGET extends VariantOptionData> implements Populator<SOURCE, TARGET> {

  private ImageFormatMapping acceleratorImageFormatMapping;
  private List<String> imageFormats;

  @Override
  public void populate(final VariantProductModel variantProductModel, final VariantOptionData variantOptionData)
    throws ConversionException
  {
//    VariantProductModel sourceProduct;
//    if(variantProductModel instanceof ApparelSizeVariantProductModel){
//      sourceProduct = (VariantProductModel) variantProductModel.getBaseProduct();
//    }else{
//      sourceProduct = variantProductModel;
//    }
    MediaModel picture = variantProductModel.getPicture();
    final Collection<VariantOptionQualifierData> qualifierDataList = new ArrayList<>();

    if (picture != null && picture.getMediaContainer() != null && ConversionStatus.CONVERTED.equals(picture.getMediaContainer().getConversionStatus())
        && "productConversionGroup".equals(picture.getMediaContainer().getConversionGroup().getCode())) {
      MediaContainerModel mediaContainer = picture.getMediaContainer();
      for(MediaModel mediaModel: mediaContainer.getMedias()){
        final ImageData imageData = new ImageData();
        imageData.setUrl(mediaModel.getURL());
        imageData.setFormat(getMediaFormat(mediaModel.getMediaFormat().getQualifier()));

        final VariantOptionQualifierData qualifierData = new VariantOptionQualifierData();
        qualifierData.setImage(imageData);

        qualifierDataList.add(qualifierData);

        variantOptionData.setVariantOptionQualifiers(qualifierDataList);
      }
    }else{
      //OOTB
      for (final Iterator<MediaModel> mediaModelIter = variantProductModel.getOthers().iterator(); mediaModelIter.hasNext();)
      {
        final MediaModel mediaModel = mediaModelIter.next();

        final ImageData imageData = new ImageData();
        imageData.setUrl(mediaModel.getURL());
        imageData.setFormat(getMediaFormat(mediaModel.getMediaFormat().getQualifier()));

        final VariantOptionQualifierData qualifierData = new VariantOptionQualifierData();
        qualifierData.setImage(imageData);

        qualifierDataList.add(qualifierData);

        variantOptionData.setVariantOptionQualifiers(qualifierDataList);

      }
    }
  }

  protected String getMediaFormat(final String format)
  {
    for (final String imageFormat : imageFormats)
    {
      if (format.equals(getAcceleratorImageFormatMapping().getMediaFormatQualifierForImageFormat(imageFormat)))
      {
        return imageFormat;
      }
    }
    return null;
  }

  protected List<String> getImageFormats()
  {
    return imageFormats;
  }

  @Required
  public void setImageFormats(final List<String> imageFormats)
  {
    this.imageFormats = imageFormats;
  }

  protected ImageFormatMapping getAcceleratorImageFormatMapping()
  {
    return acceleratorImageFormatMapping;
  }

  @Required
  public void setAcceleratorImageFormatMapping(final ImageFormatMapping acceleratorImageFormatMapping)
  {
    this.acceleratorImageFormatMapping = acceleratorImageFormatMapping;
  }
}
