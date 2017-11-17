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
package com.bridgex.pentlandaccountsummaryaddon.document.dao;

import java.util.List;
import java.util.Set;

import com.bridgex.pentlandaccountsummaryaddon.document.AccountSummaryDocumentQuery;
import com.bridgex.pentlandaccountsummaryaddon.document.criteria.DefaultCriteria;
import com.bridgex.pentlandaccountsummaryaddon.model.B2BDocumentModel;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;

public interface PagedB2BDocumentDao
{

	/**
	 * Finds all B2BDocumentModel filtered by the query. The resulting list only contains document associated to the
	 * current user's B2BUnit.
	 *
	 * @param query
	 *           paged document query
	 * @return result : a SeachPageData< B2BDocumentModel > containing documents.
	 */
	SearchPageData<B2BDocumentModel> findDocuments(final AccountSummaryDocumentQuery query);

	/**
	 * @param b2bUnitCode
	 *           the unit code
	 * @param pageableData
	 *           the pageable data object
	 * @param criteriaList
	 *           the list of filter by criteria object
	 * @return result : a SeachPageData<B2BDocumentModel> containing documents of the given unit & criteria.
	 */
	SearchPageData<B2BDocumentModel> getPagedDocumentsForUnit(final String b2bUnitCode, final PageableData pageableData, final List<DefaultCriteria> criteriaList);

	/**
	 * @param pageableData
	 *           the pageable data object
	 * @param criteriaList
	 *           the list of filter by criteria object
	 * @return result : a SeachPageData<B2BDocumentModel> containing documents of the given unit & criteria.
	 */
	SearchPageData<B2BDocumentModel> getAllPagedDocuments(final PageableData pageableData, final List<DefaultCriteria> criteriaList);

	SearchPageData<B2BDocumentModel> getPagedDocumentsForSapIds(Set<String> sapId, PageableData pageableData, List<DefaultCriteria> filterByCriteriaList);
}
