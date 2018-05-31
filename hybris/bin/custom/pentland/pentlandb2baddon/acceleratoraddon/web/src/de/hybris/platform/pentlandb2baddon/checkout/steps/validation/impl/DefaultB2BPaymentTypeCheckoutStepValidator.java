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
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.pentlandb2baddon.checkout.steps.validation.AbstractB2BCheckoutStepValidator;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bridgex.facades.order.PentlandAcceleratorCheckoutFacade;
import com.bridgex.facades.order.PentlandCartFacade;

public class DefaultB2BPaymentTypeCheckoutStepValidator extends AbstractB2BCheckoutStepValidator {

	private PentlandAcceleratorCheckoutFacade pentlandB2BAcceleratorCheckoutFacade;
	private PentlandCartFacade                cartFacade;
	private ConfigurationService configurationService;

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
		 String minimumOrderValue = getConfigurationService().getConfiguration().getString("basket.minimum.order.value");
		 String surCharge=null;
		 
		CartData cartData = getCheckoutFacade().getCheckoutCart();
		PriceData subTotal = cartData.getSubTotal();
		if(subTotal.getValue().doubleValue()<=Double.valueOf(minimumOrderValue))
		  {
			surCharge = getConfigurationService().getConfiguration().getString("basket.surcharge.value");
		  }
		
		cartFacade.populateCart(surCharge);
		
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
			List<String> sizeVars = new ArrayList<>();
			   List<OrderEntryData> entries = cartData.getEntries();
			   for (OrderEntryData orderEntryData : entries) {
			    List<OrderEntryData> entries2 = orderEntryData.getEntries();
			   
			    for (OrderEntryData orderEntryData2 : entries2) {
			      ProductData product = orderEntryData2.getProduct();
			      if(product.getPurchasable() == false)
			       
			      {
			      sizeVars.add(product.getSize()+ " of " + product.getMaterialKey());
			      }
			    
			    }
			   }
			     if(CollectionUtils.isNotEmpty(sizeVars))
			     {
			      GlobalMessages.addFlashMessage(redirectAttributes, GlobalMessages.ERROR_MESSAGES_HOLDER,
			        "cart.remove.size.variants",new Object[]
			          { sizeVars.toString() });
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

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	@Required
	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}
	
	
}