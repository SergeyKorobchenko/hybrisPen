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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.bridgex.pentlandaccountsummaryaddon.document.NumberOfDayRange;
import com.bridgex.pentlandaccountsummaryaddon.document.service.PastDueBalanceDateRangeService;

import de.hybris.platform.util.Config;

/**
 * Provides services for past due balance date range.
 *
 */
public class DefaultPastDueBalanceDateRangeService implements PastDueBalanceDateRangeService
{

	private static final String END_SUFFIX = ".end";
	private static final String START_SUFFIX = ".start";
	private static final String ACCOUNTSUMMARY_DATERANGE = "pentlandaccountsummaryaddon.daterange.";

	@Override
	public List<NumberOfDayRange> getNumberOfDayRange()
	{
		final List<NumberOfDayRange> result = new ArrayList<NumberOfDayRange>();
		boolean dateRangeExist = true;
		int index = 1;
		while (dateRangeExist)
		{
			final Integer dateRangeStartValue = getRangeValue(index, true);
			final Integer dateRangeEndValue = getRangeValue(index, false);

			if (dateRangeStartValue == null)
			{
				dateRangeExist = false;
			}
			else
			{
				result.add(new NumberOfDayRange(dateRangeStartValue, dateRangeEndValue));
			}
			index++;
		}

		return result;
	}

	protected Integer getRangeValue(final int index, final boolean start)
	{
		final String dateRangeValue = Config.getParameter(ACCOUNTSUMMARY_DATERANGE + index + (start ? START_SUFFIX : END_SUFFIX));
		if (StringUtils.isBlank(dateRangeValue))
		{
			return null;
		}
		return Integer.valueOf(dateRangeValue);
	}
}
