package com.bridgex.core.strategies.impl;

import de.hybris.platform.commerceservices.order.strategies.QuoteExpirationTimeValidationStrategy;
import de.hybris.platform.core.model.order.QuoteModel;

/**
 * @author Goncharenko Mikhail, created on 01.12.2017.
 */
public class PentlandQuoteExpirationTimeValidationStrategy implements QuoteExpirationTimeValidationStrategy {

  @Override
  public boolean hasQuoteExpired(QuoteModel quoteModel) {
    return false;
  }
}
