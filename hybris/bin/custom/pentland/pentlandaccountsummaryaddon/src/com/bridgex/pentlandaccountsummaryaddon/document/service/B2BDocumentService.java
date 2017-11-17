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
package com.bridgex.pentlandaccountsummaryaddon.document.service;

import java.util.List;
import java.util.Set;

import com.bridgex.pentlandaccountsummaryaddon.document.AccountSummaryDocumentQuery;
import com.bridgex.pentlandaccountsummaryaddon.document.criteria.DefaultCriteria;
import com.bridgex.pentlandaccountsummaryaddon.enums.DocumentStatus;
import com.bridgex.pentlandaccountsummaryaddon.jalo.B2BDocument;
import com.bridgex.pentlandaccountsummaryaddon.model.B2BDocumentModel;
import com.bridgex.pentlandaccountsummaryaddon.model.B2BDocumentTypeModel;
import com.bridgex.pentlandaccountsummaryaddon.model.DocumentMediaModel;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.search.SearchResult;

/**
 * Provides services for B2BDocument business logic/domain
 *
 */
public interface B2BDocumentService
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
	 * Return all open documents for a given B2B Unit.
	 *
	 * @param unit
	 *           the B2B unit.
	 * @return result : a SeachPageData< B2BDocumentModel > containing open documents.
	 */
	SearchResult<B2BDocumentModel> getOpenDocuments(final B2BUnitModel unit);

	/**
	 * Returns all open documents for a given documentMediaModel
	 *
	 * @param mediaModel
	 *           the media model
	 * @return result : a SeachPageData< B2BDocumentModel > containing open documents.
	 */
	SearchResult<B2BDocumentModel> getOpenDocuments(final MediaModel mediaModel);

	/**
	 * @param numberOfDays
	 *           elapsed days since the document media's creation time
	 * @param documentTypes
	 *           a list of document types
	 * @param documentStatuses
	 *           a list of document statuses
	 */
	void deleteB2BDocumentFiles(final int numberOfDays, final List<B2BDocumentTypeModel> documentTypes, final List<DocumentStatus> documentStatuses);


	/**
	 * Returns paged documents for the given unit
	 *
	 * @param b2bUnitCode
	 *           the unit code
	 * @param pageableData
	 *           the pageable data
	 * @param criteriaList
	 *           the list of criteria objects
	 * @return result : a SeachPageData< B2BDocumentModel > containing documents from the given unit.
	 */
	SearchPageData<B2BDocumentModel> getPagedDocumentsForUnit(final String b2bUnitCode, final PageableData pageableData, final List<DefaultCriteria> criteriaList);

  SearchPageData<B2BDocumentModel> getPagedDocumentsForSapIds(Set<String> sapIds, PageableData pageableData, List<DefaultCriteria> filterByCriteriaList);

	DocumentMediaModel getDocumentMediaByNumber(String documentNumber);

	void deleteB2BDocuments(int numberOfDay, List<B2BDocumentTypeModel> documentTypeList, List<DocumentStatus> documentStatusList);
}
