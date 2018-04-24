package com.bridgex.core.order.dao.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bridgex.core.order.dao.PentlandOrderDao;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.daos.impl.DefaultOrderDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

public class DefaultPentlandOrderDao extends DefaultOrderDao implements PentlandOrderDao 
{
	private static final String FIND_ORDER_BY_SAP_ORDER_CODE_QUERY = "SELECT {" + OrderModel.SOURCEORDER + "} FROM {"
			+ OrderModel._TYPECODE +" } WHERE { "+ OrderModel.CODE+"}=?sapOrderCode";
	@Override
	public List<Object> getSourceOrder(String sapOrderCode) 
	{
		final Map<String, Object> params = new HashMap<>();
		params.put(OrderModel.CODE, sapOrderCode);
		FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_ORDER_BY_SAP_ORDER_CODE_QUERY.toString());
		query.addQueryParameter("sapOrderCode", sapOrderCode);
		return getFlexibleSearchService().search(query).getResult();
	}

}
