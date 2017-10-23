package com.bridgex.core.customer.dao;

import java.util.Set;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commerceservices.customer.dao.CustomerAccountDao;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.store.BaseStoreModel;

/**
 * Created by dmitry.konovalov@masterdata.ru on 18.10.2017.
 */
public interface PentlandCustomerAccountDao extends CustomerAccountDao {

  SearchPageData<OrderModel> findOrdersByB2BUnitsAndStore(Set<B2BUnitModel> b2bUntis, BaseStoreModel store, OrderStatus[] status, PageableData pageableData);

}
