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
package com.bridgex.pentlandaccountsummaryaddon.document.service.impl;

import org.springframework.beans.factory.annotation.Required;

import com.bridgex.pentlandaccountsummaryaddon.document.dao.B2BDocumentTypeDao;
import com.bridgex.pentlandaccountsummaryaddon.document.service.B2BDocumentTypeService;
import com.bridgex.pentlandaccountsummaryaddon.model.B2BDocumentTypeModel;

import de.hybris.platform.servicelayer.search.SearchResult;

/**
 * Provides services for B2B document type.
 *
 */
public class DefaultB2BDocumentTypeService implements B2BDocumentTypeService
{
	private B2BDocumentTypeDao b2bDocumentTypeDao;

	@Override
	public SearchResult<B2BDocumentTypeModel> getAllDocumentTypes()
	{
		return getB2bDocumentTypeDao().getAllDocumentTypes();
	}

	@Required
	public void setB2bDocumentTypeDao(final B2BDocumentTypeDao b2bDocumentTypeDao)
	{
		this.b2bDocumentTypeDao = b2bDocumentTypeDao;
	}

	protected B2BDocumentTypeDao getB2bDocumentTypeDao()
	{
		return b2bDocumentTypeDao;
	}
}
