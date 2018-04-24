package com.bridgex.core.quote.cancelservices.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import java.util.Optional;

import org.apache.log4j.Logger;

import de.hybris.platform.commerceservices.enums.QuoteAction;
import de.hybris.platform.commerceservices.event.QuoteCancelEvent;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceQuoteService;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.impl.DefaultCartService;

public class CancelOrderQuoteServiceImpl extends DefaultCommerceQuoteService implements ICancelOrderQuoteService {

	private static final Logger LOG = Logger.getLogger(CancelOrderQuoteServiceImpl.class);

	/**
	 * This method is remove quote from cancelorderquery list
	 */
	public void unassignQuote(final QuoteModel quote, final UserModel assigner) {
		validateParameterNotNullStandardMessage("quote", quote);
		validateParameterNotNullStandardMessage("assigner", assigner);

		getQuoteAssignmentValidationStrategy().validateQuoteUnassignment(quote, assigner);

		final String errorMsg = String.format("An exception occured, could not un-assign quote code:%s",
				quote.getCode());
		executeQuoteAssignment(quote, null, errorMsg);
	}

	public void removeQuoteCart(final QuoteModel quote) {
		
		if (quote.getCartReference() != null) {
			if (isSessionQuoteSameAsRequestedQuote(quote)) {
				getSessionService().removeAttribute(DefaultCartService.SESSION_CART_PARAMETER_NAME);
			}
			getModelService().remove(quote.CARTREFERENCE);
			
			getModelService().refresh(quote);
		}
	}

	/**
	 * This method is cancel the quote
	 */
	public void cancelQuote(final QuoteModel quoteModel, final UserModel userModel) {
		
		QuoteModel quoteToCancel = quoteModel;
		validateParameterNotNullStandardMessage("quoteModel", quoteToCancel);
		validateParameterNotNullStandardMessage("userModel", userModel);

		getQuoteActionValidationStrategy().validate(QuoteAction.CANCEL, quoteToCancel, userModel);

		if (isSessionQuoteSameAsRequestedQuote(quoteToCancel)) {
			final Optional<CartModel> optionalCart = Optional.ofNullable(getCartService().getSessionCart());
			if (optionalCart.isPresent()) {
				quoteToCancel = updateQuoteFromCartInternal(optionalCart.get());
//					removeQuoteCart(quoteToCancel);
				
			}
		}
		
		quoteToCancel = getQuoteUpdateStateStrategy().updateQuoteState(QuoteAction.CANCEL, quoteToCancel, userModel);
		getModelService().save(quoteToCancel);
		getModelService().refresh(quoteToCancel);

		getEventService().publishEvent(new QuoteCancelEvent(quoteToCancel, userModel,
				getQuoteUserTypeIdentificationStrategy().getCurrentQuoteUserType(userModel).get()));
	}
}
