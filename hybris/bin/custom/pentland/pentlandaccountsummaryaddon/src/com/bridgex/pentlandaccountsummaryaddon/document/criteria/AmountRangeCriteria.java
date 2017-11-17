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
package com.bridgex.pentlandaccountsummaryaddon.document.criteria;

import java.math.BigDecimal;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;

import com.bridgex.pentlandaccountsummaryaddon.utils.PentlandaccountsummaryaddonUtils;

/**
 *
 */
public class AmountRangeCriteria extends RangeCriteria
{

	protected Optional<BigDecimal> startRange;
	protected Optional<BigDecimal> endRange;

	public AmountRangeCriteria(final String filterByKey)
	{
		this(filterByKey, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY);
	}

	public AmountRangeCriteria(final String filterByKey, final String startRange, final String endRange,
			final String documentStatus)
	{
		super(filterByKey, documentStatus);
		this.startRange = PentlandaccountsummaryaddonUtils.parseBigDecimalToOptional(startRange);
		this.endRange = PentlandaccountsummaryaddonUtils.parseBigDecimalToOptional(endRange);
	}

	/**
	 * @return the startRange
	 */
	@Override
	public Optional<BigDecimal> getStartRange()
	{
		return startRange;
	}

	/**
	 * @param startRange
	 *           the startRange to set
	 */
	@Override
	protected void setStartRange(final String startRange)
	{
		this.startRange = PentlandaccountsummaryaddonUtils.parseBigDecimalToOptional(startRange);
	}

	/**
	 * @return the endRange
	 */
	@Override
	public Optional<BigDecimal> getEndRange()
	{
		return endRange;
	}

	/**
	 * @param endRange
	 *           the endRange to set
	 */
	@Override
	protected void setEndRange(final String endRange)
	{
		this.endRange = PentlandaccountsummaryaddonUtils.parseBigDecimalToOptional(endRange);
	}
}
