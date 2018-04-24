package com.bridgex.core.order.impl;

import java.util.List;

import com.bridgex.core.order.PentlandOrderService;
import com.bridgex.core.order.dao.PentlandOrderDao;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.impl.DefaultOrderService;

public class DefaultPentlandOrderService extends DefaultOrderService implements PentlandOrderService 
{
	private PentlandOrderDao pentlandOrderDao;

	@Override
	public OrderModel getSourceOrder(String sapOrderCode)
	{
		List<Object> sourceOrder = getPentlandOrderDao().getSourceOrder(sapOrderCode);
		OrderModel orderModel = (OrderModel) sourceOrder.get(0);
		return orderModel;
	}

	public PentlandOrderDao getPentlandOrderDao() {
		return pentlandOrderDao;
	}

	public void setPentlandOrderDao(PentlandOrderDao pentlandOrderDao) {
		this.pentlandOrderDao = pentlandOrderDao;
	}

	
}
