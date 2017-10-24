package com.bridgex.facades.order.impl;

import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.customer.PentlandCustomerAccountService;
import com.bridgex.facades.order.PentlandOrderFacade;

import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commercefacades.order.impl.DefaultOrderFacade;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.store.BaseStoreModel;

/**
 * Created by dmitry.konovalov@masterdata.ru on 18.10.2017.
 */
public class DefaultPentalndOrderFacade extends DefaultOrderFacade implements PentlandOrderFacade {

  PentlandCustomerAccountService pentlandCustomerAccountService;

  @Override
  public SearchPageData<OrderHistoryData> getPagedB2BOrderHistoryForStatuses(final PageableData pageableData, final OrderStatus... statuses)
  {
    final CustomerModel currentCustomer = (CustomerModel) getUserService().getCurrentUser();
    final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();
    final SearchPageData<OrderModel> orderResults = getPentlandCustomerAccountService().getB2BOrderList(currentCustomer, currentBaseStore, statuses, pageableData);

    return convertPageData(orderResults, getOrderHistoryConverter());
  }

  protected PentlandCustomerAccountService getPentlandCustomerAccountService() {
    return pentlandCustomerAccountService;
  }

  @Required
  public void setPentlandCustomerAccountService(PentlandCustomerAccountService pentlandCustomerAccountService) {
    this.pentlandCustomerAccountService = pentlandCustomerAccountService;
  }
}
