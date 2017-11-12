package com.bridgex.core.order.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.commerceservices.order.impl.DefaultCommerceCartCalculationStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.promotions.jalo.PromotionsManager;

/**
 * @author Created by konstantin.pavlyukov on 11/10/2017.
 */
public class DefaultPentlandCommerceCartCalculationStrategy extends DefaultCommerceCartCalculationStrategy {

  @Override
  public boolean calculateCart(final CommerceCartParameter parameter)
  {
    final CartModel cartModel = parameter.getCart();

    validateParameterNotNull(cartModel, "Cart model cannot be null");

    final CalculationService calcService = getCalculationService();
    boolean recalculated = false;
    if (calcService.requiresCalculation(cartModel))
    {
      try
      {
        parameter.setRecalculate(false);
        beforeCalculate(parameter);
        calcService.calculate(cartModel);
      }
      catch (final CalculationException calculationException)
      {
        throw new IllegalStateException("Cart model " + cartModel.getCode() + " was not calculated due to: "
                                        + calculationException.getMessage(), calculationException);
      }
      finally
      {
        afterCalculate(parameter);
      }
      recalculated = true;
    }
    return recalculated;
  }

  @Override
  public boolean recalculateCart(final CommerceCartParameter parameter)
  {
    final CartModel cartModel = parameter.getCart();
    try
    {
      parameter.setRecalculate(true);
      beforeCalculate(parameter);
      getCalculationService().recalculate(cartModel);
    }
    catch (final CalculationException calculationException)
    {
      throw new IllegalStateException(String.format("Cart model %s was not calculated due to: %s ", cartModel.getCode(),
                                                    calculationException.getMessage()), calculationException);
    }
    finally
    {
      afterCalculate(parameter);

    }
    return true;
  }
}
