package com.bridgex.facades.order.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.customer.PentlandCustomerAccountService;
import com.bridgex.core.integration.PentlandIntegrationService;
import com.bridgex.facades.order.PentlandOrderFacade;
import com.bridgex.integration.domain.OrderDetailsDto;
import com.bridgex.integration.domain.OrderDetailsResponse;
import com.bridgex.integration.domain.OrderEntryDto;
import com.bridgex.integration.domain.SizeVariantDto;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commercefacades.order.impl.DefaultOrderFacade;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.enums.ExportStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.store.BaseStoreModel;

/**
 * Created by dmitry.konovalov@masterdata.ru on 18.10.2017.
 */
public class DefaultPentlandOrderFacade extends DefaultOrderFacade implements PentlandOrderFacade {

  private static final Logger LOG = Logger.getLogger(DefaultPentlandOrderFacade.class);

  private PentlandCustomerAccountService pentlandCustomerAccountService;

  private PentlandIntegrationService<OrderDetailsDto,OrderDetailsResponse> orderDetailsService;

  private Converter<OrderDetailsResponse,OrderData> orderDetailsConverter;
  
  private Converter<OrderModel, OrderData> orderConverter;

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
	  
	  final BaseStoreModel baseStoreModel = getBaseStoreService().getCurrentBaseStore();
	  OrderModel orderForCode = getCustomerAccountService().getOrderForCode(code, baseStoreModel); 
	  OrderData sourceOrderData = getOrderConverter().convert(orderForCode.getSourceOrder());
	  
    OrderDetailsResponse response = orderDetailsService.requestData(createRequestDto(code));
    List<OrderEntryData> entries = sourceOrderData.getEntries();
    
    List<OrderEntryData> orderData = response.getOrderEntries().stream()
    .map(entry->entries.stream().filter(e->entry.getProduct().equals(e.getProduct().getCode())).findAny())
    .filter(Optional::isPresent)
    .map(Optional::get).collect(Collectors.<OrderEntryData>toList());
    
    OrderData order= orderDetailsConverter.convert(response);
	order.setEntries(orderData);
    return order;
  }

  @Override
  public List<OrderData> getSapOrdersForOrderCode(String orderCode) {
    final BaseStoreModel baseStoreModel = getBaseStoreService().getCurrentBaseStore();

    try {
      OrderModel orderModel = getCustomerAccountService().getOrderForCode((CustomerModel) getUserService().getCurrentUser(), orderCode,
                                                             baseStoreModel);
      List<OrderModel> byBrandOrderList = orderModel.getByBrandOrderList();
      if(ExportStatus.EXPORTED.equals(orderModel.getExportStatus()) && CollectionUtils.isNotEmpty(byBrandOrderList)){
        return Converters.convertAll(byBrandOrderList, getOrderConverter());
      }
    }catch (final ModelNotFoundException e) {
      LOG.debug("No order found for code " + orderCode);
    }

    return null;
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

	public Converter<OrderModel, OrderData> getOrderConverter() {
		return orderConverter;
	}

	public void setOrderConverter(Converter<OrderModel, OrderData> orderConverter) {
		this.orderConverter = orderConverter;
	}
  
  
   
  

}
