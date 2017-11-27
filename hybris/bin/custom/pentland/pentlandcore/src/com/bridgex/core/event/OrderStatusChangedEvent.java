package com.bridgex.core.event;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.event.ClusterAwareEvent;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;

/**
 * @author Goncharenko Mikhail, created on 23.11.2017.
 */
public class OrderStatusChangedEvent extends AbstractEvent implements ClusterAwareEvent {

  private final String orderCode;

  public OrderStatusChangedEvent(String orderCode) {
    this.orderCode = orderCode;
  }

  @Override
  public boolean publish(int i, int i1) {
    return true;
  }

  public String getOrderCode() {
    return orderCode;
  }
}
