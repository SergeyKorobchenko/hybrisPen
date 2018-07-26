package com.bridgex.core.order.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.services.PentlandBaseSiteService;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.exceptions.CalculationException;

/**
 * @author Goncharenko Mikhail, created on 26.07.2018.
 */
public class PentlandCalculationServiceRouter implements CalculationService {

  private CalculationService b2cCalculationService;
  private CalculationService b2bCalculationService;

  private PentlandBaseSiteService baseSiteService;

  @Override
  public void calculate(AbstractOrderModel order) throws CalculationException {
    getCalculationService().calculate(order);
  }

  @Override
  public boolean requiresCalculation(AbstractOrderModel order) {
    return getCalculationService().requiresCalculation(order);
  }

  @Override
  public void calculate(AbstractOrderModel order, Date date) throws CalculationException {
    getCalculationService().calculate(order,date);
  }

  @Override
  public void recalculate(AbstractOrderModel order) throws CalculationException {
    getCalculationService().recalculate(order);
  }

  @Override
  public void recalculate(AbstractOrderModel order, Date date) throws CalculationException {
    getCalculationService().recalculate(order,date);
  }

  @Override
  public void calculateTotals(AbstractOrderModel order, boolean recalculate) throws CalculationException {
    getCalculationService().calculateTotals(order,recalculate);
  }

  @Override
  public void calculateTotals(AbstractOrderEntryModel entry, boolean recalculate) {
    getCalculationService().calculateTotals(entry,recalculate);
  }

  @Override
  public void recalculate(AbstractOrderEntryModel entry) throws CalculationException {
    getCalculationService().recalculate(entry);
  }

  @Required
  public void setB2cCalculationService(CalculationService b2cCalculationService) {
    this.b2cCalculationService = b2cCalculationService;
  }

  @Required
  public void setB2bCalculationService(CalculationService b2bCalculationService) {
    this.b2bCalculationService = b2bCalculationService;
  }

  @Required
  public void setBaseSiteService(PentlandBaseSiteService baseSiteService) {
    this.baseSiteService = baseSiteService;
  }

  public CalculationService getCalculationService() {
    return baseSiteService.isB2BChannel() ? b2bCalculationService
                                          : b2cCalculationService;
  }
}
