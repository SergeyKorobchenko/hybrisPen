package com.bridgex.facades.order;

import java.util.List;

import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;

/**
 * Created by dmitry.konovalov@masterdata.ru on 18.10.2017.
 */
public interface PentlandOrderFacade extends OrderFacade {

  SearchPageData<OrderHistoryData> getPagedB2BOrderHistoryForStatuses(final PageableData pageableData, final OrderStatus... statuses);

  OrderData requestOrderDetails(String orderCode);

  List<OrderData> getSapOrdersForOrderCode(String orderCode);
  
}
