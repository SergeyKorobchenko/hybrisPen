package com.bridgex.core.order.impl;

import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.services.PentlandBaseSiteService;

import de.hybris.platform.commerceservices.order.CommerceCartCalculationStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartModel;

/**
 * @author Goncharenko Mikhail, created on 26.07.2018.
 */
public class PentlandCartCalculationRouter implements CommerceCartCalculationStrategy {

  private CommerceCartCalculationStrategy b2cCartCalculationStrategy;
  private CommerceCartCalculationStrategy b2bCartCalculationStrategy;

  private PentlandBaseSiteService baseSiteService;

  @Override
  public boolean calculateCart(CommerceCartParameter parameter) {
    return getCartCalculationStrategy().calculateCart(parameter);
  }

  @Override
  public boolean recalculateCart(CommerceCartParameter parameter) {
    return getCartCalculationStrategy().recalculateCart(parameter);
  }

  @Override
  @Deprecated
  public boolean calculateCart(CartModel cartModel) {
    return getCartCalculationStrategy().calculateCart(cartModel);
  }

  @Override
  @Deprecated
  public boolean recalculateCart(CartModel cartModel) {
    return getCartCalculationStrategy().recalculateCart(cartModel);
  }

  @Required
  public void setB2cCartCalculationStrategy(CommerceCartCalculationStrategy b2cCartCalculationStrategy) {
    this.b2cCartCalculationStrategy = b2cCartCalculationStrategy;
  }

  @Required
  public void setB2bCartCalculationStrategy(CommerceCartCalculationStrategy b2bCartCalculationStrategy) {
    this.b2bCartCalculationStrategy = b2bCartCalculationStrategy;
  }

  @Required
  public void setBaseSiteService(PentlandBaseSiteService baseSiteService) {
    this.baseSiteService = baseSiteService;
  }

  public CommerceCartCalculationStrategy getCartCalculationStrategy() {
    return baseSiteService.isB2BChannel() ? b2bCartCalculationStrategy
                                          : b2cCartCalculationStrategy;
  }
}
