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

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.bridgex.pentlandaccountsummaryaddon.document.NumberOfDayRange;
import com.bridgex.pentlandaccountsummaryaddon.document.service.PastDueBalanceDateRangeService;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.util.Config;
import junit.framework.TestCase;

@IntegrationTest
public class DefaultPastDueBalanceRangeServiceTest extends HybrisJUnit4Test
{


	@Before
	public void setUp()
	{
		Config.setParameter("pentlandaccountsummaryaddon.daterange.1.start", "");
		Config.setParameter("pentlandaccountsummaryaddon.daterange.1.end", "");

		Config.setParameter("pentlandaccountsummaryaddon.daterange.2.start", "");
		Config.setParameter("pentlandaccountsummaryaddon.daterange.2.end", "");

		Config.setParameter("pentlandaccountsummaryaddon.daterange.3.start", "");
		Config.setParameter("pentlandaccountsummaryaddon.daterange.3.end", "");
	}

	@Test
	public void shouldGetEmptyDaterangeList()
	{
		final PastDueBalanceDateRangeService srv = new DefaultPastDueBalanceDateRangeService();
		final List result = srv.getNumberOfDayRange();

		TestCase.assertEquals(0, result.size());
		TestCase.assertTrue(result.isEmpty());
	}

	@Test
	public void shouldGetEmptyDatarangeInvalidParameters()
	{
		Config.setParameter("pentlandaccountsummaryaddon.daterange.1.start", "invalid");
		final PastDueBalanceDateRangeService srv = new DefaultPastDueBalanceDateRangeService();

		try
		{
			srv.getNumberOfDayRange();
			TestCase.fail();
		}
		catch (final NumberFormatException e)
		{
			// success
			TestCase.assertEquals("For input string: \"invalid\"", e.getMessage());
		}
	}

	@Test
	public void shouldGetOneDatarangeWithInfinite()
	{
		Config.setParameter("pentlandaccountsummaryaddon.daterange.1.start", "1");
		final PastDueBalanceDateRangeService srv = new DefaultPastDueBalanceDateRangeService();
		final List<NumberOfDayRange> result = srv.getNumberOfDayRange();

		TestCase.assertEquals(1, result.size());
		TestCase.assertEquals(1, result.get(0).getMinBoundary().intValue());
		TestCase.assertNull(result.get(0).getMaxBoundary());
	}

	@Test
	public void shouldGetOneDatarange()
	{
		Config.setParameter("pentlandaccountsummaryaddon.daterange.1.start", "1");
		Config.setParameter("pentlandaccountsummaryaddon.daterange.1.end", "30");
		final PastDueBalanceDateRangeService srv = new DefaultPastDueBalanceDateRangeService();
		final List<NumberOfDayRange> result = srv.getNumberOfDayRange();

		TestCase.assertEquals(1, result.size());

		TestCase.assertEquals(1, result.get(0).getMinBoundary().intValue());
		TestCase.assertEquals(30, result.get(0).getMaxBoundary().intValue());
	}

	@Test
	public void shouldGetTwoDatarange()
	{
		Config.setParameter("pentlandaccountsummaryaddon.daterange.1.start", "1");
		Config.setParameter("pentlandaccountsummaryaddon.daterange.1.end", "30");

		Config.setParameter("pentlandaccountsummaryaddon.daterange.2.start", "31");
		Config.setParameter("pentlandaccountsummaryaddon.daterange.2.end", "60");

		final PastDueBalanceDateRangeService srv = new DefaultPastDueBalanceDateRangeService();
		final List<NumberOfDayRange> result = srv.getNumberOfDayRange();

		TestCase.assertEquals(2, result.size());

		TestCase.assertEquals(1, result.get(0).getMinBoundary().intValue());
		TestCase.assertEquals(30, result.get(0).getMaxBoundary().intValue());

		TestCase.assertEquals(31, result.get(1).getMinBoundary().intValue());
		TestCase.assertEquals(60, result.get(1).getMaxBoundary().intValue());
	}
}
