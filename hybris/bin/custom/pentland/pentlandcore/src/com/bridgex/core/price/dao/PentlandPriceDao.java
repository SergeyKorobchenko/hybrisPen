package com.bridgex.core.price.dao;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/23/2017.
 */
public interface PentlandPriceDao {

  PriceRowModel findMaxPriceRowForProductAndCurrency(ProductModel productModel, CurrencyModel currencyModel);
}
