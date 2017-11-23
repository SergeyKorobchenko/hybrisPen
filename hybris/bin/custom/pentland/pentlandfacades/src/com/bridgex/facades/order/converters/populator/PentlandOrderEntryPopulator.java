package com.bridgex.facades.order.converters.populator;

import de.hybris.platform.commercefacades.order.converters.populator.OrderEntryPopulator;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;

/**
 * @author Created by Dmitry Konovalov on 23.11.2017.
 */
public class PentlandOrderEntryPopulator extends OrderEntryPopulator {

  protected void addTotals(final AbstractOrderEntryModel source, final OrderEntryData target) {
    super.addTotals(source, target);
    if (source.getErpPrice() != null) {
      target.setErpPrice(createPrice(source, source.getErpPrice()));
    }
  }

}
