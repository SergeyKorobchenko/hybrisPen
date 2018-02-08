package com.bridgex.core.event;

import de.hybris.platform.servicelayer.event.ClusterAwareEvent;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;

/**
 * @author Goncharenko Mikhail, created on 23.11.2017.
 */
public class OrderStatusChangedEvent extends AbstractEvent implements ClusterAwareEvent {

  private final String orderCode;
  private final boolean notifySales;

  public OrderStatusChangedEvent(String orderCode) {
    this.orderCode = orderCode;
    this.notifySales = false;
  }

  public OrderStatusChangedEvent(String orderCode, boolean notifySales) {
    this.orderCode = orderCode;
    this.notifySales = notifySales;
  }

  @Override
  public boolean publish(int i, int i1) {
    return true;
  }

  public String getOrderCode() {
    return orderCode;
  }

  public boolean isNotifySales() {
    return notifySales;
  }
}
