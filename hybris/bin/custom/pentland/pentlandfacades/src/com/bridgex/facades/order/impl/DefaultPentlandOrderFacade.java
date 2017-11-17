package com.bridgex.facades.order.impl;

import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.customer.PentlandCustomerAccountService;
import com.bridgex.core.integration.PentlandIntegrationService;
import com.bridgex.facades.order.PentlandOrderFacade;
import com.bridgex.integration.domain.OrderDetailsDto;
import com.bridgex.integration.domain.OrderDetailsResponse;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commercefacades.order.impl.DefaultOrderFacade;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.store.BaseStoreModel;

/**
 * Created by dmitry.konovalov@masterdata.ru on 18.10.2017.
 */
public class DefaultPentlandOrderFacade extends DefaultOrderFacade implements PentlandOrderFacade {

  private PentlandCustomerAccountService pentlandCustomerAccountService;

  private PentlandIntegrationService<OrderDetailsDto,OrderDetailsResponse> orderDetailsService;

  private Converter<OrderDetailsResponse,OrderData> orderDetailsConverter;

  @Override
  public SearchPageData<OrderHistoryData> getPagedB2BOrderHistoryForStatuses(final PageableData pageableData, final OrderStatus... statuses)
  {
    final CustomerModel currentCustomer = (CustomerModel) getUserService().getCurrentUser();
    final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();
    final SearchPageData<OrderModel> orderResults = getPentlandCustomerAccountService().getB2BOrderList(currentCustomer, currentBaseStore, statuses, pageableData);

    return convertPageData(orderResults, getOrderHistoryConverter());
  }

  @Override
  public OrderData requestOrderDetails(String code) {
    OrderDetailsResponse response = orderDetailsService.requestData(createRequestDto(code));
    return orderDetailsConverter.convert(response);
  }

  private OrderDetailsDto createRequestDto(String code) {
    OrderDetailsDto request = new OrderDetailsDto();
    request.setOrderCode(code);
    return request;
  }

  protected PentlandCustomerAccountService getPentlandCustomerAccountService() {
    return pentlandCustomerAccountService;
  }

  @Required
  public void setPentlandCustomerAccountService(PentlandCustomerAccountService pentlandCustomerAccountService) {
    this.pentlandCustomerAccountService = pentlandCustomerAccountService;
  }

  public void setOrderDetailsService(PentlandIntegrationService<OrderDetailsDto, OrderDetailsResponse> orderDetailsService) {
    this.orderDetailsService = orderDetailsService;
  }

  @Required
  public void setOrderDetailsConverter(Converter<OrderDetailsResponse, OrderData> orderDetailsConverter) {
    this.orderDetailsConverter = orderDetailsConverter;
  }
}
