package com.bridgex.core.price;

import de.hybris.platform.commerceservices.price.CommercePriceService;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/23/2017.
 */
public interface PentlandCommercePriceService extends CommercePriceService{

  /**
   * Retrieve max price to use in price range facet
   * @param productModel
   * @param currencyModel
   * @return
   */
  PriceRowModel findMaxPriceRowForProductAndCurrency(ProductModel productModel, CurrencyModel currencyModel);
}
