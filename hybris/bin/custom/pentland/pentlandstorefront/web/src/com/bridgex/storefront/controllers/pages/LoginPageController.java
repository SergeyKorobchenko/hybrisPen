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
package com.bridgex.storefront.controllers.pages;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bridgex.facades.customer.PentlandCustomerRegFacade;
import com.bridgex.storefront.controllers.ControllerConstants;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractLoginPageController;
import de.hybris.platform.acceleratorstorefrontcommons.forms.RegisterForm;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.commercefacades.user.data.RegisterData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;


/**
 * Login Controller. Handles login and register for the account flow.
 */
@Controller
@RequestMapping(value = "/login")
public class LoginPageController extends AbstractLoginPageController
{
	 private static final Logger LOG = Logger.getLogger(LoginPageController.class);
	private HttpSessionRequestCache httpSessionRequestCache;
	
	@Resource(name="pentlandCustomerRegFacade")
	 PentlandCustomerRegFacade pentlandCustomerRegFacade;
	 
	 @Resource(name = "pentlandRegistrationValidator")
	 private Validator pentlandRegistrationValidator;

	@Override
	protected String getView()
	{
		return ControllerConstants.Views.Pages.Account.AccountLoginPage;
	}

	@Override
	protected String getSuccessRedirect(final HttpServletRequest request, final HttpServletResponse response)
	{
		if (httpSessionRequestCache.getRequest(request, response) != null)
		{
			return httpSessionRequestCache.getRequest(request, response).getRedirectUrl();
		}
		return "/";
	}

	@Override
	protected AbstractPageModel getCmsPage() throws CMSItemNotFoundException
	{
		return getContentPageForLabelOrId("login");
	}


	@Resource(name = "httpSessionRequestCache")
	public void setHttpSessionRequestCache(final HttpSessionRequestCache accHttpSessionRequestCache)
	{
		this.httpSessionRequestCache = accHttpSessionRequestCache;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String doLogin(@RequestHeader(value = "referer", required = false) final String referer,
			@RequestParam(value = "error", defaultValue = "false") final boolean loginError, final Model model,
			final HttpServletRequest request, final HttpServletResponse response, final HttpSession session)
			throws CMSItemNotFoundException
	{
		if (!loginError)
		{
			storeReferer(referer, request, response);
		}
		return getDefaultLoginPage(loginError, session, model);
	}

	protected void storeReferer(final String referer, final HttpServletRequest request, final HttpServletResponse response)
	{
		if (StringUtils.isNotBlank(referer) && !StringUtils.endsWith(referer, "/login")
				&& StringUtils.contains(referer, request.getServerName()))
		{
			httpSessionRequestCache.saveRequest(request, response);
		}
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String doRegister(@RequestHeader(value = "referer", required = false) final String referer, final RegisterForm form,
			final BindingResult bindingResult, final Model model, final HttpServletRequest request,
			final HttpServletResponse response, final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		getRegistrationValidator().validate(form, bindingResult);
		return processRegisterUserRequest(referer, form, bindingResult, model, request, response, redirectModel);
	}
	
	@RequestMapping(value = "/registerForAccess", method = RequestMethod.GET)
	 public String getRegisterPage(final Model model) throws CMSItemNotFoundException {

	  LOG.info("at registerForAccess.. method");
	  PentlandCustomerRegistrationForm pentlandCustomerRegistrationForm = new PentlandCustomerRegistrationForm();
	  model.addAttribute("pentlandCustomerRegistrationForm", pentlandCustomerRegistrationForm);
	  return ControllerConstants.Views.Pages.Footer.RegisterforAccessPage;
	 }

	 @RequestMapping(value = "/registered", method = RequestMethod.POST)
	 public String postFormData(PentlandCustomerRegistrationForm pentlandCustomerRegistrationForm, final Model model,
	   final BindingResult bindingResult, final HttpServletRequest request, final HttpServletResponse response) {
	  LOG.info("at registered.. method");
	  pentlandRegistrationValidator.validate(pentlandCustomerRegistrationForm, bindingResult);
	  if (bindingResult.hasErrors()) {
	   model.addAttribute(pentlandCustomerRegistrationForm);
	   // GlobalMessages.addErrorMessage(model, FORM_GLOBAL_ERROR);
	   return ControllerConstants.Views.Pages.Footer.RegisterforAccessPage;
	  }
	  try {

	   final RegisterData data = new RegisterData();
	   data.setFirstName(pentlandCustomerRegistrationForm.getFirstName());
	   data.setLastName(pentlandCustomerRegistrationForm.getLastName());
	   data.setPosition(pentlandCustomerRegistrationForm.getPosition());
	   data.setAccessRequired(pentlandCustomerRegistrationForm.getAccessRequired());
	   data.setAccountNumber(pentlandCustomerRegistrationForm.getAccountNumber());
	   data.setUserEmail(pentlandCustomerRegistrationForm.getEmail());

	   model.addAttribute("pentlandCustomerRegistrationForm", pentlandCustomerRegistrationForm);

	   if (null != data) {

	    pentlandCustomerRegFacade.saveFormData(data);

	   }
	   return ControllerConstants.Views.Pages.Footer.SuccessRegister;

	  } catch (DuplicateUidException e) {

	   LOG.error("fail to register:"+e);
	   return ControllerConstants.Views.Pages.Footer.FailRegister;
	  }

	 }
}
