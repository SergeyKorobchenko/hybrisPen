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
package com.bridgex.pentlandaccountsummaryaddon.populators;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import com.bridgex.integration.domain.AccountSummaryDetailsDto;
import com.bridgex.integration.domain.AccountSummaryResponse;
import com.bridgex.pentlandaccountsummaryaddon.data.AccountSummaryInfoData;
import com.bridgex.pentlandaccountsummaryaddon.document.data.B2BAmountBalanceData;
import com.bridgex.pentlandaccountsummaryaddon.formatters.AmountFormatter;

import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

public class AccountSummaryInfoPopulator implements Populator<AccountSummaryResponse, AccountSummaryInfoData>
{

	public static final String DAYS = "1-30 Days";
	public static final String DAYS1 = "31-60 Days";
	public static final String DAYS2 = "61-90 Days";
	public static final String DAYS3 = "91+ Days";
	private   CommonI18NService commonI18NService;
	private   AmountFormatter   amountFormatter;

	private static final String DASH_SEPERATOR = " — ";

	@Override
	public void populate(final AccountSummaryResponse source, final AccountSummaryInfoData target) throws ConversionException
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setId(source.getSapCustomerId());
		target.setName(source.getSapCustomerName());

		CurrencyModel currency = getCurrency(source);

		populateBalanceData(source, target, currency);
		populateCreditData(source, target, currency);
		populateAddressData(source, target);
	}

	private void populateCreditData(AccountSummaryResponse source, AccountSummaryInfoData target, CurrencyModel currency) {
		Set<String> creditReps = new HashSet<>();
		Map<String, String> creditLimits = source.getDetails().stream().
			collect(Collectors.groupingBy(AccountSummaryDetailsDto::getBrandName,
			                              Collectors.collectingAndThen(Collectors.summingDouble((k -> parseDoubleValue(k.getCreditLimit()))),
			                                                           v-> amountFormatter.formatAmount(v,currency))));

		for (AccountSummaryDetailsDto detail : source.getDetails()) {
			if (StringUtils.isNotBlank(detail.getCreditRep())) {
				creditReps.add(detail.getBrandName() + DASH_SEPERATOR + detail.getCreditRep());
			}
		}
		target.setCreditReps(new ArrayList<>(creditReps));
		target.setFormattedCreditLimits(creditLimits);
	}

	private void populateBalanceData(AccountSummaryResponse source, AccountSummaryInfoData target, CurrencyModel currency) {
		B2BAmountBalanceData balanceData = new B2BAmountBalanceData();

		double cur = 0, past = 0, open = 0, d1to30 = 0, d30to60 = 0, d60to90 = 0, d90over = 0;
		for (AccountSummaryDetailsDto detail : source.getDetails()) {
			cur += parseDoubleValue(detail.getCurrentBalance());
			past += parseDoubleValue(detail.getPastDueBalance());
			open += parseDoubleValue(detail.getOpenBalance());
			d1to30 += parseDoubleValue(detail.getDays1to30());
			d30to60 += parseDoubleValue(detail.getDays31to60());
			d60to90 += parseDoubleValue(detail.getDays61to90());
			d90over += parseDoubleValue(detail.getDaysOver90());
		}

		balanceData.setCurrentBalance(getAmountFormatter().formatAmount(cur, currency));
		balanceData.setOpenBalance(getAmountFormatter().formatAmount(open, currency));
		balanceData.setPastDueBalance(getAmountFormatter().formatAmount(past, currency));

		Map<String, String> dueBalance = new HashMap<>();
		dueBalance.put(DAYS, getAmountFormatter().formatAmount(d1to30, currency));
		dueBalance.put(DAYS1, getAmountFormatter().formatAmount(d30to60, currency));
		dueBalance.put(DAYS2, getAmountFormatter().formatAmount(d60to90, currency));
		dueBalance.put(DAYS3, getAmountFormatter().formatAmount(d90over, currency));

		balanceData.setDueBalance(dueBalance);
		target.setAmountBalanceData(balanceData);
	}

	private double parseDoubleValue(String currentBalance) {
		try {
			return Double.parseDouble(currentBalance);
		} catch (NumberFormatException e) {
			return 0D;
		}
	}

	private CurrencyModel getCurrency(AccountSummaryResponse source) {
		try {
			return source.getDetails().stream()
			      .map(AccountSummaryDetailsDto::getCurrency)
			      .filter(StringUtils::isNotBlank)
			      .findAny()
			      .map(s -> commonI18NService.getCurrency(s)).orElseGet(() -> commonI18NService.getCurrentCurrency());
		} catch (UnknownIdentifierException e) {
			return commonI18NService.getCurrentCurrency();
		}
	}

	private void populateAddressData(AccountSummaryResponse source, AccountSummaryInfoData target) {
		AddressData address = new AddressData();

		CountryModel country = commonI18NService.getCountry(source.getCountry());
		CountryData countryData = new CountryData();
		countryData.setName(country.getName());
		countryData.setIsocode(country.getIsocode());

		address.setCountry(countryData);
		address.setState(source.getRegion());
		address.setPostalCode(source.getPostalCode());
		address.setTown(source.getCity());
		address.setLine1(source.getStreetLine1());
		address.setLine2(source.getStreetLine2());
		address.setLine3(source.getStreetLine3());

		target.setAddress(address);
	}

	public AmountFormatter getAmountFormatter()
	{
		return amountFormatter;
	}

	public void setAmountFormatter(final AmountFormatter amountFormatter)
	{
		this.amountFormatter = amountFormatter;
	}

	public CommonI18NService getCommonI18NService() {
		return commonI18NService;
	}

	public void setCommonI18NService(CommonI18NService commonI18NService) {
		this.commonI18NService = commonI18NService;
	}
}
