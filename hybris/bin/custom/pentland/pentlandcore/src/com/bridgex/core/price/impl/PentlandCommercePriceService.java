package com.bridgex.core.price.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.bridgex.core.model.ApparelSizeVariantProductModel;

import de.hybris.platform.commerceservices.price.impl.DefaultCommercePriceService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.PriceInformation;

/**
 * @author Created by konstantin.pavlyukov on 10/23/2017.
 */
public class PentlandCommercePriceService extends DefaultCommercePriceService {
  @Override
  public PriceInformation getWebPriceForProduct(final ProductModel product)
  {
    validateParameterNotNull(product, "Product model cannot be null");
    ProductModel result = product;
    if (product instanceof ApparelSizeVariantProductModel) {
      result = ((ApparelSizeVariantProductModel)product).getBaseProduct();
    }
    final List<PriceInformation> prices = getPriceService().getPriceInformationsForProduct(result);
    if (CollectionUtils.isNotEmpty(prices))
    {
      PriceInformation minPriceForLowestQuantity = null;
      for (final PriceInformation price : prices)
      {
        if (minPriceForLowestQuantity == null
            || (((Long) minPriceForLowestQuantity.getQualifierValue("minqtd")).longValue() > ((Long) price
          .getQualifierValue("minqtd")).longValue()))
        {
          minPriceForLowestQuantity = price;
        }
      }
      return minPriceForLowestQuantity;
    }
    return null;
  }
}
