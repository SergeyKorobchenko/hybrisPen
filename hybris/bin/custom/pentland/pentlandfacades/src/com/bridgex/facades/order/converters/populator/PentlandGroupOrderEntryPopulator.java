package com.bridgex.facades.order.converters.populator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import de.hybris.platform.commercefacades.order.converters.populator.GroupOrderEntryPopulator;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.variants.model.VariantProductModel;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/3/2017.
 */
public class PentlandGroupOrderEntryPopulator extends GroupOrderEntryPopulator<AbstractOrderModel, AbstractOrderData> {

  private static final Logger LOG = Logger.getLogger(PentlandGroupOrderEntryPopulator.class);

  private static final String APPAREL_VARIANT_TYPE = "ApparelSizeVariantProduct";

  @Override
  protected boolean isGroupable(final ProductData product) {
    return product.getBaseProduct() != null && CollectionUtils.isNotEmpty(product.getBaseOptions())
           && APPAREL_VARIANT_TYPE.equalsIgnoreCase(product.getBaseOptions().get(0).getVariantType());
  }

  protected List<OrderEntryData> groupEntries(final List<OrderEntryData> entries, final AbstractOrderData order)
  {
    final Map<String, OrderEntryData> group = new HashMap<>();
    final List<OrderEntryData> allEntries = new ArrayList<>();

    for (final OrderEntryData entry : entries) {
      final ProductData product = entry.getProduct();
      if (isGroupable(product)) {
        final OrderEntryData rootEntry = group.computeIfAbsent(getBaseProduct(product), k -> createGroupedOrderEntry(entry));
        rootEntry.getEntries().add(entry);
        setEntryGroups(entry, rootEntry, order);
      }else {
        allEntries.add(entry);
      }
    }

    if (!group.isEmpty()) {
      consolidateGroupedOrderEntry(group);
      allEntries.addAll(group.values());
    }

    return allEntries;
  }

  private String getBaseProduct(ProductData product){
    String baseProduct = product.getBaseProduct();
    if(StringUtils.isNotEmpty(baseProduct)) {
      try {
        ProductModel styleProduct = getProductService().getProductForCode(baseProduct);
        if(styleProduct instanceof VariantProductModel){
          ProductModel baseProductModel = ((VariantProductModel) styleProduct).getBaseProduct();
          if(baseProductModel != null){
            return baseProductModel.getCode();
          }
        }
      }catch (UnknownIdentifierException | AmbiguousIdentifierException e){
        LOG.debug("Failed to identify base product #" + baseProduct);
      }

    }

      return baseProduct;
  }

}
