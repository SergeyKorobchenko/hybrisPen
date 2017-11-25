package com.bridgex.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commerceservices.model.process.QuoteProcessModel;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;

/**
 * @author Goncharenko Mikhail, created on 24.11.2017.
 */
public class QuoteNotificationOpsEmailContext extends QuoteNotificationEmailContext {

  public static final String OPS_MAIL_FLAG = "opsMail";
  public static final String ACCOUNT_NAME = "account";

  @Override
  public void init(QuoteProcessModel quoteProcessModel, EmailPageModel emailPageModel) {
    super.init(quoteProcessModel, emailPageModel);
    put(OPS_MAIL_FLAG, true);
    putCustomerAccount(quoteProcessModel);
  }

  private void putCustomerAccount(QuoteProcessModel quoteProcess) {
    QuoteModel quote = getQuoteService().getCurrentQuoteForCode(quoteProcess.getQuoteCode());
    B2BCustomerModel user = (B2BCustomerModel) (quote.getUser());
    user.getAllGroups().stream()
        .filter(B2BUnitModel.class::isInstance)
        .map(B2BUnitModel.class::cast)
        .map(B2BUnitModel::getName)
        .findFirst().ifPresent(name -> put(ACCOUNT_NAME, name));
  }

}
