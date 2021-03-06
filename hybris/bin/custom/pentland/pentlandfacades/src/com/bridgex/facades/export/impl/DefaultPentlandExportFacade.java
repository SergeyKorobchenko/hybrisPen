package com.bridgex.facades.export.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.model.ApparelSizeVariantProductModel;
import com.bridgex.core.product.PentlandProductService;
import com.bridgex.facades.export.PentlandExportFacade;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.core.model.media.MediaContainerModel;
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
  public static final String MEDIA_EXPORT_PATH = "media.replication.dirs";
  public static final String DEFAULT_MEDIA_ROOT = "http://pentland.local:9001";

  private PentlandProductService productService;

  @Override
  public void exportImagesFromCart(final ZipOutputStream zipOutputStream, final CartData cartData) {

    List<OrderEntryData> entries = cartData.getEntries();
    if(CollectionUtils.isNotEmpty(entries)){
      Set<String> products = collectProductCodesFromEntries(entries);
      exportImagesForProductList(zipOutputStream, products, false);
    }

  }

  @Override
  public void exportImagesForProductList(final ZipOutputStream zipOutputStream, Set<String> products, boolean onlyMaster){
    Set<MediaModel> collectedMedia;
    if(onlyMaster){
      collectedMedia = products.stream().map(this::getPrimaryImageMasterUrl).filter(Objects::nonNull).collect(Collectors.toSet());
    }else {
      collectedMedia = products.stream().flatMap(productCode -> getGalleryImages(productCode).stream()).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    String pathPrefix = Config.getString(MEDIA_EXPORT_PATH, "") + "/sys_master/";

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
//        BufferedImage image = ImageIO.read(new URL(urlPrefix + media.getDownloadURL()));
        BufferedImage image = ImageIO.read(new File(pathPrefix + media.getLocation()));
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

  private Set<String> collectProductCodesFromEntries(final List<OrderEntryData> entries) {
    Set<String> productCodes = new HashSet<>();
    for (final OrderEntryData entry : entries) {
      if (Boolean.TRUE.equals(entry.getProduct().getMultidimensional())) {
        productCodes.addAll(entry.getEntries().stream().filter(e -> e.getQuantity() > 0).map(subEntry -> subEntry.getProduct().getBaseProduct()).collect(Collectors.toList()));
      }
      else
      {
        productCodes.add(entry.getProduct().getCode());
      }
    }
    return productCodes;
  }

  private MediaModel getPrimaryImageMasterUrl(final String productCode) {
    try {
      ProductModel productForCode = productService.getProductForCode(productCode);
      if(productForCode instanceof ApparelSizeVariantProductModel){
        productForCode = ((ApparelSizeVariantProductModel) productForCode).getBaseProduct();
      }
      //relying on assumption that primary image master is always set to product.picture
      return productForCode.getPicture();
    }catch(UnknownIdentifierException | AmbiguousIdentifierException e){
      //shouldn't ever be possible, but...
      LOG.error("Unknown product code in cart: " + productCode);
      return null;
    }
  }

  private List<MediaModel> getGalleryImages(final String productCode){
    try {
      ProductModel productForCode = productService.getProductForCode(productCode);
      if(productForCode instanceof ApparelSizeVariantProductModel){
        productForCode = ((ApparelSizeVariantProductModel) productForCode).getBaseProduct();
      }
      //relying on assumption that primary image master is included in gallery images
      List<MediaContainerModel> galleryImages = productForCode.getGalleryImages();
      if(CollectionUtils.isNotEmpty(galleryImages)){
        return galleryImages.stream().map(MediaContainerModel::getMaster).collect(Collectors.toList());
      }
    }catch(UnknownIdentifierException | AmbiguousIdentifierException e){
      //shouldn't ever be possible, but...
      LOG.error("Unknown product code in cart: " + productCode);
    }
    return new ArrayList<>();
  }

  @Required
  public void setProductService(PentlandProductService productService) {
    this.productService = productService;
  }
}
