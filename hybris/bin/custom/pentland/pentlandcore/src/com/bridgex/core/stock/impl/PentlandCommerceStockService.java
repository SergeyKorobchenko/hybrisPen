package com.bridgex.core.stock.impl;

import de.hybris.platform.commerceservices.stock.impl.DefaultCommerceStockService;
import de.hybris.platform.store.BaseStoreModel;

public class PentlandCommerceStockService extends DefaultCommerceStockService {

  @Override
  public boolean isStockSystemEnabled(final BaseStoreModel baseStore)
  {
    return false;
  }
}
