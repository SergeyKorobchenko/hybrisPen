package com.bridgex.core.event;

import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.b2b.services.B2BOrderService;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.OrderService;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.model.ModelService;

/**
 * @author Goncharenko Mikhail, created on 23.11.2017.
 */
public class OrderStatusChangedEventListener extends AbstractEventListener<OrderStatusChangedEvent> {

  private final static String PROCESS_DEFINITION = "orderStatusChangeEmailProcess";

  private ModelService           modelService;
  private BusinessProcessService businessProcessService;
  private B2BOrderService        b2BOrderService;

  @Required
  public void setModelService(ModelService modelService) {
    this.modelService = modelService;
  }

  @Required
  public void setBusinessProcessService(BusinessProcessService businessProcessService) {
    this.businessProcessService = businessProcessService;
  }

  public void setOrderService(B2BOrderService b2BOrderService) {
    this.b2BOrderService = b2BOrderService;
  }

  @Override
  protected void onEvent(OrderStatusChangedEvent event) {

    OrderModel order = b2BOrderService.getOrderForCode(event.getOrderCode());

    OrderProcessModel process = businessProcessService.createProcess(PROCESS_DEFINITION + "-" + order.getCode() + "-" + System.currentTimeMillis(), PROCESS_DEFINITION);
    process.setOrder(order);
    modelService.save(process);
    businessProcessService.startProcess(process);
  }

}
