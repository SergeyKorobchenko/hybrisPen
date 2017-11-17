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
package com.bridgex.pentlandaccountsummaryaddon.document.dao.impl;

import org.apache.log4j.Logger;

import com.bridgex.pentlandaccountsummaryaddon.document.dao.B2BDocumentPaymentInfoDao;
import com.bridgex.pentlandaccountsummaryaddon.model.B2BDocumentModel;
import com.bridgex.pentlandaccountsummaryaddon.model.B2BDocumentPaymentInfoModel;

import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

public class DefaultB2BDocumentPaymentInfoDao extends DefaultGenericDao<B2BDocumentPaymentInfoModel> implements B2BDocumentPaymentInfoDao
{

	private static final String DOCUMENT_NUMBER = "documentNumber";
	private static final String FLEX_QUERY_PAYMENT_INFO = String
			.format(
					"select {b1:pk} from { %s as b1 join %s as b2 ON {b1:payDocument}={b2:pk} } WHERE {b2:documentNumber}=?documentNumber ORDER BY {b1:date} DESC",
					B2BDocumentPaymentInfoModel._TYPECODE, B2BDocumentModel._TYPECODE, B2BDocumentModel._TYPECODE);
	private static final Logger LOG = Logger.getLogger(com.bridgex.pentlandaccountsummaryaddon.document.dao.impl.DefaultB2BDocumentPaymentInfoDao.class.getName());

	public DefaultB2BDocumentPaymentInfoDao()
	{
		super(B2BDocumentPaymentInfoModel._TYPECODE);
	}

	@Override
	public SearchResult<B2BDocumentPaymentInfoModel> getDocumentPaymentInfo(final String documentNumber)
	{

		final FlexibleSearchQuery query = new FlexibleSearchQuery(FLEX_QUERY_PAYMENT_INFO);
		query.addQueryParameter(DOCUMENT_NUMBER, documentNumber);

		return getFlexibleSearchService().search(query);
	}
}
