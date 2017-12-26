package com.bridgex.core.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.customer.dao.PentlandPrincipalGroupMemberDao;
import com.bridgex.core.services.PentlandB2BUnitService;

import de.hybris.platform.acceleratorservices.email.strategy.EmailAddressFetchStrategy;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.acceleratorservices.process.email.actions.GenerateEmailAction;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.task.RetryLaterException;

/**
 * @author Goncharenko Mikhail, created on 22.11.2017.
 */
public class GenerateSalesRepEmailAction extends GenerateEmailAction {

  private PentlandPrincipalGroupMemberDao groupMemberDao;
  private EmailAddressFetchStrategy       emailFetchStrategy;
  private PentlandB2BUnitService b2BUnitService;

  private static final Logger LOG = Logger.getLogger(GenerateSalesRepEmailAction.class);

  @Required
  public void setGroupMemberDao(PentlandPrincipalGroupMemberDao groupMemberDao) {
    this.groupMemberDao = groupMemberDao;
  }

  @Required
  public void setEmailFetchStrategy(EmailAddressFetchStrategy emailFetchStrategy) {
    this.emailFetchStrategy = emailFetchStrategy;
  }

  @Override
  public Transition executeAction(final BusinessProcessModel businessProcessModel) throws RetryLaterException
  {
    getContextResolutionStrategy().initializeContext(businessProcessModel);

    final CatalogVersionModel contentCatalogVersion = getContextResolutionStrategy()
      .getContentCatalogVersion(businessProcessModel);
    if (contentCatalogVersion == null)
    {
      LOG.warn("Could not resolve the content catalog version, cannot generate email content");
      return Transition.NOK;
    }

    final EmailPageModel emailPageModel = getCmsEmailPageService().getEmailPageForFrontendTemplate(getFrontendTemplateName(),
                                                                                                   contentCatalogVersion);
    if (emailPageModel == null)
    {
      LOG.warn("Could not retrieve email page model for " + getFrontendTemplateName() + " and "
               + contentCatalogVersion.getCatalog().getName() + ":" + contentCatalogVersion.getVersion()
               + ", cannot generate email content");
      return Transition.NOK;
    }

    final EmailMessageModel emailMessageModel = getEmailGenerationService().generate(businessProcessModel, emailPageModel);
    if (emailMessageModel == null)
    {
      LOG.warn("Failed to generate email message");
      return Transition.NOK;
    }

    emailMessageModel.setToAddresses(getSalesRepAddresses(businessProcessModel.getUser()));

    final List<EmailMessageModel> emails = new ArrayList<>();
    emails.addAll(businessProcessModel.getEmails());
    emails.add(emailMessageModel);
    businessProcessModel.setEmails(emails);

    getModelService().save(businessProcessModel);

    LOG.info("Email message generated");
    return Transition.OK;
  }

  private List<EmailAddressModel> getSalesRepAddresses(UserModel user) {
    Set<String> unitIds = b2BUnitService.getAllParents(user).stream()
                              .map(B2BUnitModel::getUid)
                              .collect(Collectors.toSet());
    return groupMemberDao.findEmployeesForB2BUnits(unitIds).stream()
      .map(emp -> emailFetchStrategy.fetch(emp.getUid(), emp.getName()))
      .collect(Collectors.toList());
  }

  @Required
  public void setB2BUnitService(PentlandB2BUnitService b2BUnitService) {
    this.b2BUnitService = b2BUnitService;
  }
}
