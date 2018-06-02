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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bridgex.facades.customer.PentlandCustomerRegFacade;
import com.bridgex.storefront.controllers.ControllerConstants;

import de.hybris.platform.acceleratorstorefrontcommons.breadcrumb.Breadcrumb;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractLoginPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.RegisterForm;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commercefacades.user.data.RegisterData;
import de.hybris.platform.commercefacades.user.data.TitleData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;


/**
 * Login Controller. Handles login and register for the account flow.
 */
@Controller
@RequestMapping(value = "/login")
public class LoginPageController extends AbstractLoginPageController
{
	 private static final Logger LOG = Logger.getLogger(LoginPageController.class);
	 public final static String CMS_REGISTER_ACCESS_PAGE_NAME = "SecureCustomerAccessRegisterPage";
	 public final static String CMS_REGISTER_ACCESS_SUCCESS_PAGE_NAME = "SecureCustomerAccessRegisterSuccessPage";
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
	 public String getRegisterPage(final Model model) throws CMSItemNotFoundException
	{
	  LOG.info("at registerForAccess.. method");
	  return getDefaultRegistrationPage(model, getContentPageForLabelOrId(getRegistrationCmsPage()));
	 }
	
	protected String getRegistrationCmsPage()
	{
		return CMS_REGISTER_ACCESS_PAGE_NAME;
	}
	
	protected String getRegisterAccessSuccessCmsPage()
	{
		return CMS_REGISTER_ACCESS_SUCCESS_PAGE_NAME;
	}

	protected String getRegistrationView()
	{
		return ControllerConstants.Views.Pages.Footer.RegisterforAccessPage;
	}
	
	protected String getRegisterAccessSuccessView()
	{
		return ControllerConstants.Views.Pages.Footer.SuccessRegister;
	}
	
	protected String getDefaultRegistrationPage(final Model model, final ContentPageModel contentPageModel)
	{
		populateModelCmsContent(model, contentPageModel);
		model.addAttribute("pentlandCustomerRegistrationForm", new PentlandCustomerRegistrationForm());
		return getRegistrationView();
	}
	
	protected String getDefaultRegisterAccessSuccessPage(final Model model, final ContentPageModel contentPageModel)
	{
		final Breadcrumb registerSuccessBreadcrumbEntry = new Breadcrumb("#",
				getMessageSource().getMessage("breadcrumb.register.access.success", null, getI18nService().getCurrentLocale()), null);
		model.addAttribute("breadcrumbs", Collections.singletonList(registerSuccessBreadcrumbEntry));
		storeCmsPageInModel(model, contentPageModel);
		setUpMetaDataForContentPage(model, contentPageModel);
		return getRegisterAccessSuccessView();
	}
	
	protected void populateModelCmsContent(final Model model, final ContentPageModel contentPageModel)
	{
		storeCmsPageInModel(model, contentPageModel);
		setUpMetaDataForContentPage(model, contentPageModel);

		final Breadcrumb registrationBreadcrumbEntry = new Breadcrumb("#",
				getMessageSource().getMessage("breadcrumb.register.access", null, getI18nService().getCurrentLocale()), null);
		model.addAttribute("breadcrumbs", Collections.singletonList(registrationBreadcrumbEntry));
	}
	
	 @RequestMapping(value = "/registered", method = RequestMethod.POST)
	 public String postFormData(PentlandCustomerRegistrationForm pentlandCustomerRegistrationForm, final Model model,
	   final BindingResult bindingResult, final HttpServletRequest request, final HttpServletResponse response) throws CMSItemNotFoundException {
	  LOG.info("at registered.. method");
	  pentlandRegistrationValidator.validate(pentlandCustomerRegistrationForm, bindingResult);
	  if (bindingResult.hasErrors()) {
	   model.addAttribute(pentlandCustomerRegistrationForm);
	   // GlobalMessages.addErrorMessage(model, FORM_GLOBAL_ERROR);
	   return getDefaultRegistrationPage(model, getContentPageForLabelOrId(getRegistrationCmsPage()));
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
	   return getDefaultRegisterAccessSuccessPage(model, getContentPageForLabelOrId(getRegisterAccessSuccessCmsPage()));

	  } catch (DuplicateUidException e) {

	   LOG.error("fail to register:"+e);
	   GlobalMessages.addMessage(model, GlobalMessages.ERROR_MESSAGES_HOLDER, "register.access.dublicate.mail", new Object[]
				{pentlandCustomerRegistrationForm.getEmail()});
	   return getDefaultRegistrationPage(model, getContentPageForLabelOrId(getRegistrationCmsPage()));
	  }

	 }
}
