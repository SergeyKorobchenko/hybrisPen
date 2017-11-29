package com.bridgex.core.order.impl;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.order.impl.DefaultCalculationService;
import de.hybris.platform.order.strategies.calculation.OrderRequiresCalculationStrategy;
import de.hybris.platform.util.PriceValue;
import de.hybris.platform.util.TaxValue;

/**
 * @author Created by konstantin.pavlyukov on 11/10/2017.
 */
public class DefaultPentlandCalculationService extends DefaultCalculationService {

  private OrderRequiresCalculationStrategy pentlandOrderRequiresCalculationStrategy;

  protected void resetAllValues(final AbstractOrderEntryModel entry) throws CalculationException
  {

    try {
      final PriceValue pv = findBasePrice(entry);
      final AbstractOrderModel order = entry.getOrder();
      final PriceValue basePrice = convertPriceIfNecessary(pv, order.getNet(), order.getCurrency(), null);
      entry.setBasePrice(basePrice.getValue());
      //TODO this is workaround until integration will be enabled
    } catch (CalculationException e) {
      entry.setBasePrice(0d);
    }
  }

  @Override
  public void calculate(final AbstractOrderModel order) throws CalculationException {
    //disable calculation, reset base price only
    if (pentlandOrderRequiresCalculationStrategy.requiresCalculation(order)) {
      calculateEntries(order, false);
    }
  }

  @Override
  public void calculate(final AbstractOrderModel order, final Date date) throws CalculationException
  {
    final Date old = order.getDate();
    order.setDate(date);
    try {
      this.calculate(order);
    }finally {
      order.setDate(old);
      getModelService().save(order);
    }
  }

  @Override
  public void calculateTotals(final AbstractOrderModel order, final boolean recalculate) throws CalculationException
  {
    //disable calculation
  }

  protected void recalculateOrderEntryIfNeeded(final AbstractOrderEntryModel entry, final boolean forceRecalculation)
    throws CalculationException {
    if (forceRecalculation || pentlandOrderRequiresCalculationStrategy.requiresCalculation(entry)) {
      resetAllValues(entry);
    }
  }

  public void calculateEntries(final AbstractOrderModel order, final boolean forceRecalculate) throws CalculationException {
    for (final AbstractOrderEntryModel e : order.getEntries()) {
      recalculateOrderEntryIfNeeded(e, forceRecalculate);
    }

  }

  @Required
  public void setPentlandOrderRequiresCalculationStrategy(OrderRequiresCalculationStrategy pentlandOrderRequiresCalculationStrategy) {
    this.pentlandOrderRequiresCalculationStrategy = pentlandOrderRequiresCalculationStrategy;
  }
}
