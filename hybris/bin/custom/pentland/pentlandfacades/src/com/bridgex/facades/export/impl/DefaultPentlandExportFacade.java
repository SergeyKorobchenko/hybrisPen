package com.bridgex.facades.export.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.product.PentlandProductService;
import com.bridgex.facades.export.PentlandExportFacade;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.util.Config;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/13/2017.
 */
public class DefaultPentlandExportFacade implements PentlandExportFacade{

  private static final Logger LOG = Logger.getLogger(DefaultPentlandExportFacade.class);
  public static final String MEDIA_EXPORT_HTTP = "media.export.http";
  public static final String DEFAULT_MEDIA_ROOT = "http://pentland.local:9001";

  private PentlandProductService productService;

  @Override
  public void exportImagesFromCart(final ZipOutputStream zipOutputStream, final CartData cartData) {

    List<OrderEntryData> entries = cartData.getEntries();
    if(CollectionUtils.isNotEmpty(entries)){
      //TODO collect images from style-level entries
      Set<String> products = entries.stream().map(entry -> entry.getProduct().getCode()).collect(Collectors.toSet());
      exportImagesForProductList(zipOutputStream, products);
    }

  }

  @Override
  public void exportImagesForProductList(final ZipOutputStream zipOutputStream, Set<String> products){
    Set<MediaModel> collectedMedia = products.stream().map(this::getPrimaryImageMasterUrl).collect(Collectors.toSet());

    String urlPrefix = Config.getString(MEDIA_EXPORT_HTTP, DEFAULT_MEDIA_ROOT);

    for(MediaModel media: collectedMedia){
      //for the images imported via hotfolder the realfilename is always filled. However, it still can be null for initialdata images or for any other reason
      String fileName = StringUtils.isNotEmpty(media.getRealFileName()) ? media.getRealFileName() : media.getCode();
      if(fileName.contains("/")){
        //again, in case of initialdata there's a '/' symbol in media code that will be interpreted as folder separator if we don't get rid of it
        String[] splitFileName = fileName.split("/");
        fileName = splitFileName[splitFileName.length - 1];
      }
      ZipEntry zipEntry = new ZipEntry(fileName);
      try{
        zipOutputStream.putNextEntry(zipEntry);
        BufferedImage image = ImageIO.read(new URL(urlPrefix + media.getDownloadURL()));
        String imageFormat = "jpg";
        //try to get image format from filename
        String[] split = fileName.split("\\.");
        if(split.length > 1){
          imageFormat = split[split.length-1];
        }

        ImageIO.write(image, imageFormat, zipOutputStream);
        zipOutputStream.closeEntry();
      }
      catch (IOException e) {
        LOG.error("Failed to put image into export zip: " + media.getCode(), e);
      }
    }

  }

  private MediaModel getPrimaryImageMasterUrl(final String productCode) {
    try {
      ProductModel productForCode = productService.getProductForCode(productCode);
      //relying on assumption that primary image master is always set to product.picture
      return productForCode.getPicture();
    }catch(UnknownIdentifierException | AmbiguousIdentifierException e){
      //shouldn't ever be possible, but...
      LOG.error("Unknown product code in cart: " + productCode);
      return null;
    }
  }

  @Required
  public void setProductService(PentlandProductService productService) {
    this.productService = productService;
  }
}
