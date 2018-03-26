package com.bridgex.core.customer;

import java.util.List;

import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.store.BaseStoreModel;

/**
 * Created by dmitry.konovalov@masterdata.ru on 18.10.2017.
 */
public interface PentlandCustomerAccountService extends CustomerAccountService {

  SearchPageData<OrderModel> getB2BOrderList(final CustomerModel customerModel, final BaseStoreModel store, final OrderStatus[] status, final PageableData pageableData);
  
  List<AddressModel> getDeliveryAddressesForCustomer(UserModel currentCustomer);
}
