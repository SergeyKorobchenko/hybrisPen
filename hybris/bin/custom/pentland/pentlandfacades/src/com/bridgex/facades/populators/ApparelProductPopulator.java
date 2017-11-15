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
package com.bridgex.facades.populators;

import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.enums.Gender;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.variants.model.VariantProductModel;

import com.bridgex.core.model.ApparelSizeVariantProductModel;
import com.bridgex.core.model.ApparelStyleVariantProductModel;
import com.bridgex.facades.product.data.GenderData;

import org.springframework.beans.factory.annotation.Required;


/**
 * Populates {@link ProductData} with genders
 */
public class ApparelProductPopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends AbstractProductPopulator<SOURCE, TARGET>
{
	private Converter<Gender, GenderData> genderConverter;

	protected Converter<Gender, GenderData> getGenderConverter()
	{
		return genderConverter;
	}

	@Required
	public void setGenderConverter(final Converter<Gender, GenderData> genderConverter)
	{
		this.genderConverter = genderConverter;
	}

	@Override
	public void populate(final ProductModel source, final ProductData target) throws ConversionException
	{
		final Gender gender = (Gender)getProductAttribute(source, ProductModel.GENDER);
		if (gender != null) {
			target.setGender(getGenderConverter().convert(gender));
		}
		populateAdditionalIdentifiers(source, target);
	}

	private void populateAdditionalIdentifiers(ProductModel source, ProductData target) {
		if (source instanceof ApparelSizeVariantProductModel) {
			ProductModel styleProduct = ((VariantProductModel) source).getBaseProduct();
			target.setMaterialKey(styleProduct.getCode());
			target.setStylecode(((VariantProductModel) styleProduct).getBaseProduct().getCode());
			target.setUpc(source.getUpc());
		} else if (source instanceof ApparelStyleVariantProductModel) {
			target.setMaterialKey(source.getCode());
		}
	}
}
