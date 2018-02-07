package com.bridgex.core.ticket.strategies.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.ticket.PentlandCustomerRepTicketContext;
import com.bridgex.core.ticket.PentlandCustomerTicketContext;

import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.ticket.email.context.AbstractTicketContext;
import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import de.hybris.platform.ticket.model.CsTicketEventEmailConfigurationModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.strategies.impl.DefaultTicketEventEmailStrategy;

/**
 * @author Created by ekaterina.agievich@masterdata.ru on 2/6/2018.
 */
public class PentlandDefaultTicketEventEmailStrategy extends DefaultTicketEventEmailStrategy {

  private static final Logger LOG = Logger.getLogger(PentlandDefaultTicketEventEmailStrategy.class);

  private EnumerationService enumerationService;

  @Override
  protected AbstractTicketContext createContextForEvent(CsTicketEventEmailConfigurationModel config, CsTicketModel ticket, CsTicketEventModel event) {
    AbstractTicketContext ticketContext = super.createContextForEvent(config, ticket, event);
    if(ticketContext instanceof PentlandCustomerTicketContext){
      PentlandCustomerTicketContext pentlandTicket = (PentlandCustomerTicketContext) ticketContext;
      pentlandTicket.setLocalizedCategory(enumerationService.getEnumerationName(ticket.getCategory()));
      return pentlandTicket;
    }else if(ticketContext instanceof PentlandCustomerRepTicketContext){
      PentlandCustomerRepTicketContext pentlandTicket = (PentlandCustomerRepTicketContext) ticketContext;
      pentlandTicket.setLocalizedCategory(enumerationService.getEnumerationName(ticket.getCategory()));
      return pentlandTicket;
    }
    return ticketContext;
  }


  @Required
  public void setEnumerationService(final EnumerationService enumerationService)
  {
    this.enumerationService = enumerationService;
  }
}
