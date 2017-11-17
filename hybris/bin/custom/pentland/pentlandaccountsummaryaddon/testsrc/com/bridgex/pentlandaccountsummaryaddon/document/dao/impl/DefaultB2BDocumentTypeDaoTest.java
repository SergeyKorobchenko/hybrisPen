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

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.bridgex.pentlandaccountsummaryaddon.B2BIntegrationTest;
import com.bridgex.pentlandaccountsummaryaddon.document.dao.B2BDocumentTypeDao;
import com.bridgex.pentlandaccountsummaryaddon.model.B2BDocumentTypeModel;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.search.SearchResult;
import junit.framework.TestCase;

@IntegrationTest
public class DefaultB2BDocumentTypeDaoTest extends B2BIntegrationTest
{

	@Resource
	private B2BDocumentTypeDao b2bDocumentTypeDao;

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		importCsv("/pentlandaccountsummaryaddon/test/testB2BDocumentType.csv", "utf-8");
	}

	@Test
	public void shouldReturnAllDocumentType()
	{
		final SearchResult<B2BDocumentTypeModel> result = b2bDocumentTypeDao.getAllDocumentTypes();

		TestCase.assertEquals(5, result.getTotalCount());

		final Set<String> resultSet = new HashSet();
		for (final B2BDocumentTypeModel each : result.getResult())
		{
			resultSet.add(each.getCode());
		}

		TestCase.assertTrue(resultSet.contains("Purchase Order"));
		TestCase.assertTrue(resultSet.contains("Invoice"));
		TestCase.assertTrue(resultSet.contains("Credit Note"));
		TestCase.assertTrue(resultSet.contains("Debit Note"));
		TestCase.assertTrue(resultSet.contains("Statement"));
	}
}
