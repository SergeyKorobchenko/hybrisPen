package com.bridgex.core.search.solrfacetsearch.provider.impl;

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.model.ApparelSizeVariantProductModel;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.product.PriceService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.Qualifier;
import de.hybris.platform.solrfacetsearch.provider.RangeNameProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import de.hybris.platform.solrfacetsearch.provider.impl.ValueProviderParameterUtils;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/19/2017.
 */
public class WholesalePriceValueResolver extends AbstractValueResolver<ProductModel, Object, List<PriceInformation>> {

  public static final String OPTIONAL_PARAM = "optional";
  public static final boolean OPTIONAL_PARAM_DEFAULT_VALUE = true;

  private   PriceService      priceService;
  private RangeNameProvider rangeNameProvider;

  @Override
  protected void addFieldValues(InputDocument document,
                                IndexerBatchContext indexerBatchContext,
                                IndexedProperty indexedProperty,
                                ProductModel productModel,
                                ValueResolverContext<Object, List<PriceInformation>> resolverContext) throws FieldValueProviderException {
    boolean hasPrice = false;

    final List<PriceInformation> priceInformations = resolverContext.getQualifierData();
    if (priceInformations != null)
    {
      final Double priceValue = getPriceValue(indexedProperty, priceInformations);
      if (priceValue != null)
      {
        hasPrice = true;
        document.addField(indexedProperty, priceValue, resolverContext.getFieldQualifier());
      }
    }

    if (!hasPrice)
    {
      final boolean isOptional = ValueProviderParameterUtils.getBoolean(indexedProperty, OPTIONAL_PARAM,
                                                                        OPTIONAL_PARAM_DEFAULT_VALUE);
      if (!isOptional)
      {
        throw new FieldValueProviderException("No value resolved for indexed property " + indexedProperty.getName());
      }
    }
  }

  @Override
  protected List<PriceInformation> loadQualifierData(final IndexerBatchContext batchContext,
                                                     final Collection<IndexedProperty> indexedProperties, final ProductModel product, final Qualifier qualifier) {
    return loadPriceInformations(product);
  }

  private List<PriceInformation> loadPriceInformations(final ProductModel product) {
    ProductModel baseProductModel = getBaseProductModel(product);
    return priceService.getPriceInformationsForProduct(baseProductModel);
  }

  private Double getPriceValue(final IndexedProperty indexedProperty, final List<PriceInformation> priceInformations)
    throws FieldValueProviderException {
    Double value = null;

    if (priceInformations != null && !priceInformations.isEmpty())
    {
      value = priceInformations.get(0).getPriceValue().getValue();
    }

    return value;
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
  public void setPriceService(PriceService priceService) {
    this.priceService = priceService;
  }

  @Required
  public void setRangeNameProvider(RangeNameProvider rangeNameProvider) {
    this.rangeNameProvider = rangeNameProvider;
  }
}
