package com.bridgex.core.order.impl;

import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.order.PentlandCommerceCheckoutService;

import de.hybris.platform.b2bacceleratorservices.order.impl.DefaultB2BCommerceCheckoutService;
import de.hybris.platform.commerceservices.order.CommerceDeliveryAddressStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/31/2017.
 */
public class DefaultPentlandCommerceCheckoutService extends DefaultB2BCommerceCheckoutService implements PentlandCommerceCheckoutService {

  private CommerceDeliveryAddressStrategy markForAddressStrategy;

  @Override
  public boolean setMarkForAddress(CommerceCheckoutParameter parameter) {
    return markForAddressStrategy.storeDeliveryAddress(parameter);
  }

  @Required
  public void setMarkForAddressStrategy(CommerceDeliveryAddressStrategy markForAddressStrategy) {
    this.markForAddressStrategy = markForAddressStrategy;
  }
}
