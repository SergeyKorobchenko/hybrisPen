package com.bridgex.facades.order.converters.populator;

import org.apache.log4j.Logger;

import de.hybris.platform.commercefacades.order.converters.populator.OrderEntryPopulator;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;

/**
 * @author Created by Dmitry Konovalov on 23.11.2017.
 */
public class PentlandOrderEntryPopulator extends OrderEntryPopulator {

  private static final Logger LOG = Logger.getLogger(PentlandOrderEntryPopulator.class);

  protected void addTotals(final AbstractOrderEntryModel source, final OrderEntryData target) {
    super.addTotals(source, target);
    if (source.getErpPrice() != null) {
      target.setErpPrice(createPrice(source, source.getErpPrice()));
    }
  }

  protected void addProduct(final AbstractOrderEntryModel orderEntry, final OrderEntryData entry) {
    if(orderEntry.getProduct() != null) {
      entry.setProduct(getProductConverter().convert(orderEntry.getProduct()));
    }else{
      LOG.error("Corrupted data in abstract order #" + orderEntry.getOrder().getCode() + ": missing product.");
    }
  }

}
