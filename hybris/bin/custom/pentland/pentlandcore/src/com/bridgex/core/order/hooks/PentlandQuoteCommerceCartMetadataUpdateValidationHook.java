package com.bridgex.core.order.hooks;

import java.util.Date;
import java.util.Optional;

import de.hybris.platform.commerceservices.enums.QuoteUserType;
import de.hybris.platform.commerceservices.order.hook.impl.QuoteCommerceCartMetadataUpdateValidationHook;
import de.hybris.platform.commerceservices.service.data.CommerceCartMetadataParameter;

/**
 * @author Goncharenko Mikhail, created on 05.12.2017.
 */
public class PentlandQuoteCommerceCartMetadataUpdateValidationHook extends QuoteCommerceCartMetadataUpdateValidationHook {

  @Override
  protected void validateParameter(CommerceCartMetadataParameter parameter, QuoteUserType quoteUserType) {
    final Optional<Date> optionalExpirationTime = parameter.getExpirationTime();
    if (QuoteUserType.BUYER.equals(quoteUserType)) {
      if (optionalExpirationTime.isPresent() || parameter.isRemoveExpirationTime()) {
        throw new IllegalArgumentException("Buyer is not allowed to modify expiration time of a cart created from a quote.");
      }
    } else if (!QuoteUserType.SELLER.equals(quoteUserType)) {
      throw new IllegalArgumentException("Unknown quote user type.");
    }
  }

}
