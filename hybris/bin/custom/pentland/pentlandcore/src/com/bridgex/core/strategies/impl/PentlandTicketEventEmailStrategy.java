package com.bridgex.core.strategies.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.ticket.email.context.AbstractTicketContext;
import de.hybris.platform.ticket.enums.CsEmailRecipients;
import de.hybris.platform.ticket.events.model.CsTicketChangeEventEntryModel;
import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import de.hybris.platform.ticket.model.CsTicketEventEmailConfigurationModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.strategies.impl.DefaultTicketEventEmailStrategy;
import de.hybris.platform.util.Config;

/**
 * @author Goncharenko Mikhail, created on 21.11.2017.
 */
public class PentlandTicketEventEmailStrategy extends DefaultTicketEventEmailStrategy {

  private static final Logger LOG = Logger.getLogger(PentlandTicketEventEmailStrategy.class);
  public static final String MEDIA_EXPORT_HTTP = "media.export.http";
  public static final String WEBSITE_PENTLAND_HTTPS = "website.pentland.https";

  private Map<CsEmailRecipients, String> recipientTypeToContextClassMap;
  private MediaService pentlandMediaService;

  @Override
  public void setRecipientTypeToContextClassMap(Map<CsEmailRecipients, String> recipientTypeToContextClassMap) {
    this.recipientTypeToContextClassMap = recipientTypeToContextClassMap;
  }

  @Required
  public void setPentlandMediaService(MediaService mediaService) {
    this.pentlandMediaService = mediaService;
  }

  @Override
  protected AbstractTicketContext createContextForEvent(CsTicketEventEmailConfigurationModel config, CsTicketModel ticket, CsTicketEventModel event) {
    String contextClassName = recipientTypeToContextClassMap.get(config.getRecipientType());

    try {
      Class contextClass = Class.forName(contextClassName);
      Constructor constructor = contextClass.getConstructor(new Class[]{CsTicketModel.class, CsTicketEventModel.class, String.class});
      AbstractTicketContext ticketContext = (AbstractTicketContext)constructor.newInstance(new Object[]{ticket, event, getLogoUrl()});
      StringBuilder text = new StringBuilder();
      Iterator var10 = event.getEntries().iterator();

      while(var10.hasNext()) {
        CsTicketChangeEventEntryModel e = (CsTicketChangeEventEntryModel)var10.next();
        text.append(e.getAlteredAttribute().getName() + ": " + e.getOldStringValue() + " -> " + e.getNewStringValue() + "\n");
      }

      text.append("\n").append(event.getText());
      event.setText(text.toString());
      ticketContext.put("siteUrl", Config.getParameter(WEBSITE_PENTLAND_HTTPS));
      return ticketContext;
    } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException | ClassNotFoundException var11) {
      LOG.error("Error finding context class for email target [" + config.getRecipientType() + "]. Context class was [" + contextClassName + "]", var11);
      return null;
    }
  }

  private String getLogoUrl() {
    try {
      String urlPrefix = Config.getParameter(MEDIA_EXPORT_HTTP);
      return urlPrefix + pentlandMediaService.getMedia("/images/logo_pentland.png").getDownloadURL();
    } catch (UnknownIdentifierException | AmbiguousIdentifierException e) {
      LOG.error("Error while loading logo image");
      return StringUtils.EMPTY;
    }
  }

}
