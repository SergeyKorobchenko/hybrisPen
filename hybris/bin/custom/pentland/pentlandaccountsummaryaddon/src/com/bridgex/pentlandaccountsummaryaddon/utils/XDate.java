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
package com.bridgex.pentlandaccountsummaryaddon.utils;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

/**
 * Used to provide Date utilities
 */
public final class XDate
{
	private static final Logger LOG = Logger.getLogger(com.bridgex.pentlandaccountsummaryaddon.utils.XDate.class);

	private XDate()
	{
		// No public constructor for utility class
	}

	public static Date setToEndOfDay(final Date date)
	{
		Date newDate = new Date(date.getTime());
		newDate = DateUtils.setHours(newDate, 23);
		newDate = DateUtils.setMinutes(newDate, 59);
		newDate = DateUtils.setSeconds(newDate, 59);
		newDate = DateUtils.setMilliseconds(newDate, 999);
		return newDate;
	}
}
