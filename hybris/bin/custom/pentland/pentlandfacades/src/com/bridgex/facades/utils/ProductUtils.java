package com.bridgex.facades.utils;

import com.bridgex.core.enums.DiscontinuedStatus;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.variants.model.VariantProductModel;

/**
 * @author Created by konstantin.pavlyukov on 11/7/2017.
 */
public class ProductUtils {

  public static boolean isNotDiscontinued(DiscontinuedStatus status) {
    return (null != status && (DiscontinuedStatus.D03.equals(status) || DiscontinuedStatus.D04.equals(status) || DiscontinuedStatus.D05.equals(status)) );
  }

  public static boolean isNotDiscontinued(ProductModel product) {
    DiscontinuedStatus status = product.getDiscontinuedStatus();
    if (status == null && product instanceof VariantProductModel) {
      status = (((VariantProductModel) product).getBaseProduct()).getDiscontinuedStatus();
    }
    return (null != status && (DiscontinuedStatus.D03.equals(status) || DiscontinuedStatus.D04.equals(status) || DiscontinuedStatus.D05.equals(status)) );
  }

}
