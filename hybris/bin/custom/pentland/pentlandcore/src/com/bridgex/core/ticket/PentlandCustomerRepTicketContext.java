package com.bridgex.core.ticket;

import java.util.Optional;

import com.bridgex.core.model.DivisionModel;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ticket.email.context.CustomerTicketContext;
import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import de.hybris.platform.ticket.model.CsTicketModel;

/**
 * Created by Goncharenko Mikhail on 30.10.2017.
 */
public class PentlandCustomerRepTicketContext extends CustomerTicketContext {

  public PentlandCustomerRepTicketContext(CsTicketModel ticket, CsTicketEventModel event) {
    super(ticket, event);
  }

  @Override
  public String getTo() {
    UserModel user = getTicket().getCustomer();
    return Optional.of(((B2BCustomerModel) user).getDivision())
                   .map(DivisionModel::getEmail)
                   .orElse(null);
  }

}
