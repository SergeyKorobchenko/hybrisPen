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
package com.bridgex.pentlandaccountsummaryaddon.document.populators;

import java.math.BigDecimal;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.bridgex.core.category.PentlandCategoryService;
import com.bridgex.pentlandaccountsummaryaddon.constants.PentlandaccountsummaryaddonConstants;
import com.bridgex.pentlandaccountsummaryaddon.document.data.B2BDocumentData;
import com.bridgex.pentlandaccountsummaryaddon.document.data.B2BDocumentTypeData;
import com.bridgex.pentlandaccountsummaryaddon.document.data.MediaData;
import com.bridgex.pentlandaccountsummaryaddon.enums.DocumentPayableOrUsable;
import com.bridgex.pentlandaccountsummaryaddon.formatters.AmountFormatter;
import com.bridgex.pentlandaccountsummaryaddon.model.B2BDocumentModel;
import com.bridgex.pentlandaccountsummaryaddon.model.B2BDocumentTypeModel;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.storesession.data.CurrencyData;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;

public class B2BDocumentPopulator implements Populator<B2BDocumentModel, B2BDocumentData>
{

	private static final Logger LOG = Logger.getLogger(B2BDocumentPopulator.class);

	private Converter<CurrencyModel, CurrencyData> currencyConverter;
	private Converter<MediaModel, MediaData>       mediaConverter;
	private AmountFormatter                        amountFormatter;
	private CommerceCommonI18NService              commerceCommonI18NService;
	private CommonI18NService                      commonI18NService;
	private I18NService                            i18NService;
	private PentlandCategoryService                categoryService;

	@Override
	public void populate( final B2BDocumentModel source, final B2BDocumentData target ) throws ConversionException
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		final Locale locale = getLocale();
		final B2BDocumentTypeModel documentTypeModel = source.getDocumentType();

		target.setDocumentNumber(source.getDocumentNumber());
		target.setAmount(source.getAmount());
		target.setFormattedAmount(amountFormatter.formatAmount(source.getAmount(), source.getCurrency(), locale));
		target.setOpenAmount(source.getOpenAmount());
		target.setFormattedOpenAmount(amountFormatter.formatAmount(source.getOpenAmount(), source.getCurrency(), locale));
		target.setDate(source.getDate());
		target.setDueDate(source.getDueDate());
		target.setDocumentType(getB2BDocumentType(documentTypeModel));
		target.setSelectable(isSelectable(documentTypeModel, source.getOpenAmount()));
		target.setCurrency(currencyConverter.convert(source.getCurrency()));
		target.setStatus(source.getStatus().getCode());
		target.setDocumentMedia(getDocumentMedia(source));

		B2BUnitModel unit = source.getUnit();
		if(unit != null && StringUtils.isNotEmpty(unit.getSapBrand())){
			String sapBrand = unit.getSapBrand();
			try {
				CategoryModel brandCategory = categoryService.getCategoryForCode(sapBrand);
				target.setBrandName(brandCategory.getName());
			}catch(ModelNotFoundException | AmbiguousIdentifierException e){
				LOG.debug("Error while retrieving brand category for unit: " + e);
			}
		}
	}
	
	protected Locale getLocale()
	{
		final LanguageModel currentLanguage = getCommonI18NService().getCurrentLanguage();
		Locale locale = getCommerceCommonI18NService().getLocaleForLanguage(currentLanguage);
		if (locale == null)
		{
			locale = getI18NService().getCurrentLocale();
		}
		return locale;
	}
	
	protected B2BDocumentTypeData getB2BDocumentType(final B2BDocumentTypeModel documentTypeModel)
	{
		final B2BDocumentTypeData documentType = new B2BDocumentTypeData();
		
		documentType.setCode(documentTypeModel.getCode());
		documentType.setName(documentTypeModel.getName());
		documentType.setDisplayInAllList(documentTypeModel.getDisplayInAllList());
		documentType.setIncludeInOpenBalance(documentTypeModel.getIncludeInOpenBalance());
		if (documentTypeModel.getPayableOrUsable() != null) {
			documentType.setPayableOrUsable(documentTypeModel.getPayableOrUsable().getCode());
		}
		
		return documentType;
	}
	
	protected boolean isSelectable(final B2BDocumentTypeModel documentTypeModel, BigDecimal openAmount)
	{
		boolean isSelectable = false;
		if (documentTypeModel.getPayableOrUsable() != null && BigDecimal.ZERO.compareTo(openAmount) < 0)
		{
			// select both types
			if( Registry.getCurrentTenant().getConfig().getBoolean(PentlandaccountsummaryaddonConstants.PAYORUSEINTERFACENAME, false) )
			{
				isSelectable = true;
			}
			else
			{
				isSelectable = documentTypeModel.getPayableOrUsable() == DocumentPayableOrUsable.PAY;
			}
		}
		return isSelectable;
	}
	
	protected MediaData getDocumentMedia(final B2BDocumentModel source)
	{
		MediaData documentMedia = null;
		if (source.getDocumentMedia() != null)
		{
			documentMedia = mediaConverter.convert(source.getDocumentMedia());
		}
		return documentMedia;
	}

	@Required
	public void setCurrencyConverter( final Converter<CurrencyModel, CurrencyData> currencyConverter )
	{
		this.currencyConverter = currencyConverter;
	}

	@Required
	public void setMediaConverter( final Converter<MediaModel, MediaData> mediaConverter )
	{
		this.mediaConverter = mediaConverter;
	}

	@Required
	public void setAmountFormatter( final AmountFormatter amountFormatter)
	{
		this.amountFormatter = amountFormatter;
	}

	protected CommerceCommonI18NService getCommerceCommonI18NService()
	{
		return commerceCommonI18NService;
	}

	@Required
	public void setCommerceCommonI18NService( final CommerceCommonI18NService commerceCommonI18NService )
	{
		this.commerceCommonI18NService = commerceCommonI18NService;
	}

	protected I18NService getI18NService()
	{
		return i18NService;
	}

	@Required
	public void setI18NService( final I18NService i18NService )
	{
		this.i18NService = i18NService;
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@Required
	public void setCommonI18NService( final CommonI18NService commonI18NService )
	{
		this.commonI18NService = commonI18NService;
	}

	@Required
	public void setCategoryService(PentlandCategoryService categoryService) {
		this.categoryService = categoryService;
	}
}
