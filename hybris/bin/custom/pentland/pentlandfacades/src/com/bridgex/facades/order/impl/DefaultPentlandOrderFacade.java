package com.bridgex.facades.order.impl;

import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.customer.PentlandCustomerAccountService;
import com.bridgex.facades.integration.OrderDetailsFacade;
import com.bridgex.facades.order.PentlandOrderFacade;

import de.hybris.platform.commercefacades.order.data.OrderData;
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
public class DefaultPentlandOrderFacade extends DefaultOrderFacade implements PentlandOrderFacade {

  private PentlandCustomerAccountService pentlandCustomerAccountService;

  private OrderDetailsFacade orderDetailsFacade;

  @Override
  public SearchPageData<OrderHistoryData> getPagedB2BOrderHistoryForStatuses(final PageableData pageableData, final OrderStatus... statuses)
  {
    final CustomerModel currentCustomer = (CustomerModel) getUserService().getCurrentUser();
    final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();
    final SearchPageData<OrderModel> orderResults = getPentlandCustomerAccountService().getB2BOrderList(currentCustomer, currentBaseStore, statuses, pageableData);

    return convertPageData(orderResults, getOrderHistoryConverter());
  }

  @Override
  public OrderData getOrderDetailsForCode(String code) {
    return orderDetailsFacade.getOrderDetails(code);
  }

  protected PentlandCustomerAccountService getPentlandCustomerAccountService() {
    return pentlandCustomerAccountService;
  }

  @Required
  public void setPentlandCustomerAccountService(PentlandCustomerAccountService pentlandCustomerAccountService) {
    this.pentlandCustomerAccountService = pentlandCustomerAccountService;
  }

  public OrderDetailsFacade getOrderDetailsFacade() {
    return orderDetailsFacade;
  }

  @Required
  public void setOrderDetailsFacade(OrderDetailsFacade orderDetailsFacade) {
    this.orderDetailsFacade = orderDetailsFacade;
  }
}
