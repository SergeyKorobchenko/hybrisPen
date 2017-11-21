package com.bridgex.core.strategies.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;

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
  private MediaService mediaService;

  @Override
  public void setRecipientTypeToContextClassMap(Map<CsEmailRecipients, String> recipientTypeToContextClassMap) {
    this.recipientTypeToContextClassMap = recipientTypeToContextClassMap;
  }

  @Override
  public void setMediaService(MediaService mediaService) {
    this.mediaService = mediaService;
  }

  @Override
  protected Email getPreConfiguredEmail() throws EmailException {
    Email email = super.getPreConfiguredEmail();
    //TODO create cfg property
    email.setFrom("support@pentlandconnect.com");
    return email;
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
    String urlPrefix = Config.getParameter(MEDIA_EXPORT_HTTP);
    return urlPrefix + mediaService.getMedia("/images/logo_pentland.png").getDownloadURL();
  }

}
