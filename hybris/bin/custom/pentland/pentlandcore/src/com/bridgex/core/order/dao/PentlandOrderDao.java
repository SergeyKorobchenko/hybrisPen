package com.bridgex.core.order.dao;

import java.util.List;

import de.hybris.platform.order.daos.OrderDao;

public interface PentlandOrderDao extends OrderDao
{
	List<Object> getSourceOrder(String sapOrderCode);
}
