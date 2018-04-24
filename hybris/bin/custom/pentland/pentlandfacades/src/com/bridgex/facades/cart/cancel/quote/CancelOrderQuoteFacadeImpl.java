package com.bridgex.facades.cart.cancel.quote;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import org.apache.log4j.Logger;

import com.bridgex.core.quote.cancelservices.impl.ICancelOrderQuoteService;

import de.hybris.platform.commercefacades.order.impl.DefaultQuoteFacade;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.core.model.user.UserModel;

public class CancelOrderQuoteFacadeImpl extends DefaultQuoteFacade implements ICancelOrderQuoteFacade {

	private ICancelOrderQuoteService cancelOrderQuoteService;

	private static final Logger LOG = Logger.getLogger(CancelOrderQuoteFacadeImpl.class);

	/**
	 * This method is going to cancel the quote based on quote code
	 */
	public void cancelQuote(final String quoteCode) {
		
		validateParameterNotNullStandardMessage("quoteCode", quoteCode);

		final QuoteModel quoteModel = getQuoteModelForCode(quoteCode);
		final UserModel userModel = getQuoteUserIdentificationStrategy().getCurrentQuoteUser();

		getCancelOrderQuoteService().unassignQuote(quoteModel, userModel);

		getCancelOrderQuoteService().cancelQuote(quoteModel, getQuoteUserIdentificationStrategy().getCurrentQuoteUser());
		
	}

	public ICancelOrderQuoteService getCancelOrderQuoteService() {
		return cancelOrderQuoteService;
	}

	public void setCancelOrderQuoteService(ICancelOrderQuoteService cancelOrderQuoteService) {
		this.cancelOrderQuoteService = cancelOrderQuoteService;
	}

}
