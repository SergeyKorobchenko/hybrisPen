package com.bridgex.core.ticket;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import com.bridgex.core.model.DivisionModel;
import com.bridgex.core.services.PentlandB2BUnitService;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
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
    B2BUnitModel b2bUnit = (B2BUnitModel)user.getGroups().stream().filter(group -> group instanceof B2BUnitModel).findFirst().orElse(null);
    if(b2bUnit != null && b2bUnit.getDivision() != null){
      return b2bUnit.getDivision().getEmail();
    }else {
      return Optional.ofNullable(((B2BCustomerModel) user)).map(B2BCustomerModel::getDivision).map(DivisionModel::getEmail).orElse(null);
    }
  }

  public String getCustomerName(){
    UserModel user = getTicket().getCustomer();
    return user.getName();
  }

  public String getCustomerEmail(){
    UserModel user = getTicket().getCustomer();
    return user.getUid();
  }

  public String getSapCustomer(){
    UserModel user = getTicket().getCustomer();
    B2BUnitModel b2bUnit = (B2BUnitModel)user.getGroups().stream().filter(group -> group instanceof B2BUnitModel).findFirst().orElse(null);
    if(b2bUnit != null && StringUtils.isNotEmpty(b2bUnit.getSapID())){
      return b2bUnit.getSapID();
    }
    return StringUtils.EMPTY;
  }

  public String getCustomerAccount(){
    UserModel user = getTicket().getCustomer();
    B2BUnitModel b2bUnit = (B2BUnitModel)user.getGroups().stream().filter(group -> group instanceof B2BUnitModel).findFirst().orElse(null);
    if(b2bUnit != null){
      return b2bUnit.getName();
    }

    return StringUtils.EMPTY;
  }

  @Override
  protected String buildParagraphs(String text) {
    return text.replaceAll("(\\n\\r?)+", "<br/>");
  }
}