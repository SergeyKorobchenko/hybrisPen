package com.bridgex.core.order.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import java.util.List;
import java.util.stream.Collectors;

import de.hybris.platform.commerceservices.enums.QuoteAction;
import de.hybris.platform.commerceservices.enums.QuoteUserType;
import de.hybris.platform.commerceservices.event.QuoteBuyerSubmitEvent;
import de.hybris.platform.commerceservices.event.QuoteSalesRepSubmitEvent;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceQuoteService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.core.model.user.UserModel;

/**
 * @author Goncharenko Mikhail, created on 11.01.2018.
 */
public class PentlandCommerceQuoteService extends DefaultCommerceQuoteService {

  @Override
  public QuoteModel submitQuote(QuoteModel quoteModel, UserModel userModel) {
    validateParameterNotNullStandardMessage("quoteModel", quoteModel);
    validateParameterNotNullStandardMessage("userModel", userModel);

    getQuoteActionValidationStrategy().validate(QuoteAction.SUBMIT, quoteModel, userModel);

    QuoteModel updatedQuoteModel = isSessionQuoteSameAsRequestedQuote(quoteModel)
                                   ? updateQuoteFromCart(getCartService().getSessionCart(), userModel) : quoteModel;

    validateQuoteTotal(updatedQuoteModel);
    getQuoteMetadataValidationStrategy().validate(QuoteAction.SUBMIT, updatedQuoteModel, userModel);
    updatedQuoteModel = cleanupEmptyEntries(updatedQuoteModel);
    updatedQuoteModel = getQuoteUpdateStateStrategy().updateQuoteState(QuoteAction.SUBMIT, updatedQuoteModel, userModel);

    getModelService().save(updatedQuoteModel);
    getModelService().refresh(updatedQuoteModel);

    final QuoteUserType quoteUserType = getQuoteUserTypeIdentificationStrategy().getCurrentQuoteUserType(userModel).get();

    if (QuoteUserType.BUYER.equals(quoteUserType)) {
      final QuoteBuyerSubmitEvent quoteBuyerSubmitEvent = new QuoteBuyerSubmitEvent(updatedQuoteModel, userModel, quoteUserType);
      getEventService().publishEvent(quoteBuyerSubmitEvent);
    }
    else if (QuoteUserType.SELLER.equals(quoteUserType)) {
      final QuoteSalesRepSubmitEvent quoteSalesRepSubmitEvent = new QuoteSalesRepSubmitEvent(updatedQuoteModel, userModel, quoteUserType);
      getEventService().publishEvent(quoteSalesRepSubmitEvent);
    }

    return updatedQuoteModel;
  }

  private QuoteModel cleanupEmptyEntries(QuoteModel updatedQuoteModel) {
    List<AbstractOrderEntryModel> entries = updatedQuoteModel.getEntries().stream()
                                                             .filter(entry -> entry.getQuantity() > 0)
                                                             .collect(Collectors.toList());
    updatedQuoteModel.setEntries(entries);
    return updatedQuoteModel;
  }

}
