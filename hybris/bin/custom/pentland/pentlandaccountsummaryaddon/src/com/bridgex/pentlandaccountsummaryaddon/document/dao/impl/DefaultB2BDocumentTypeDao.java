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

import com.bridgex.pentlandaccountsummaryaddon.document.dao.B2BDocumentTypeDao;
import com.bridgex.pentlandaccountsummaryaddon.model.B2BDocumentTypeModel;

import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

public class DefaultB2BDocumentTypeDao extends DefaultGenericDao<B2BDocumentTypeModel> implements B2BDocumentTypeDao
{

	private static final String FIND_ALL_DOCUMENT_TYPE = "SELECT {" + B2BDocumentTypeModel._TYPECODE + ":pk}  FROM { "
			+ B2BDocumentTypeModel._TYPECODE + " } ";

	public DefaultB2BDocumentTypeDao()
	{
		super(B2BDocumentTypeModel._TYPECODE);
	}

	@Override
	public SearchResult<B2BDocumentTypeModel> getAllDocumentTypes()
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_ALL_DOCUMENT_TYPE);
		return getFlexibleSearchService().search(query);
	}

}
