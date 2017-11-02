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
package com.bridgex.storefront.controllers.pages.checkout.steps;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bridgex.facades.order.PentlandAcceleratorCheckoutFacade;
import com.bridgex.storefront.controllers.ControllerConstants;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.PreValidateCheckoutStep;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.PreValidateQuoteCheckoutStep;
import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.CheckoutStep;
import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.validation.ValidationResults;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.checkout.steps.AbstractCheckoutStepController;
import de.hybris.platform.acceleratorstorefrontcommons.forms.AddressForm;
import de.hybris.platform.acceleratorstorefrontcommons.util.AddressDataUtil;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.user.data.AddressData;

@Controller
@RequestMapping(value = "/checkout/multi/mark-for")
public class MarkForAddressCheckoutStepController extends AbstractCheckoutStepController
{
	public static final String MARK_FOR = "mark-for";

	@Resource(name = "addressDataUtil")
	private AddressDataUtil addressDataUtil;

	@Resource
	private PentlandAcceleratorCheckoutFacade acceleratorCheckoutFacade;

	@Override
	@RequestMapping(value = "/choose", method = RequestMethod.GET)
	@RequireHardLogIn
	@PreValidateQuoteCheckoutStep
	@PreValidateCheckoutStep(checkoutStep = MARK_FOR)
	public String enterStep(final Model model, final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		final CartData cartData = getCheckoutFacade().getCheckoutCart();

		List<? extends AddressData> markForAddresses = getMarkForAddresses(cartData.getDeliveryAddress());
		if(CollectionUtils.isEmpty(markForAddresses)){
			return getCheckoutStep().nextStep();
		}

		populateCommonModelAttributes(model, cartData, new AddressForm(), markForAddresses);

		return ControllerConstants.Views.Pages.MultiStepCheckout.MarkForPage;
	}

	/**
	 * This method gets called when the "Use this Address" button is clicked. It sets the selected delivery address on
	 * the checkout facade - if it has changed, and reloads the page highlighting the selected delivery address.
	 *
	 * @param selectedAddressCode
	 *           - the id of the delivery address.
	 *
	 * @return - a URL to the page to load.
	 */
	@RequestMapping(value = "/select", method = RequestMethod.GET)
	@RequireHardLogIn
	public String doSelectDeliveryAddress(@RequestParam("selectedAddressCode") final String selectedAddressCode,
			final RedirectAttributes redirectAttributes)
	{
		final ValidationResults validationResults = getCheckoutStep().validate(redirectAttributes);
		if (getCheckoutStep().checkIfValidationErrors(validationResults)) {
			return getCheckoutStep().onValidation(validationResults);
		}

		setDeliveryAddress(selectedAddressCode);

		return getCheckoutStep().nextStep();
	}

	protected void setDeliveryAddress(final String selectedAddressCode) {
		acceleratorCheckoutFacade.setMarkForAddressForCart(selectedAddressCode);
	}

	@RequestMapping(value = "/back", method = RequestMethod.GET)
	@RequireHardLogIn
	@Override
	public String back(final RedirectAttributes redirectAttributes)
	{
		return getCheckoutStep().previousStep();
	}

	@RequestMapping(value = "/next", method = RequestMethod.GET)
	@RequireHardLogIn
	@Override
	public String next(final RedirectAttributes redirectAttributes)
	{
		return getCheckoutStep().nextStep();
	}

	protected String getBreadcrumbKey()
	{
		return "checkout.multi." + getCheckoutStep().getProgressBarId() + ".breadcrumb";
	}

	protected CheckoutStep getCheckoutStep()
	{
		return getCheckoutStep(MARK_FOR);
	}

	protected void populateCommonModelAttributes(final Model model, final CartData cartData, final AddressForm addressForm,
	                                             final List<? extends AddressData> markFors) throws CMSItemNotFoundException
	{
		model.addAttribute("cartData", cartData);
		model.addAttribute("addressForm", addressForm);
		model.addAttribute("deliveryAddresses", markFors);
		model.addAttribute("noAddress", getCheckoutFlowFacade().hasNoDeliveryAddress());
		model.addAttribute("addressFormEnabled", getCheckoutFacade().isNewAddressEnabledForCart());
		model.addAttribute("removeAddressEnabled", getCheckoutFacade().isRemoveAddressEnabledForCart());
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, getResourceBreadcrumbBuilder().getBreadcrumbs(getBreadcrumbKey()));
		model.addAttribute("metaRobots", "noindex,nofollow");

		prepareDataForPage(model);
		storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		setCheckoutStepLinksForModel(model, getCheckoutStep());
	}

	private List<? extends AddressData> getMarkForAddresses(final AddressData selectedAddressData) // NOSONAR
	{
		List<AddressData> markForAddresses = null;
		if (selectedAddressData != null) {
			markForAddresses = acceleratorCheckoutFacade.findMarkForAddressesForShippingAddress(selectedAddressData.getId());


		}

		return markForAddresses == null ? Collections.emptyList() : markForAddresses;
	}

}
