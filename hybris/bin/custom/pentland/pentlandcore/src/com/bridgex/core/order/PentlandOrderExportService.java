package com.bridgex.core.order;

import de.hybris.platform.core.model.order.OrderModel;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/7/2017.
 */
public interface PentlandOrderExportService {

  boolean exportOrder(OrderModel orderModel);
}
