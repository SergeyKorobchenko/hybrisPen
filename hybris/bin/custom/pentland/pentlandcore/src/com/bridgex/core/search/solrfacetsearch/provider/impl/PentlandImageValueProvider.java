package com.bridgex.core.search.solrfacetsearch.provider.impl;

import java.util.List;

import de.hybris.platform.commerceservices.search.solrfacetsearch.provider.impl.ImageValueProvider;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.variants.model.VariantProductModel;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 12/6/2017.
 */
public class PentlandImageValueProvider extends ImageValueProvider{

  protected MediaModel findMedia(final ProductModel product, final MediaFormatModel mediaFormat) {
    if (product != null && mediaFormat != null) {
      //try primary image first
      MediaModel picture = product.getPicture();
      if(picture != null){
        MediaContainerModel mediaContainer = picture.getMediaContainer();
        if(mediaContainer != null){
          try {
            final MediaModel media = getMediaContainerService().getMediaForFormat(mediaContainer, mediaFormat);
            if (media != null) {
              return media;
            }
          }catch (final ModelNotFoundException ignore) {//NOPMD
            // ignore
          }
        }
      }

      final List<MediaContainerModel> galleryImages = product.getGalleryImages();
      if (galleryImages != null && !galleryImages.isEmpty()) {
        // Search each media container in the gallery for an image of the right format
        for (final MediaContainerModel container : galleryImages) {
          try {
            final MediaModel media = getMediaContainerService().getMediaForFormat(container, mediaFormat);
            if (media != null) {
              return media;
            }
          }catch (final ModelNotFoundException ignore) {//NOPMD
            // ignore
          }
        }
      }

      // Failed to find media in product
      if (product instanceof VariantProductModel) {
        // Look in the base product
        return findMedia(((VariantProductModel) product).getBaseProduct(), mediaFormat);
      }
    }
    return null;
  }
}
