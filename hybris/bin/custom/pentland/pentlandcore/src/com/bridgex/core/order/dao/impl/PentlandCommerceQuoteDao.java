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
package com.bridgex.core.order.dao.impl;

import java.util.*;

import de.hybris.platform.commerceservices.order.dao.CommerceQuoteDao;
import de.hybris.platform.commerceservices.order.dao.impl.DefaultCommerceQuoteDao;
import de.hybris.platform.commerceservices.search.flexiblesearch.data.SortQueryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.QuoteState;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.store.BaseStoreModel;

/**
 * @author Goncharenko Mikhail, created on 09.01.2018.
 */
public class PentlandCommerceQuoteDao extends DefaultCommerceQuoteDao
{
	private static final String FIND_QUOTES_BY_CUSTOMER_STORE_CODE_MAX_VERSION_QUERY = "SELECT {q1:" + QuoteModel.PK + "} FROM {"
			+ QuoteModel._TYPECODE + " as q1} WHERE {q1:" + QuoteModel.STATE + "} IN (?quoteStates) AND {q1:" + QuoteModel.USER
			+ "} = ?" + QuoteModel.USER + " AND {q1:" + QuoteModel.STORE + "} = ?" + QuoteModel.STORE + " AND {q1:"
			+ QuoteModel.VERSION + "} = ({{ SELECT MAX({" + QuoteModel.VERSION + "}) FROM {" + QuoteModel._TYPECODE + "} WHERE {"
			+ QuoteModel.CODE + "} = {q1:" + QuoteModel.CODE + "} AND {" + QuoteModel.STATE + "} IN (?quoteStates) AND {"
			+ QuoteModel.USER + "} = ?" + QuoteModel.USER + " AND {" + QuoteModel.STORE + "} = ?" + QuoteModel.STORE + "}}) ";

	private static final String ORDER_BY_QUOTE_CODE_DESC = " ORDER BY {q1:" + QuoteModel.CODE + "} DESC";
	private static final String ORDER_BY_QUOTE_NAME_DESC = " ORDER BY {q1:" + QuoteModel.NAME + "} DESC";
	private static final String ORDER_BY_QUOTE_DATE_DESC = " ORDER BY {q1:" + QuoteModel.MODIFIEDTIME + "} DESC";
	private static final String ORDER_BY_QUOTE_STATE = " ORDER BY {q1:" + QuoteModel.STATE + "} DESC";

	@Override
	public SearchPageData<QuoteModel> findQuotesByCustomerAndStore(final CustomerModel customerModel, final BaseStoreModel store,
			final PageableData pageableData, final Set<QuoteState> quoteStates)
	{
		validateUserAndStoreAndStates(store, customerModel, quoteStates);

		final Map<String, Object> queryParams = populateBasicQueryParams(store, customerModel, quoteStates);
		final List<SortQueryData> sortQueries = createSortQueryData();
		return getPagedFlexibleSearchService().search(sortQueries, "byCode", queryParams, pageableData);
	}

	private List<SortQueryData> createSortQueryData() {
		List<SortQueryData> sortQueries;
		sortQueries = Arrays.asList(
				createSortQueryData("byName",
				    createQuery(FIND_QUOTES_BY_CUSTOMER_STORE_CODE_MAX_VERSION_QUERY, ORDER_BY_QUOTE_NAME_DESC)),
				createSortQueryData("byCode",
						createQuery(FIND_QUOTES_BY_CUSTOMER_STORE_CODE_MAX_VERSION_QUERY, ORDER_BY_QUOTE_CODE_DESC)),
				createSortQueryData("byState",
						createQuery(FIND_QUOTES_BY_CUSTOMER_STORE_CODE_MAX_VERSION_QUERY, ORDER_BY_QUOTE_STATE)),
				createSortQueryData("byDate",
			      createQuery(FIND_QUOTES_BY_CUSTOMER_STORE_CODE_MAX_VERSION_QUERY, ORDER_BY_QUOTE_DATE_DESC)));
		return sortQueries;
	}

}
