package com.bridgex.facades.order.converters.populator;

import de.hybris.platform.commercefacades.order.converters.populator.OrderHistoryPopulator;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.core.model.order.OrderModel;

/**
 * Created by dmitry.konovalov@masterdata.ru on 18.10.2017.
 */
public class PentlandOrderHistoryPopulator extends OrderHistoryPopulator {
  @Override
  public void populate(final OrderModel source, final OrderHistoryData target)
  {
    super.populate(source, target);
    target.setOrderType(source.getOrderType());
    target.setRdd(source.getRdd());
    target.setPurchaseOrderNumber(source.getPurchaseOrderNumber());
    target.setTotalQty(source.getTotalQty());
  }
}
