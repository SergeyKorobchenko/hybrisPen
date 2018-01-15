package com.bridgex.facades.export;

import java.util.Set;
import java.util.zip.ZipOutputStream;

import de.hybris.platform.commercefacades.order.data.CartData;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/13/2017.
 */
public interface PentlandExportFacade {

  void exportImagesFromCart(final ZipOutputStream zipOutputStream, final CartData cartData);

  void exportImagesForProductList(final ZipOutputStream zipOutputStream, Set<String> products, boolean onlyMaster);
}
