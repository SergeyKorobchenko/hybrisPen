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
package com.bridgex.pentlandaccountsummaryaddon.controllers.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bridgex.pentlandaccountsummaryaddon.constants.PentlandaccountsummaryaddonConstants;
import com.bridgex.pentlandaccountsummaryaddon.data.AccountSummaryInfoData;
import com.bridgex.pentlandaccountsummaryaddon.document.criteria.DefaultCriteria;
import com.bridgex.pentlandaccountsummaryaddon.document.criteria.FilterByCriteriaData;
import com.bridgex.pentlandaccountsummaryaddon.document.data.B2BDocumentData;
import com.bridgex.pentlandaccountsummaryaddon.facade.B2BAccountSummaryFacade;
import com.bridgex.pentlandaccountsummaryaddon.breadcrumb.impl.AccountSummaryMyCompanyBreadcrumbBuilder;
import com.bridgex.pentlandaccountsummaryaddon.model.B2BDocumentModel;
import com.bridgex.pentlandaccountsummaryaddon.model.B2BDocumentTypeModel;
import com.bridgex.pentlandaccountsummaryaddon.utils.PentlandaccountsummaryaddonUtils;
import com.bridgex.pentlandaccountsummaryaddon.document.criteria.validator.CriteriaValidator;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.ThirdPartyConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractSearchPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.util.XSSFilterUtil;
import de.hybris.platform.b2bacceleratorfacades.customer.impl.DefaultB2BCustomerFacade;
import de.hybris.platform.b2bcommercefacades.company.B2BUnitFacade;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.util.Config;

/**
 * Controller for organization management.
 */

@Controller
public class AccountSummaryPageController extends AbstractSearchPageController
{
	private static final Logger LOG = Logger.getLogger(AccountSummaryPageController.class);

	private static final String DOCUMENT_TYPE_LIST = "documentTypeList";
	private static final String ACCOUNTSUMMARY_UNIT_URL = "/my-company/account-summary/";

	@Resource(name = "accountSummaryMyCompanyBreadcrumbBuilder")
	protected AccountSummaryMyCompanyBreadcrumbBuilder myCompanyBreadcrumbBuilder;

	@Resource(name = "b2bAccountSummaryFacade")
	protected B2BAccountSummaryFacade b2bAccountSummaryFacade;

	@Resource(name = "b2bUnitFacade")
	protected B2BUnitFacade b2bUnitFacade;

	@Resource(name = "filterByList")
	private Map<String, DefaultCriteria> filterByList;

	@Resource(name = "customerFacade")
	protected CustomerFacade customerFacade;

	@Resource
	private Map<String, CriteriaValidator> validatorMapping;

	@RequestMapping(value = ACCOUNTSUMMARY_UNIT_URL, method = RequestMethod.GET)
	@RequireHardLogIn
	public String accountSummaryUnitDetails(@RequestParam(value = "page", defaultValue = "0") final int page,
	                                        @RequestParam(value = "sort", required = false) final String sortCode,
	                                        @RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
	                                        @RequestParam(value = "startRange", defaultValue = StringUtils.EMPTY) final String startRange,
	                                        @RequestParam(value = "endRange", defaultValue = StringUtils.EMPTY) final String endRange,
	                                        @RequestParam(value = "documentTypeCode", defaultValue = StringUtils.EMPTY) final String documentTypeCode,
	                                        @RequestParam(value = "documentStatus", defaultValue = "OPEN") final String documentStatus,
	                                        @RequestParam(value = "filterByKey", defaultValue = B2BDocumentModel.DOCUMENTNUMBER) final String filterByKey,
	                                        @RequestParam(value = "filterByValue", defaultValue = StringUtils.EMPTY) final String filterByValue, final Model model,
	                                        final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		try
		{
			final AccountSummaryInfoData accountSummaryInfoData = b2bAccountSummaryFacade.getAccountSummaryInfoData("");
			model.addAttribute("accountSummaryInfoData", accountSummaryInfoData);
		}
		catch (final UnknownIdentifierException uie)
		{
			LOG.warn("Attempted to load a unit that does not exists");
			GlobalMessages.addFlashMessage(redirectModel, GlobalMessages.ERROR_MESSAGES_HOLDER, "b2bunit.notfound", null);
			return REDIRECT_PREFIX + "/";
		}

		prepareDocumentTypeList(model);

		final String encodedFilterByValue = sanitizeIncomingValues(filterByValue);
		final String encodedDocumentTypeCode = sanitizeIncomingValues(documentTypeCode);
		final String encodedStartRange = sanitizeIncomingValues(startRange);
		final String encodedEndRange = sanitizeIncomingValues(endRange);

		final FilterByCriteriaData filterByCriteriaData = createFilterByCriteriaData(encodedStartRange, encodedEndRange,
		                                                                             encodedDocumentTypeCode, documentStatus, encodedFilterByValue);

		if (!filterByList.containsKey(filterByKey))
		{
			GlobalMessages.addErrorMessage(model, "text.company.accountsummary.invalid.criteria");
		}
		else
		{
			final boolean valid = validatorMapping.containsKey(filterByKey) ? validatorMapping.get(filterByKey).isValid(
				encodedStartRange, encodedEndRange, model) : true;

			if (valid)
			{
				// Handle paged search results
				final PageableData pageableData = createPageableData(page,
				                                                     Config.getInt("accountsummary.unit.documents.search.page.size", 5), sortCode, showMode);

				final DefaultCriteria defaultCriteria = filterByList.get(filterByKey);
				final SearchPageData<B2BDocumentData> searchPageData = b2bAccountSummaryFacade.getPagedDocumentsForUnit(unit,
				                                                                                                        pageableData, filterByCriteriaData, defaultCriteria);
				populateModel(model, searchPageData, showMode);
			}
		}
		model.addAttribute("dateFormat", PentlandaccountsummaryaddonConstants.DATE_FORMAT_MM_DD_YYYY);
		model.addAttribute("filterByList", filterByList.keySet());
		model.addAttribute("documentStatusList", PentlandaccountsummaryaddonUtils.getDocumentStatusList());
		model.addAttribute("filterByKey", filterByKey);
		model.addAttribute("criteriaData", filterByCriteriaData);
		storeCmsPageInModel(model,
		                    getContentPageForLabelOrId(PentlandaccountsummaryaddonConstants.ACCOUNT_SUMMARY_UNIT_DETAILS_LIST_CMS_PAGE));
		setUpMetaDataForContentPage(model,
		                            getContentPageForLabelOrId(PentlandaccountsummaryaddonConstants.ACCOUNT_SUMMARY_UNIT_DETAILS_LIST_CMS_PAGE));
		model.addAttribute("breadcrumbs", myCompanyBreadcrumbBuilder.createAccountSummaryDetailsBreadcrumbs(unit));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);

		return getViewForPage(model);
	}

	protected String sanitizeIncomingValues(final String inputValue)
	{
		return StringUtils.isNotBlank(inputValue) ? XSSFilterUtil.filter(inputValue) : inputValue;
	}

	protected void prepareDocumentTypeList(final Model model)
	{
		List<B2BDocumentTypeModel> documentTypeList = b2bAccountSummaryFacade.getAllDocumentTypes().getResult();
		if (documentTypeList == null)
		{
			documentTypeList = new ArrayList<B2BDocumentTypeModel>();
		}
		model.addAttribute(DOCUMENT_TYPE_LIST, documentTypeList);
	}

	protected FilterByCriteriaData createFilterByCriteriaData(final String startRange, final String endRange,
	                                                          final String documentTypeCode, final String documentStatus, final String filterByValue)
	{
		final FilterByCriteriaData filterByCriteriaData = new FilterByCriteriaData();
		filterByCriteriaData.setStartRange(startRange);
		filterByCriteriaData.setEndRange(endRange);
		filterByCriteriaData.setDocumentTypeCode(documentTypeCode);
		filterByCriteriaData.setDocumentStatus(documentStatus);
		filterByCriteriaData.setFilterByValue(filterByValue);

		return filterByCriteriaData;
	}
}
