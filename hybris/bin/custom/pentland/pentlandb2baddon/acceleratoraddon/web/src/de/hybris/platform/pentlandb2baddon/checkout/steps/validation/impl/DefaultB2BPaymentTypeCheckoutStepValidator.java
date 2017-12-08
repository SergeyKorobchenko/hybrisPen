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
package de.hybris.platform.pentlandb2baddon.checkout.steps.validation.impl;

import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.validation.ValidationResults;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.pentlandb2baddon.checkout.steps.validation.AbstractB2BCheckoutStepValidator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bridgex.facades.order.PentlandAcceleratorCheckoutFacade;
import com.bridgex.facades.order.PentlandCartFacade;

public class DefaultB2BPaymentTypeCheckoutStepValidator extends AbstractB2BCheckoutStepValidator {

	private PentlandAcceleratorCheckoutFacade pentlandB2BAcceleratorCheckoutFacade;
	private PentlandCartFacade                cartFacade;

	@Override
	protected ValidationResults doValidateOnEnter(final RedirectAttributes redirectAttributes) {
//		if(pentlandB2BAcceleratorCheckoutFacade.isCartTotalQuantityZero()){
//			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
//			                               "checkout.error.empty.cart");
//			return ValidationResults.FAILED;
//		}
		if(pentlandB2BAcceleratorCheckoutFacade.cartHasZeroQuantityBaseEntries()){
			GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
			                               "checkout.error.empty.entry.cart");
			return ValidationResults.FAILED;
		}
		pentlandB2BAcceleratorCheckoutFacade.cleanupZeroQuantityEntries();
		cartFacade.populateCart();
		CartData cartData = getCheckoutFacade().getCheckoutCart();
		if (cartData.getEntries() != null && !cartData.getEntries().isEmpty()){
			if(StringUtils.isEmpty(cartData.getPurchaseOrderNumber())){
				GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
				                               "checkout.error.empty.po");
				return ValidationResults.FAILED;
			}
			if(cartData.getRdd() == null){
				GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
				                               "checkout.error.empty.rdd");
				return ValidationResults.FAILED;
		}
			return ValidationResults.SUCCESS;
		}

		return ValidationResults.FAILED;
	}

	@Override
	public ValidationResults validateOnExit()
	{
		return ValidationResults.SUCCESS;
	}

	@Required
	public void setPentlandB2BAcceleratorCheckoutFacade(PentlandAcceleratorCheckoutFacade pentlandB2BAcceleratorCheckoutFacade) {
		this.pentlandB2BAcceleratorCheckoutFacade = pentlandB2BAcceleratorCheckoutFacade;
	}

	@Required
	public void setCartFacade(PentlandCartFacade cartFacade) {
		this.cartFacade = cartFacade;
	}
}