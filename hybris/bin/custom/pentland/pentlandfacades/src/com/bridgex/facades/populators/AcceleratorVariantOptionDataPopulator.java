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

import de.hybris.platform.commercefacades.product.ImageFormatMapping;
import de.hybris.platform.commercefacades.product.converters.populator.VariantOptionDataPopulator;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.VariantOptionData;
import de.hybris.platform.commercefacades.product.data.VariantOptionQualifierData;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.media.MediaContainerService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.variants.model.VariantAttributeDescriptorModel;
import de.hybris.platform.variants.model.VariantProductModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;


/**
 * Accelerator specific variant option data converter implementation.
 */
public class AcceleratorVariantOptionDataPopulator extends VariantOptionDataPopulator
{
	private TypeService typeService;
	private MediaService mediaService;
	private MediaContainerService mediaContainerService;
	private ImageFormatMapping imageFormatMapping;
	private Map<String, String> variantAttributeMapping;
	private Map<String, String> sizeVariantAttributeMapping;

	@Override
	public void populate(final VariantProductModel source, final VariantOptionData target)
	{
		//super.populate(source, target);
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (source.getBaseProduct() != null)
		{
			final List<VariantAttributeDescriptorModel> descriptorModels = getVariantsService()
					.getVariantAttributesForVariantType(source.getBaseProduct().getVariantType());

			final Collection<VariantOptionQualifierData> variantOptionQualifiers = new ArrayList<VariantOptionQualifierData>();
			for (final VariantAttributeDescriptorModel descriptorModel : descriptorModels)
			{
				// Create the variant qualifier
				final VariantOptionQualifierData variantOptionQualifier = new VariantOptionQualifierData();
				final String qualifier = descriptorModel.getQualifier();
				variantOptionQualifier.setQualifier(qualifier);
				variantOptionQualifier.setName(descriptorModel.getName());
				// Lookup the value
				
				final Object variantAttributeValue = lookupVariantAttributeName(source, qualifier);
				variantOptionQualifier.setValue(variantAttributeValue == null ? "" : variantAttributeValue.toString());
				

				// Add to list of variants
				variantOptionQualifiers.add(variantOptionQualifier);
			}
			target.setVariantOptionQualifiers(variantOptionQualifiers);
			target.setCode(source.getCode());
			if(CollectionUtils.isNotEmpty(source.getSupercategories()))
			{
			target.setUrl(getProductModelUrlResolver().resolve(source));
			}
			target.setStock(getStockConverter().convert(source));

			final PriceDataType priceType;
			final PriceInformation info;
			if (CollectionUtils.isEmpty(source.getVariants()))
			{
				priceType = PriceDataType.BUY;
				info = getCommercePriceService().getWebPriceForProduct(source);
			}
			else
			{
				priceType = PriceDataType.FROM;
				info = getCommercePriceService().getFromPriceForProduct(source);
			}

			if (info != null)
			{
				final PriceData priceData = getPriceDataFactory().create(priceType,
						BigDecimal.valueOf(info.getPriceValue().getValue()), info.getPriceValue().getCurrencyIso());
				target.setPriceData(priceData);
			}
		}
		
		if(CollectionUtils.isNotEmpty(source.getSupercategories()))
		  {
		final ComposedTypeModel productType = getTypeService().getComposedTypeForClass(source.getClass());
		final MediaContainerModel mediaContainer = getPrimaryImageMediaContainer(source);

		for (final VariantOptionQualifierData variantOptionQualifier : target.getVariantOptionQualifiers()) {
			if (mediaContainer != null) {
				final MediaModel media = getMediaWithImageFormat(mediaContainer, lookupImageFormat(productType, variantOptionQualifier.getQualifier()));
				if (media != null) {
					variantOptionQualifier.setImage(getImageConverter().convert(media));
				}
			}

			final String sizeNoProperty = lookupSizeNo(productType, variantOptionQualifier.getQualifier());
			if (sizeNoProperty != null) {
				final Object value = getVariantsService().getVariantAttributeValue(source, sizeNoProperty);
				variantOptionQualifier.setSizeNo(((Integer) value));
			}

		   }
		  }

	}

	protected String lookupSizeNo(final ComposedTypeModel productType, final String attributeQualifier) {
		if (productType == null)
		{
			return null;
		}
		final String key = productType.getCode() + "." + attributeQualifier;
		return getSizeVariantAttributeMapping().get(key);
	}

	protected MediaModel getMediaWithImageFormat(final MediaContainerModel mediaContainer, final String imageFormat)
	{
		if (mediaContainer != null && imageFormat != null)
		{
			final String mediaFormatQualifier = getImageFormatMapping().getMediaFormatQualifierForImageFormat(imageFormat);
			if (mediaFormatQualifier != null)
			{
				final MediaFormatModel mediaFormat = getMediaService().getFormat(mediaFormatQualifier);
				if (mediaFormat != null)
				{
					try {
						return getMediaContainerService().getMediaForFormat(mediaContainer, mediaFormat);
					}catch(ModelNotFoundException e){
						return null;
					}
				}
			}
		}
		return null;
	}

	protected String lookupImageFormat(final ComposedTypeModel productType, final String attributeQualifier)
	{
		if (productType == null)
		{
			return null;
		}

		// Lookup the image format mapping
		final String key = productType.getCode() + "." + attributeQualifier;
		final String imageFormat = getVariantAttributeMapping().get(key);

		// Try super type of not found for this type
		return imageFormat != null ? imageFormat : lookupImageFormat(productType.getSuperType(), attributeQualifier);
	}

	protected MediaContainerModel getPrimaryImageMediaContainer(final VariantProductModel variantProductModel)
	{
		final MediaModel picture = variantProductModel.getPicture();
		if (picture != null)
		{
			return picture.getMediaContainer();
		}
		return null;
	}


	protected TypeService getTypeService()
	{
		return typeService;
	}

	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}

	protected MediaService getMediaService()
	{
		return mediaService;
	}

	@Required
	public void setMediaService(final MediaService mediaService)
	{
		this.mediaService = mediaService;
	}

	protected MediaContainerService getMediaContainerService()
	{
		return mediaContainerService;
	}

	@Required
	public void setMediaContainerService(final MediaContainerService mediaContainerService)
	{
		this.mediaContainerService = mediaContainerService;
	}

	protected ImageFormatMapping getImageFormatMapping()
	{
		return imageFormatMapping;
	}

	@Required
	public void setImageFormatMapping(final ImageFormatMapping imageFormatMapping)
	{
		this.imageFormatMapping = imageFormatMapping;
	}

	protected Map<String, String> getVariantAttributeMapping()
	{
		return variantAttributeMapping;
	}

	@Required
	public void setVariantAttributeMapping(final Map<String, String> variantAttributeMapping)
	{
		this.variantAttributeMapping = variantAttributeMapping;
	}

	protected Map<String, String> getSizeVariantAttributeMapping() {
		return sizeVariantAttributeMapping;
	}

	@Required
	public void setSizeVariantAttributeMapping(Map<String, String> sizeVariantAttributeMapping) {
		this.sizeVariantAttributeMapping = sizeVariantAttributeMapping;
	}
}
