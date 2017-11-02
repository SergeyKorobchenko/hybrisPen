package com.bridgex.facades.integration;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;

import com.bridgex.core.integration.OrderDetailsService;
import com.bridgex.integration.domain.OrderDetailsDto;
import com.bridgex.integration.domain.OrderDetailsResponse;
import com.bridgex.integration.service.IntegrationService;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.servicelayer.dto.converter.Converter;

/**
 * @author Goncharenko Mikhail, created on 01.11.2017.
 */
public class OrderDetailsFacade {

  private final static String I_SERVICE_CONSUMER = "Hybris_B2B";
  private final static String I_VBTYP = "C";
  private final static String I_CUSTOMER_VIEW_FLAG = "";

  private OrderDetailsService                       orderDetailsService;
  private StoreSessionFacade                        storeSessionFacade;
  private Converter<OrderDetailsResponse,OrderData> orderDetailsConverter;

  @Required
  public void setOrderDetailsConverter(Converter<OrderDetailsResponse, OrderData> orderDetailsConverter) {
    this.orderDetailsConverter = orderDetailsConverter;
  }

  @Required
  public void setOrderDetailsService(OrderDetailsService orderDetailsService) {
    this.orderDetailsService = orderDetailsService;
  }

  @Required
  public void setStoreSessionFacade(StoreSessionFacade storeSessionFacade) {
    this.storeSessionFacade = storeSessionFacade;
  }

  public OrderData requestOrderDetails(String orderCode) {
    OrderDetailsResponse response = orderDetailsService.requestOrderDetails(createRequestDto(orderCode));
    return orderDetailsConverter.convert(response);
  }

  private OrderDetailsDto createRequestDto(String code) {
    OrderDetailsDto request = new OrderDetailsDto();
    request.setOrderCode(code);
    request.setLanguage(storeSessionFacade.getCurrentLanguage().getIsocode().toUpperCase());
    request.setOrderType(I_VBTYP);
    request.setServiceConsumer(I_SERVICE_CONSUMER);
    request.setCustomerViewFlag(I_CUSTOMER_VIEW_FLAG);
    return request;
  }

}
