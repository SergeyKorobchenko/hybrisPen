package com.bridgex.core.price.dao.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.bridgex.core.price.dao.PentlandPriceDao;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/23/2017.
 */
public class PentlandDefaultPriceDao extends AbstractItemDao implements PentlandPriceDao{
  @Override
  public PriceRowModel findMaxPriceRowForProductAndCurrency(ProductModel productModel, CurrencyModel currencyModel) {
    StringBuilder query = new StringBuilder();
    query.append("select {PK} from {").append(PriceRowModel._TYPECODE).append("} where ");

    query.append("({").append(PriceRowModel.PRODUCTMATCHQUALIFIER).append("}=?productPK ");
    query.append("or {").append(PriceRowModel.PRODUCTID).append("}=?productID) ");
    query.append("and {").append(PriceRowModel.CURRENCY).append("}=?").append(currencyModel);

    query.append(" order by {").append(PriceRowModel.PRICE).append("} DESC");

    FlexibleSearchQuery fsq = new FlexibleSearchQuery(query.toString());
    fsq.addQueryParameter("productPK", productModel.getPk());
    fsq.addQueryParameter("productID", productModel.getCode());
    fsq.addQueryParameter("currency", currencyModel);
    fsq.setCount(1);

    List<PriceRowModel> result = getFlexibleSearchService().<PriceRowModel>search(fsq).getResult();

    if(CollectionUtils.isNotEmpty(result)){
      return result.get(0);
    }

    return null;
  }
}
