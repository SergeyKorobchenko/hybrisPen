/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.bridgex.supportticketingaddon.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.ResourceBreadcrumbBuilder;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.ThirdPartyConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractSearchPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;

import com.bridgex.constants.PentlandsupportticketingaddonConstants;
import com.bridgex.supportticketingaddon.forms.SupportTicketForm;

import de.hybris.platform.customerticketingfacades.TicketFacade;
import de.hybris.platform.customerticketingfacades.data.StatusData;
import de.hybris.platform.customerticketingfacades.data.TicketData;
import de.hybris.platform.ticket.service.UnsupportedAttachmentException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;


/**
 * Controller for Customer Support tickets.
 */
@Controller
@RequestMapping("/add-support-ticket")
public class SupportTicketsPageController extends AbstractSearchPageController
{
	private static final Logger LOG = Logger.getLogger(SupportTicketsPageController.class);

	@Resource(name = "csAttachmentUploadMaxSize")
	private long maxUploadSizeValue;

	@Resource(name = "accountBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder accountBreadcrumbBuilder;

	@Resource(name = "defaultTicketFacade")
	private TicketFacade ticketFacade;

	@Resource(name = "cartFacade")
	private CartFacade cartFacade;

	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;

	@Resource(name = "validator")
	private Validator validator;

	@Resource(name = "allowedUploadedFormats")
	private String allowedUploadedFormats;

	private final String[] DISALLOWED_FIELDS = new String[] {};

	@InitBinder
	public void initBinder(final WebDataBinder binder)
	{
		binder.setDisallowedFields(DISALLOWED_FIELDS);
	}

		@InitBinder
	public void init(final WebDataBinder binder)
	{
		binder.setBindEmptyMultipartFiles(false);
	}

	/**
	 * Used for retrieving page to create a customer support ticket.
	 *
	 * @param model
	 * @return View String
	 * @throws CMSItemNotFoundException
	 */
	@RequestMapping(method = RequestMethod.GET)
	@RequireHardLogIn
	public String addSupportTicket(final Model model) throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(PentlandsupportticketingaddonConstants.ADD_SUPPORT_TICKET_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(PentlandsupportticketingaddonConstants.ADD_SUPPORT_TICKET_PAGE));

		model.addAttribute(WebConstants.BREADCRUMBS_KEY, getBreadcrumbs(PentlandsupportticketingaddonConstants.TEXT_SUPPORT_TICKETING_ADD));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);
		model.addAttribute(PentlandsupportticketingaddonConstants.SUPPORT_TICKET_FORM, new SupportTicketForm());
		model.addAttribute(PentlandsupportticketingaddonConstants.MAX_UPLOAD_SIZE, maxUploadSizeValue);
		model.addAttribute(PentlandsupportticketingaddonConstants.MAX_UPLOAD_SIZE_MB, FileUtils.byteCountToDisplaySize(maxUploadSizeValue));
		try
		{
			model.addAttribute(PentlandsupportticketingaddonConstants.SUPPORT_TICKET_ASSOCIATED_OBJECTS, ticketFacade.getAssociatedToObjects());
			model.addAttribute(PentlandsupportticketingaddonConstants.SUPPORT_TICKET_CATEGORIES, ticketFacade.getTicketCategories());
		}
		catch (final UnsupportedOperationException ex)
		{
			LOG.error(ex.getMessage(), ex);
		}


		return getViewForPage(model);
	}


	/**
	 * Creates a ticket.
	 *
	 * @param supportTicketForm
	 * @param bindingResult
	 * @return View String
	 */
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequireHardLogIn
	@ResponseBody
	public ResponseEntity<Map<String, String>> addSupportTicket(final SupportTicketForm supportTicketForm,
			final BindingResult bindingResult)
	{
		validator.validate(supportTicketForm, bindingResult);
		if (bindingResult.hasErrors())
		{
			final Map<String, String> map = buildErrorMessagesMap(bindingResult);

			return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
		}

		try
		{
			TicketData newTicket = ticketFacade.createTicket(this.populateTicketData(supportTicketForm));
			return new ResponseEntity<>(getSuccessMessageMap(newTicket), HttpStatus.OK);
		}
		catch (final UnsupportedAttachmentException usAttEx)
		{
			LOG.error(usAttEx.getMessage(), usAttEx);
			final Map<String, String> map = Maps.newHashMap();
			map.put(PentlandsupportticketingaddonConstants.SUPPORT_TICKET_TRY_LATER, getMessageSource()
					.getMessage(PentlandsupportticketingaddonConstants.TEXT_SUPPORT_TICKETING_ATTACHMENT_BLOCK_LISTED, new Object[]
			{ allowedUploadedFormats }, getI18nService().getCurrentLocale()));
			return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
		}
		catch (final RuntimeException rEX)
		{
			final Map<String, String> map = Maps.newHashMap();
			LOG.error(rEX.getMessage(), rEX);

			map.put(PentlandsupportticketingaddonConstants.SUPPORT_TICKET_TRY_LATER, getMessageSource().getMessage(
					PentlandsupportticketingaddonConstants.TEXT_SUPPORT_TICKETING_TRY_LATER, null, getI18nService().getCurrentLocale()));
			return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
		}

	}

	private Map<String, String> getSuccessMessageMap(TicketData ticket) {
		final Map<String, String> map = Maps.newHashMap();
		map.put(PentlandsupportticketingaddonConstants.SUPPORT_TICKET_SUCCESS, getMessageSource().getMessage(
			PentlandsupportticketingaddonConstants.TEXT_SUPPORT_TICKET_SUCCESS, new Object[]{ticket.getId()}, getI18nService().getCurrentLocale()));
		return map;
	}

	/**
	 * Populated the data from the form bean to ticket data object.
	 *
	 * @param supportTicketForm
	 * @return TicketData
	 */
	protected TicketData populateTicketData(final SupportTicketForm supportTicketForm) // TODO: should be moved to populator class
	{
		final TicketData ticketData = new TicketData();
		if (cartFacade.hasSessionCart())
		{
			final CartData cartData = cartFacade.getSessionCart();
			if (!cartData.getEntries().isEmpty())
			{
				ticketData.setCartId(cartData.getCode());
			}
		}
		if (StringUtils.isNotBlank(supportTicketForm.getId()))
		{
			ticketData.setId(supportTicketForm.getId());
		}
		final StatusData status = new StatusData();
		status.setId(supportTicketForm.getStatus());
		ticketData.setStatus(status);
		ticketData.setCustomerId(customerFacade.getCurrentCustomerUid());
		ticketData.setSubject(supportTicketForm.getSubject());
		ticketData.setMessage(supportTicketForm.getMessage());
		ticketData.setAssociatedTo(supportTicketForm.getAssociatedTo());
		ticketData.setTicketCategory(supportTicketForm.getTicketCategory());
		ticketData.setAttachments(supportTicketForm.getFiles());
		return ticketData;
	}

	protected List<Breadcrumb> getBreadcrumbs(final String breadcrumbCode)
	{
		final List<Breadcrumb> breadcrumbs = accountBreadcrumbBuilder.getBreadcrumbs(null);
		breadcrumbs.add(
				new Breadcrumb("#", getMessageSource().getMessage(breadcrumbCode, null, getI18nService().getCurrentLocale()), null));
		return breadcrumbs;
	}

	/**
	 * Build the error message list with map contains the validation error code and localised message.
	 *
	 * @param bindingResult
	 * @return Map of error code and message
	 */
	protected Map<String, String> buildErrorMessagesMap(final BindingResult bindingResult)
	{
		return bindingResult.getAllErrors()
		                    .stream()
		                    .filter(err -> err.getCode() != null && err.getCode().length() > 0)
		                    .map(err ->
												{
													final Map<String, String> map = Maps.newHashMap();
													map.put(err.getCodes()[0].replaceAll("\\.", "-"), err.getDefaultMessage());
													return map;
												})
		                    .reduce(new HashMap<>(), (k,v)-> {
												k.putAll(v);
												return k;
												});
	}

	/**
	 * Build a map with key and localized Message.
	 *
	 * @param key
	 *           the render key
	 * @param localisedKey
	 *           the localised message key
	 * @return Map of error code and message
	 */
	protected Map<String, String> buildMessageMap(final String key, final String localisedKey)
	{
		final Map<String, String> map = Maps.newHashMap();
		map.put(key, getMessageSource().getMessage(localisedKey, null, getI18nService().getCurrentLocale()));

		return map;
	}
}