/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.pentlandb2baddon.controllers.pages.checkout;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractCheckoutController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.util.XSSFilterUtil;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.order.InvalidCartException;

import java.text.ParseException;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bridgex.facades.order.PentlandB2BCheckoutFacade;

@Controller
@RequestMapping(value = "/checkout/summary")
public class ReorderCheckoutController extends AbstractCheckoutController
{
	private static final String REDIRECT_ORDER_LIST_URL = REDIRECT_PREFIX + "/my-account/orders/";

	@Resource(name = "b2bCheckoutFacade")
	private PentlandB2BCheckoutFacade b2bCheckoutFacade;

	private static final Logger LOG = Logger.getLogger(ReorderCheckoutController.class);

	@RequestMapping(value = "/reorder", method =
	{ RequestMethod.PUT, RequestMethod.POST })
	@RequireHardLogIn
	public String reorder(@RequestParam(value = "orderCode") final String orderCode, final RedirectAttributes redirectModel)
			throws CMSItemNotFoundException, InvalidCartException, ParseException, CommerceCartModificationException
	{
		try
		{
			// create a cart from the order and set it as session cart.
			b2bCheckoutFacade.createCartFromSessionDetails(orderCode);
		}
		catch (final IllegalArgumentException e)
		{
			LOG.error(String.format("Unable to reorder %s", orderCode), e);
			GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER, "order.reorder.error", null);
			return REDIRECT_ORDER_LIST_URL;
		}
		return REDIRECT_PREFIX + "/checkout/multi/summary/view";
	}
}
