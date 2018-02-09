package com.bridgex.core.event;

import java.time.LocalDate;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.b2b.services.B2BOrderService;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.model.ModelService;

/**
 * @author Goncharenko Mikhail, created on 23.11.2017.
 */
public class OrderStatusChangedEventListener extends AbstractEventListener<OrderStatusChangedEvent> {

  private static Logger LOG = Logger.getLogger(OrderStatusChangedEventListener.class);

  private final static String PROCESS_DEFINITION = "orderStatusChangeEmailProcess";
  private final static String PROCESS_DEFINITION_REP = "orderStatusChangeRepEmailProcess";

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

    String processId = PROCESS_DEFINITION + "-" + order.getCode() + "-" + order.getStatus() + "-" + LocalDate.now().toString();
    //check that this status change is not already being handled by another node
    if(businessProcessService.getProcess(processId) == null){
      try {
        OrderProcessModel process = businessProcessService.createProcess(processId, PROCESS_DEFINITION);
        process.setOrder(order.getSourceOrder());
        modelService.save(process);
        businessProcessService.startProcess(process);
      }catch(Throwable e){
        //process was already created by another node
        LOG.debug(e.getMessage());
      }
    }
    if(event.isNotifySales()){
      try {
      String processIdRep = PROCESS_DEFINITION_REP + "-" + order.getCode() + "-" + order.getStatus() + "-" + LocalDate.now().toString();
      OrderProcessModel process = businessProcessService.createProcess(processIdRep, PROCESS_DEFINITION_REP);
      process.setOrder(order.getSourceOrder());
      modelService.save(process);
      businessProcessService.startProcess(process);
      }catch(Throwable e){
        //the email was already sent
        LOG.debug(e.getMessage());
      }
    }
  }

}
