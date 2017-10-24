package com.bridgex.core.actions.quote;

import java.util.Optional;

import javax.annotation.Resource;

import com.bridgex.core.model.DivisionModel;

import de.hybris.platform.acceleratorservices.email.EmailService;
import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.acceleratorservices.process.email.actions.GenerateEmailAction;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.task.RetryLaterException;


/**
 * Created by Goncharenko Mikhail on 24.10.2017.
 */
public class GenerateQuoteEmailAction extends GenerateEmailAction {

  private final static String DEFAULT_CUSTOMERREP_EMAIL = "demo@example.com";
  private final static String DEFAULT_CUSTOMERREP_NAME = "Customer Rep";

  private EmailService emailService;

  @Override
  public Transition executeAction(BusinessProcessModel businessProcessModel) throws RetryLaterException {
    Transition result = super.executeAction(businessProcessModel);

    if (result == Transition.OK) {
      UserModel user = businessProcessModel.getUser();

      if (user instanceof B2BCustomerModel) {
        B2BCustomerModel b2BCustomer = (B2BCustomerModel) user;
        EmailMessageModel emailMessage = businessProcessModel.getEmails().iterator().next();
        EmailAddressModel emailAddress = getCustomerRepEmail(b2BCustomer);
        emailMessage.getToAddresses().add(emailAddress);
        return Transition.OK;
      }
    }
    return Transition.NOK;
  }

  private EmailAddressModel getCustomerRepEmail(B2BCustomerModel b2BCustomer) {
    String email = Optional.of(b2BCustomer.getDivision())
                           .map(DivisionModel::getEmail)
                           .orElse(DEFAULT_CUSTOMERREP_EMAIL);
    String displayedName = Optional.of(b2BCustomer.getDivision())
                           .map(DivisionModel::getName)
                           .orElse(DEFAULT_CUSTOMERREP_NAME);
    return emailService.getOrCreateEmailAddressForEmail(email,displayedName);
  }

  public void setEmailService(EmailService emailService) {
    this.emailService = emailService;
  }
}
