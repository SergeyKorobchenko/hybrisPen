package com.bridgex.core.strategies.impl;

import de.hybris.platform.commerceservices.strategies.impl.DefaultCartValidationStrategy;
import de.hybris.platform.core.model.order.CartEntryModel;
// This turns off stock validation for cart entry
public class PentlandCartValidationStrategy extends DefaultCartValidationStrategy {

  @Override
  protected Long getStockLevel(final CartEntryModel cartEntryModel)
  {
    return null;
  }
}
