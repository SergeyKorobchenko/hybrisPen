package com.bridgex.core.price.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.model.ApparelSizeVariantProductModel;
import com.bridgex.core.price.PentlandCommercePriceService;
import com.bridgex.core.price.dao.PentlandPriceDao;

import de.hybris.platform.commerceservices.price.CommercePriceService;
import de.hybris.platform.commerceservices.price.impl.DefaultCommercePriceService;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.jalo.order.price.PriceInformation;

/**
 * @author Created by konstantin.pavlyukov on 10/23/2017.
 */
public class PentlandDefaultCommercePriceService extends DefaultCommercePriceService implements PentlandCommercePriceService{

  private PentlandPriceDao pentlandPriceDao;

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

  @Override
  public PriceRowModel findMaxPriceRowForProductAndCurrency(ProductModel productModel, CurrencyModel currencyModel) {
    return pentlandPriceDao.findMaxPriceRowForProductAndCurrency(productModel, currencyModel);
  }

  @Required
  public void setPentlandPriceDao(PentlandPriceDao pentlandPriceDao) {
    this.pentlandPriceDao = pentlandPriceDao;
  }
}
