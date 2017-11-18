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

import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.Test;
import org.mockito.Mockito;

import com.bridgex.pentlandaccountsummaryaddon.document.dao.B2BDocumentTypeDao;
import com.bridgex.pentlandaccountsummaryaddon.model.B2BDocumentTypeModel;

import de.hybris.platform.b2b.mock.HybrisMokitoTest;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.impl.SearchResultImpl;
import junit.framework.TestCase;

public class DefaultB2BDocumentTypeServiceMockTest extends HybrisMokitoTest
{

	@Test
	public void shouldReturnAllDocumentTypes()
	{
		final B2BDocumentTypeDao b2bDocumentTypeDao = mock(B2BDocumentTypeDao.class);

		final B2BDocumentTypeModel type1 = new B2BDocumentTypeModel();
		type1.setCode("type1");
		final B2BDocumentTypeModel type2 = new B2BDocumentTypeModel();
		type1.setCode("type2");
		final B2BDocumentTypeModel type3 = new B2BDocumentTypeModel();
		type1.setCode("type3");

		final SearchResult<B2BDocumentTypeModel> result = new SearchResultImpl<>(Arrays.asList(type1, type2, type3), 3, 3, 1);
		when(b2bDocumentTypeDao.getAllDocumentTypes()).thenReturn(result);

		final DefaultB2BDocumentTypeService defaultB2BDocumentTypeService = new DefaultB2BDocumentTypeService();
		defaultB2BDocumentTypeService.setB2bDocumentTypeDao(b2bDocumentTypeDao);
		final SearchResult<B2BDocumentTypeModel> finalResult = defaultB2BDocumentTypeService.getAllDocumentTypes();

		TestCase.assertEquals(3, finalResult.getResult().size());
		TestCase.assertTrue(finalResult.getResult().contains(type1));
		TestCase.assertTrue(finalResult.getResult().contains(type2));
		TestCase.assertTrue(finalResult.getResult().contains(type3));

		verify(b2bDocumentTypeDao, Mockito.times(1)).getAllDocumentTypes();
	}
}
