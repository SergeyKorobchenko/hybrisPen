/**
 * 
 */
package com.bridgex.core.actions;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import de.hybris.platform.acceleratorservices.email.impl.DefaultEmailGenerationService;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageTemplateModel;
import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
/**
 * @author santoshi
 *
 */
public class PentlandDefaultEmailGenerationService extends DefaultEmailGenerationService
{
 private static final Logger LOG = Logger.getLogger(PentlandDefaultEmailGenerationService.class);
 
 @Resource(name = "configurationService")
 private ConfigurationService configurationService;
 private UserService userService;
 private ModelService modelService;
 
 @Override
 public EmailMessageModel generate(final BusinessProcessModel businessProcessModel, final EmailPageModel emailPageModel)
 {
 
  ServicesUtil.validateParameterNotNull(emailPageModel, "EmailPageModel cannot be null");
  Assert.isInstanceOf(EmailPageTemplateModel.class, emailPageModel.getMasterTemplate(),
    "MasterTemplate associated with EmailPageModel should be EmailPageTemplate");

  final EmailPageTemplateModel emailPageTemplateModel = (EmailPageTemplateModel) emailPageModel.getMasterTemplate();
  final RendererTemplateModel bodyRenderTemplate = emailPageTemplateModel.getHtmlTemplate();
  Assert.notNull(bodyRenderTemplate, "HtmlTemplate associated with MasterTemplate of EmailPageModel cannot be null");
  final RendererTemplateModel subjectRenderTemplate = emailPageTemplateModel.getSubject();
  Assert.notNull(subjectRenderTemplate, "Subject associated with MasterTemplate of EmailPageModel cannot be null");

  final EmailMessageModel emailMessageModel;
  //This call creates the context to be used for rendering of subject and body templates.
  final AbstractEmailContext<BusinessProcessModel> emailContext = getEmailContextFactory().create(businessProcessModel,
    emailPageModel, bodyRenderTemplate);

  if (emailContext == null)
  {
   LOG.error("Failed to create email context for businessProcess [" + businessProcessModel + "]");
   throw new IllegalStateException("Failed to create email context for businessProcess [" + businessProcessModel + "]");
  }
  else
  {
   if (!validate(emailContext))
   {
    LOG.error("Email context for businessProcess [" + businessProcessModel + "] is not valid: "
      + ReflectionToStringBuilder.toString(emailContext));
    throw new IllegalStateException("Email context for businessProcess [" + businessProcessModel + "] is not valid: "
      + ReflectionToStringBuilder.toString(emailContext));
   }

   final StringWriter subject = new StringWriter();
   getRendererService().render(subjectRenderTemplate, emailContext, subject);

   final StringWriter body = new StringWriter();
   getRendererService().render(bodyRenderTemplate, emailContext, body);
   if (emailPageTemplateModel.getUid() != null
     && (emailPageTemplateModel.getUid().equalsIgnoreCase("CustomerRegistrationEmailTemplate")))
   {
    emailMessageModel = createEmailMessage(subject.toString(), body.toString(), emailContext, emailPageTemplateModel);
   }
   else
   {
    emailMessageModel = createEmailMessage(subject.toString(), body.toString(), emailContext);
   }
   if (LOG.isDebugEnabled())
   {
    LOG.debug("Email Subject: " + emailMessageModel.getSubject());
    LOG.debug("Email Body: " + emailMessageModel.getBody());
   }

  }
  if(emailPageTemplateModel.getHtmlTemplate().getCode().equals("pentland_Email_Customer_Registration_Body"))
  {
	  UserModel userForUID = getUserService().getUserForUID(emailContext.getToEmail());
	  getModelService().remove(userForUID);
  }
  return emailMessageModel;
 }

 private EmailMessageModel createEmailMessage(String emailSubject, final String emailBody,
   AbstractEmailContext<BusinessProcessModel> emailContext, EmailPageTemplateModel emailPageTemplateModel)
 {
  final List<EmailAddressModel> toEmails = new ArrayList<EmailAddressModel>();
  EmailAddressModel fromAddress = null;
  final String pentlandEmailAddress = configurationService.getConfiguration().getString("pentland.email.addres");
  final String pentlandNameAddress = configurationService.getConfiguration().getString("pentland.name.addres");

  final EmailAddressModel pentlandAddress = getEmailService().getOrCreateEmailAddressForEmail(pentlandEmailAddress,
    pentlandNameAddress);
  toEmails.add(pentlandAddress);
  fromAddress = getEmailService().getOrCreateEmailAddressForEmail(emailContext.getFromEmail(),
    emailContext.getFromDisplayName());
  return getEmailService().createEmailMessage(toEmails, new ArrayList<EmailAddressModel>(),
    new ArrayList<EmailAddressModel>(), fromAddress, emailContext.getFromEmail(), emailSubject, emailBody, null);

 }

public UserService getUserService() {
	return userService;
}

public void setUserService(UserService userService) {
	this.userService = userService;
}

public ModelService getModelService() {
	return modelService;
}

public void setModelService(ModelService modelService) {
	this.modelService = modelService;
}
 
 
 
 

}
