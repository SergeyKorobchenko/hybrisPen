package com.bridgex.core.order.impl;

import java.util.Collection;
import java.util.Date;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.order.impl.DefaultCalculationService;
import de.hybris.platform.util.PriceValue;
import de.hybris.platform.util.TaxValue;

/**
 * @author Created by konstantin.pavlyukov on 11/10/2017.
 */
public class DefaultPentlandCalculationService extends DefaultCalculationService {
  protected void resetAllValues(final AbstractOrderEntryModel entry) throws CalculationException
  {
    // taxes
    final Collection<TaxValue> entryTaxes = findTaxValues(entry);
    entry.setTaxValues(entryTaxes);
    try {
      final PriceValue pv = findBasePrice(entry);
      final AbstractOrderModel order = entry.getOrder();
      final PriceValue basePrice = convertPriceIfNecessary(pv, order.getNet(), order.getCurrency(), entryTaxes);
      entry.setBasePrice(basePrice.getValue());
      //TODO this is workaround until integration will be enabled
    } catch (CalculationException e) {
      entry.setBasePrice(0d);
    }
  }

  @Override
  public void calculate(final AbstractOrderModel order) throws CalculationException
  {
    //disable calculation
  }

  @Override
  public void calculate(final AbstractOrderModel order, final Date date) throws CalculationException
  {
    //disable calculation
  }

  @Override
  public void calculateTotals(final AbstractOrderModel order, final boolean recalculate) throws CalculationException
  {
    //disable calculation
  }

}
