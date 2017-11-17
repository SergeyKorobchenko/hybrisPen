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
package com.bridgex.pentlandaccountsummaryaddon.document.criteria.validator.impl;

import java.math.BigDecimal;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.ui.Model;

import com.bridgex.pentlandaccountsummaryaddon.utils.PentlandaccountsummaryaddonUtils;
import com.bridgex.pentlandaccountsummaryaddon.document.criteria.validator.CriteriaValidator;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;

/**
 *
 */
public class AmountCriteriaValidator implements CriteriaValidator
{
	private static final Logger LOG = Logger.getLogger(AmountCriteriaValidator.class);

	@Override
	public boolean isValid(final String startRange, final String endRange, final Model model)
	{

		Optional<BigDecimal> parsedStartRange = Optional.empty();
		Optional<BigDecimal> parsedEndRange = Optional.empty();

		if (LOG.isDebugEnabled())
		{
			LOG.debug("validating amount ranges");
		}

		if (StringUtils.isNotBlank(startRange))
		{
			parsedStartRange = PentlandaccountsummaryaddonUtils.parseBigDecimal(startRange);
			if (!parsedStartRange.isPresent())
			{
				GlobalMessages.addErrorMessage(model, "text.company.accountsummary.criteria.amount.format.from.invalid");
				return false;
			}
		}

		if (StringUtils.isNotBlank(endRange))
		{
			parsedEndRange = PentlandaccountsummaryaddonUtils.parseBigDecimal(endRange);
			if (!parsedEndRange.isPresent())
			{
				GlobalMessages.addErrorMessage(model, "text.company.accountsummary.criteria.amount.format.to.invalid");
				return false;
			}
		}

		if (parsedStartRange.isPresent() && parsedEndRange.isPresent()
				&& parsedStartRange.get().compareTo(parsedEndRange.get()) == 1)
		{
			GlobalMessages.addErrorMessage(model, "text.company.accountsummary.criteria.amount.invalid");
			return false;
		}

		return true;
	}
}