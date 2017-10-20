package com.bridgex.facades.search.converters.populator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.bridgex.core.search.solrfacetsearch.provider.impl.UserPriceGroupCurrencyQualifierProvider;

import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.converters.populator.SearchResultVariantOptionsProductPopulator;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.europe1.enums.UserPriceGroup;
import de.hybris.platform.servicelayer.session.SessionService;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/20/2017.
 */
public class PentlandSearchResultProductPopulator extends SearchResultVariantOptionsProductPopulator {

  private SessionService sessionService;

  @Override
  public void populate(final SearchResultValueData source, final ProductData target)
  {
    Assert.notNull(source, "Parameter source cannot be null.");
    Assert.notNull(target, "Parameter target cannot be null.");
    // Pull the values directly from the SearchResult object
    target.setCode(this.<String> getValue(source, "code"));
    target.setName(this.<String> getValue(source, "name"));
    target.setBaseProduct(this.<String> getValue(source, "baseProductCode"));

    populatePrices(source, target);

    // Populate product's classification features
    getProductFeatureListPopulator().populate(getFeaturesList(source), target);

    final List<ImageData> images = createImageData(source);
    if (CollectionUtils.isNotEmpty(images))
    {
      target.setImages(images);
    }

    populateUrl(source, target);

    if (CollectionUtils.isEmpty(source.getVariants()))
    {
      return;
    }

    final String variantType = getValue(source, ITEMTYPE_VARIANT_PROPERTY);
    final Set<String> variantTypeAttributes = getVariantsService().getVariantAttributes(variantType);
    final String rollupProperty = getRollupProperty(variantTypeAttributes);
    if (StringUtils.isBlank(rollupProperty))
    {
      return;
    }

    final List<SearchResultValueData> variants = source.getVariants().stream()
                                                       .filter(distinctByKey(p -> getValue(p, rollupProperty))).collect(Collectors.toList());

    target.setVariantOptions(getVariantOptions(variants, variantTypeAttributes, rollupProperty));
  }

  @Override
  protected void populatePrices(final SearchResultValueData source, final ProductData target)
  {
    //We have to check prices for all price groups in session
    List<UserPriceGroup> sessionUPGs = sessionService.getAttribute(Europe1Constants.PARAMS.UPG);
    CurrencyModel currentCurrency = getCommonI18NService().getCurrentCurrency();

    for(UserPriceGroup priceGroup: sessionUPGs) {
      UserPriceGroupCurrencyQualifierProvider.UserPriceGroupCurrencyQualifier qualifier =
        new UserPriceGroupCurrencyQualifierProvider.UserPriceGroupCurrencyQualifier(priceGroup, currentCurrency);
      String priceField = String.format("priceValue_%s_double", qualifier.toFieldQualifier().toLowerCase());
      Double priceValue = this.<Double>getValue(source, priceField);

      if (priceValue != null) {

        final PriceData priceData = getPriceDataFactory().create(PriceDataType.BUY, BigDecimal.valueOf(priceValue), currentCurrency);
        target.setPrice(priceData);
        break;
      }
    }
  }

  @Required
  public void setSessionService(SessionService sessionService) {
    this.sessionService = sessionService;
  }
}
