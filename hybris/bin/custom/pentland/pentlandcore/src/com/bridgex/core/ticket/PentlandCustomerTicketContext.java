package com.bridgex.core.ticket;

import java.util.Optional;

import de.hybris.platform.b2b.jalo.B2BCustomer;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ticket.email.context.CustomerTicketContext;
import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import de.hybris.platform.ticket.model.CsTicketModel;

/**
 * Created by Goncharenko Mikhail on 30.10.2017.
 */
public class PentlandCustomerTicketContext extends CustomerTicketContext {

  private String localizedCategory;

  public PentlandCustomerTicketContext(CsTicketModel ticket, CsTicketEventModel event, String logoUrl) {
    super(ticket, event);
    put("logo", logoUrl);
  }

  @Override
  public String getTo() {
    return Optional.ofNullable(getTicket())
                   .map(CsTicketModel::getCustomer)
                   .map(UserModel::getUid)
                   .orElse(null);
  }

  @Override
  protected String buildParagraphs(String text) {
    return text.replaceAll("(\\n\\r?)+", "<br/>");
  }

  public String getLocalizedCategory() {
    return localizedCategory;
  }

  public void setLocalizedCategory(String localizedCategory) {
    this.localizedCategory = localizedCategory;
  }
}