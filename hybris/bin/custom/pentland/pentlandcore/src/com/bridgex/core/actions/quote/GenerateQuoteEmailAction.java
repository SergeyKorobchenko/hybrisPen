package com.bridgex.core.actions.quote;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.bridgex.core.model.DivisionModel;

import de.hybris.platform.acceleratorservices.email.EmailService;
import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.acceleratorservices.process.email.actions.GenerateEmailAction;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
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
      UserModel user = (businessProcessModel.getUser());
      if (user instanceof B2BCustomerModel) {
        B2BCustomerModel b2BCustomer = (B2BCustomerModel) user;
        List<EmailMessageModel> emailMessages = businessProcessModel.getEmails();
        EmailMessageModel lastMessage = emailMessages.get(emailMessages.size() - 1);
        EmailAddressModel emailAddress = getCustomerOpsEmail(b2BCustomer);
        lastMessage.setToAddresses(Collections.singletonList(emailAddress));
        getModelService().save(lastMessage);
        return Transition.OK;
      }
    }
    return Transition.NOK;
  }

  private EmailAddressModel getCustomerOpsEmail(B2BCustomerModel b2BCustomer) {
    B2BUnitModel b2bUnit = (B2BUnitModel)b2BCustomer.getGroups().stream().filter(group -> group instanceof B2BUnitModel).findFirst().orElse(null);
    String email = "";
    String displayedName = "";
    if(b2bUnit != null && b2bUnit.getDivision() != null){
      email = b2bUnit.getDivision().getEmail();
      displayedName = b2bUnit.getDivision().getName();
    }else {
      email = Optional.ofNullable(b2BCustomer.getDivision()).map(DivisionModel::getEmail).orElse(DEFAULT_CUSTOMERREP_EMAIL);
      displayedName = Optional.ofNullable(b2BCustomer.getDivision()).map(DivisionModel::getName).orElse(DEFAULT_CUSTOMERREP_NAME);
    }
    return emailService.getOrCreateEmailAddressForEmail(email,displayedName);
  }

  public void setEmailService(EmailService emailService) {
    this.emailService = emailService;
  }
}
