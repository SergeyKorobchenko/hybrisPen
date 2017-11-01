package com.bridgex.core.ticket;

import de.hybris.platform.ticket.email.context.CustomerTicketContext;
import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import de.hybris.platform.ticket.model.CsTicketModel;

/**
 * Created by Goncharenko Mikhail on 30.10.2017.
 */
public class PentlandCustomerTicketContext extends CustomerTicketContext {

  public PentlandCustomerTicketContext(CsTicketModel ticket, CsTicketEventModel event) {
    super(ticket, event);
  }

  @Override
  public String getTo() {
    return getTicket().getCustomer().getUid();
  }

}
