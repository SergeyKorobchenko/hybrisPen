package com.bridgex.core.strategies.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commerceservices.order.strategies.impl.DefaultQuoteCartValidationStrategy;
import de.hybris.platform.core.model.order.AbstractOrderModel;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 12/28/2017.
 */
public class PentlandQuoteCartValidationStrategy extends DefaultQuoteCartValidationStrategy {
  @Override
  public boolean validate(AbstractOrderModel source, AbstractOrderModel target) {
    validateParameterNotNullStandardMessage("source", source);
    validateParameterNotNullStandardMessage("target", target);
    return true;
//    return compareEntries(source.getEntries(), target.getEntries());
  }
}
