package com.bridgex.fulfilmentprocess.actions.order;


import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.order.PentlandOrderExportService;

import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;

import de.hybris.platform.task.RetryLaterException;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/7/2017.
 */
public class ExportOrderAction extends AbstractProceduralAction<OrderProcessModel> {

  private PentlandOrderExportService pentlandOrderExportService;

  @Override
  public void executeAction(OrderProcessModel orderProcessModel) throws RetryLaterException, Exception {
    pentlandOrderExportService.exportOrder(orderProcessModel.getOrder());
  }

  @Required
  public void setPentlandOrderExportService(PentlandOrderExportService pentlandOrderExportService) {
    this.pentlandOrderExportService = pentlandOrderExportService;
  }
}
