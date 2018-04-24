package com.bridgex.core.order;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.OrderService;

public interface PentlandOrderService extends OrderService
{
	OrderModel getSourceOrder(String sapOrderCode);

}
