package com.bridgex.core.ticket;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.bridgex.core.model.DivisionModel;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.ticket.email.context.CustomerTicketContext;
import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import de.hybris.platform.ticket.model.CsTicketModel;

/**
 * Created by Goncharenko Mikhail on 30.10.2017.
 */
public class PentlandCustomerRepTicketContext extends CustomerTicketContext {

  public PentlandCustomerRepTicketContext(CsTicketModel ticket, CsTicketEventModel event, String logoUrl) {
    super(ticket, event);
    put("logo", logoUrl);
  }

  @Override
  public String getTo() {
    UserModel user = getTicket().getCustomer();
    return Optional.ofNullable(((B2BCustomerModel) user))
                   .map(B2BCustomerModel::getDivision)
                   .map(DivisionModel::getEmail)
                   .orElse(null);
  }

  @Override
  protected String buildParagraphs(String text) {
    return text.replaceAll("(\\n\\r?)+", "<br/>");
  }
}