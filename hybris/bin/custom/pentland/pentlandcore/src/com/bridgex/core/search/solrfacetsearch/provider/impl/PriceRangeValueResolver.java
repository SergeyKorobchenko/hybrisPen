package com.bridgex.core.search.solrfacetsearch.provider.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.model.ApparelSizeVariantProductModel;
import com.bridgex.core.price.PentlandCommercePriceService;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.Qualifier;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import de.hybris.platform.solrfacetsearch.provider.impl.ValueProviderParameterUtils;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/23/2017.
 */
public class PriceRangeValueResolver extends AbstractValueResolver<ProductModel, Object, PriceRowModel> {

  public static final String OPTIONAL_PARAM = "optional";
  public static final boolean OPTIONAL_PARAM_DEFAULT_VALUE = true;

  private PentlandCommercePriceService commercePriceService;

  @Override
  protected void addFieldValues(InputDocument document,
                                IndexerBatchContext indexerBatchContext,
                                IndexedProperty indexedProperty,
                                ProductModel productModel,
                                ValueResolverContext<Object, PriceRowModel> resolverContext) throws FieldValueProviderException {

    boolean hasPrice = false;

    final PriceRowModel priceRow = resolverContext.getQualifierData();
    if (priceRow != null) {
      final Double priceValue = priceRow.getPrice();
      if (priceValue != null) {
        hasPrice = true;
          document.addField(indexedProperty, priceValue, resolverContext.getFieldQualifier());
      }
    }

    if (!hasPrice) {
      final boolean isOptional = ValueProviderParameterUtils.getBoolean(indexedProperty, OPTIONAL_PARAM,
                                                                        OPTIONAL_PARAM_DEFAULT_VALUE);
      if (!isOptional) {
        throw new FieldValueProviderException("No value resolved for indexed property " + indexedProperty.getName());
      }
    }

  }

  @Override
  protected PriceRowModel loadQualifierData(final IndexerBatchContext batchContext,
                                                     final Collection<IndexedProperty> indexedProperties, final ProductModel product, final Qualifier qualifier) {
    ProductModel baseProductModel = getBaseProductModel(product);
    return commercePriceService.findMaxPriceRowForProductAndCurrency(baseProductModel, qualifier.getValueForType(CurrencyModel.class));
  }


  protected ProductModel getBaseProductModel(final ProductModel model)
  {
    if (model instanceof ApparelSizeVariantProductModel) {
      //prices are stored at style level
      return ((ApparelSizeVariantProductModel) model).getBaseProduct();
    }else{
      //in case of non-variant or style-only products
      return model;
    }
  }


  @Required
  public void setCommercePriceService(PentlandCommercePriceService commercePriceService) {
    this.commercePriceService = commercePriceService;
  }
}
