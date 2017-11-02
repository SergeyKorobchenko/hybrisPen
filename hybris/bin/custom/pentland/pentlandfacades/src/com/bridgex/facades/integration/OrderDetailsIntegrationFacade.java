package com.bridgex.facades.integration;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;

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
public class OrderDetailsIntegrationFacade {

  private final static String I_SERVICE_CONSUMER = "Hybris_B2B";
  private final static String I_VBTYP = "C";
  private final static String I_CUSTOMER_VIEW_FLAG = "";

  private IntegrationService<OrderDetailsDto, OrderDetailsResponse> integrationService;
  private StoreSessionFacade storeSessionFacade;
  private Converter<OrderDetailsResponse,OrderData> orderDetailsConverter;

  @Required
  public void setIntegrationService(IntegrationService integrationService) {
    this.integrationService = integrationService;
  }

  @Required
  public void setStoreSessionFacade(StoreSessionFacade storeSessionFacade) {
    this.storeSessionFacade = storeSessionFacade;
  }

  private OrderData requestOrderDetails(String orderCode) {
    ResponseEntity<OrderDetailsResponse> responseEntity = integrationService.sendRequest(createRequestDto(orderCode), OrderDetailsResponse.class);

    return new OrderData();
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

  private OrderData extractOrderData(ResponseEntity<OrderDetailsResponse> responseEntity) {
    if (responseEntity.getStatusCodeValue() != 200) {
      throw new ResourceAccessException("ERP Request failed with code " + responseEntity.getStatusCodeValue());
    }
    OrderDetailsResponse response = responseEntity.getBody();
    return orderDetailsConverter.convert(response);
  }

}
