package com.bridgex.core.order.interceptor;

import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.event.OrderStatusChangedEvent;

import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.core.enums.ExportStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.interceptors.DefaultOrderPrepareInterceptor;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

/**
 * @author Goncharenko Mikhail, created on 23.11.2017.
 */
public class PentlandOrderPrepareInterceptor extends DefaultOrderPrepareInterceptor {

  private EventService eventService;

  @Required
  public void setEventService(EventService eventService) {
    this.eventService = eventService;
  }

  @Override
  public void onPrepare(Object model, InterceptorContext ctx) throws InterceptorException {
    super.onPrepare(model, ctx);
    if (model instanceof OrderModel) {
      OrderModel order = (OrderModel) model;
      if (isNotificationNeeded(order, ctx)) {
        submitChangeStatusEvent(order.getSourceOrder());
      }
    }
  }

  private void submitChangeStatusEvent(OrderModel order) {
    eventService.publishEvent(new OrderStatusChangedEvent(order.getCode()));
  }

  private boolean isNotificationNeeded(OrderModel order, InterceptorContext ctx) {
    return ctx.isModified(order, OrderModel.STATUS) && !ctx.isNew(order) && SalesApplication.SAP.equals(order.getSalesApplication()) && order.getSourceOrder() != null;
  }
}