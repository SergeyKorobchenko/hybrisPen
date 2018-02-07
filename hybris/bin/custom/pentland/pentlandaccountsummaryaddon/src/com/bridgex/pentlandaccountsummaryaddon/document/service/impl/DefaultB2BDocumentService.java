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

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateIfAnyResult;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.client.ResourceAccessException;

import com.bridgex.core.integration.PentlandIntegrationService;
import com.bridgex.integration.domain.DocumentDto;
import com.bridgex.integration.domain.DocumentResponse;
import com.bridgex.pentlandaccountsummaryaddon.document.AccountSummaryDocumentQuery;
import com.bridgex.pentlandaccountsummaryaddon.document.criteria.DefaultCriteria;
import com.bridgex.pentlandaccountsummaryaddon.document.dao.B2BDocumentDao;
import com.bridgex.pentlandaccountsummaryaddon.document.dao.PagedB2BDocumentDao;
import com.bridgex.pentlandaccountsummaryaddon.document.service.B2BDocumentService;
import com.bridgex.pentlandaccountsummaryaddon.enums.DocumentStatus;
import com.bridgex.pentlandaccountsummaryaddon.model.B2BDocumentModel;
import com.bridgex.pentlandaccountsummaryaddon.model.B2BDocumentTypeModel;
import com.bridgex.pentlandaccountsummaryaddon.model.DocumentMediaModel;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.SearchResult;

/**
 * Provides services for B2BDocument business logic/domain
 *
 */
public class DefaultB2BDocumentService implements B2BDocumentService
{
	private PagedB2BDocumentDao        pagedB2BDocumentDao;
	private B2BDocumentDao             b2bDocumentDao;
	private ModelService               modelService;
	private PentlandIntegrationService<DocumentDto,DocumentResponse> invoicePDFService;
	private MediaService               mediaService;

	private static final Logger LOG = Logger.getLogger(com.bridgex.pentlandaccountsummaryaddon.document.service.impl.DefaultB2BDocumentService.class.getName());

	@Required
	public void setMediaService(MediaService mediaService) {
		this.mediaService = mediaService;
	}

	@Override
	public SearchPageData<B2BDocumentModel> findDocuments(final AccountSummaryDocumentQuery query)
	{
		return getPagedB2BDocumentDao().findDocuments(query);
	}

	@Override
	public SearchResult<B2BDocumentModel> getOpenDocuments(final B2BUnitModel unit)
	{
		return getB2bDocumentDao().getOpenDocuments(unit);
	}

	@Override
	public SearchResult<B2BDocumentModel> getOpenDocuments(final MediaModel mediaModel)
	{
		return getB2bDocumentDao().getOpenDocuments(mediaModel);
	}

	@Required
	public void setPagedB2BDocumentDao(final PagedB2BDocumentDao pagedB2BDocumentDao)
	{
		this.pagedB2BDocumentDao = pagedB2BDocumentDao;
	}

	@Required
	public void setB2bDocumentDao(final B2BDocumentDao b2bDocumentDao)
	{
		this.b2bDocumentDao = b2bDocumentDao;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Required
	public void setInvoicePDFService(PentlandIntegrationService<DocumentDto, DocumentResponse> invoicePDFService) {
		this.invoicePDFService = invoicePDFService;
	}

	@Override
	public void deleteB2BDocumentFiles(final int numberOfDay, final List<B2BDocumentTypeModel> documentTypes,
			final List<DocumentStatus> documentStatuses)
	{
		final SearchResult<DocumentMediaModel> mediaList = getB2bDocumentDao().findOldDocumentMedia(numberOfDay, documentTypes,
				documentStatuses);

		for (final DocumentMediaModel media : mediaList.getResult())
		{
			MediaManager.getInstance().deleteMedia(media.getFolder().getQualifier(), media.getLocation());
			try {
				LOG.debug("[deleteB2BDocumentFiles] remove " + media.getLocation());
				getModelService().remove(media);
			}
			catch (final ModelRemovalException e) {
				LOG.error("[deleteB2BDocumentFiles]" + e);
			}
		}
	}


	@Override
	public void deleteB2BDocuments(int numberOfDay, List<B2BDocumentTypeModel> documentTypeList, List<DocumentStatus> documentStatusList) {
		SearchResult<B2BDocumentModel> models = getB2bDocumentDao().findOldDocuments(numberOfDay,documentStatusList, documentTypeList);
		try {
			modelService.removeAll(models);
		} catch (ModelRemovalException e) {
				LOG.error("[deleteB2BDocuments]" + e);
		}
	}

	@Override
	public SearchPageData<B2BDocumentModel> getPagedDocumentsForUnit(final String b2bUnitCode, final PageableData pageableData,
			final List<DefaultCriteria> criteriaList)
	{
		validateParameterNotNull(b2bUnitCode, "b2bUnitCode must not be null");
		validateParameterNotNull(pageableData, "pageableData must not be null");
		validateIfAnyResult(criteriaList, "criteria list must not be empty or null");
		return getPagedB2BDocumentDao().getPagedDocumentsForUnit(b2bUnitCode, pageableData, criteriaList);
	}

	@Override
	public SearchPageData<B2BDocumentModel> getPagedDocumentsForSapIds(Set<String> sapId, PageableData pageableData, List<DefaultCriteria> filterByCriteriaList) {
		validateParameterNotNull(sapId, "b2bUnitCode must not be null");
		validateParameterNotNull(pageableData, "pageableData must not be null");
		validateIfAnyResult(filterByCriteriaList, "criteria list must not be empty or null");

		return getPagedB2BDocumentDao().getPagedDocumentsForSapIds(sapId, pageableData, filterByCriteriaList);
	}

	@Override
	public DocumentMediaModel getDocumentMediaByNumber(String documentNumber) {
		validateParameterNotNull(documentNumber, "document number must not be null");
		B2BDocumentModel documentModel = b2bDocumentDao.getDocumentByNumber(documentNumber);
		DocumentMediaModel mediaModel = documentModel.getDocumentMedia();

		if (mediaModel != null) {
			return mediaModel;
		}
		DocumentMediaModel newMedia = requestAndSaveDocumentMedia(documentNumber);
		if(newMedia != null) {
			documentModel.setDocumentMedia(newMedia);
			modelService.saveAll();
			return newMedia;
		}else{
			return null;
		}
	}

	private DocumentMediaModel requestAndSaveDocumentMedia(String documentNumber) {
		try {
			DocumentResponse response = invoicePDFService.requestData(createDocumentDto(documentNumber));
			DocumentMediaModel newMedia = modelService.create(DocumentMediaModel.class);
			newMedia.setCode("invoice-" + documentNumber);
			modelService.save(newMedia);
			mediaService.setStreamForMedia(newMedia, new ByteArrayInputStream(response.getEtOutput().getBinaryData()), documentNumber + ".pdf", "application/pdf", mediaService.getFolder("accountsummary"));
			return newMedia;
		}catch(ResourceAccessException e){
			LOG.info("Error generating PDF for document " + documentNumber + ". " + e.getMessage());
			return null;
		}
	}

	private DocumentDto createDocumentDto(String documentNumber) {
		DocumentDto request = new DocumentDto();
		request.setInvoiceCode(documentNumber);
		return request;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	protected PagedB2BDocumentDao getPagedB2BDocumentDao()
	{
		return pagedB2BDocumentDao;
	}


	protected B2BDocumentDao getB2bDocumentDao()
	{
		return b2bDocumentDao;
	}
}
