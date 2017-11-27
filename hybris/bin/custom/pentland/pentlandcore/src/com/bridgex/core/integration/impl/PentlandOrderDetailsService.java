package com.bridgex.core.integration.impl;

import com.bridgex.integration.domain.*;

import de.hybris.platform.b2b.services.B2BOrderService;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.CartEntry;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.client.ResourceAccessException;

import com.bridgex.core.integration.PentlandIntegrationService;
import com.bridgex.integration.constants.ErpintegrationConstants;
import com.bridgex.integration.service.IntegrationService;

import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Goncharenko Mikhail, created on 02.11.2017.
 */
public class PentlandOrderDetailsService implements PentlandIntegrationService<OrderDetailsDto,OrderDetailsResponse> {

  private static final String ORDER_DETAILS_SESSION_KEY = "orderDetails:%s";

  private IntegrationService<OrderDetailsDto, OrderDetailsResponse> integrationService;
  private CommonI18NService                                         commonI18NService;
  private SessionService                                            sessionService;
  private B2BOrderService                                           b2bOrderService;
  private ModelService                                              modelService;

  @Override
  public OrderDetailsResponse requestData(OrderDetailsDto request) {
    request.setLanguage(commonI18NService.getCurrentLanguage().getIsocode().toUpperCase());
    OrderDetailsResponse response = integrationService.sendRequest(request, OrderDetailsResponse.class).getBody();
    checkRequestSuccess(response);
    storeOrderDetailsInSession(response);
    updateOrderStatus(request.getOrderCode(), response.getStatus());
    return response;
  }

  private void updateOrderStatus(String code, String status) {
    OrderModel order = b2bOrderService.getOrderForCode(code);
    if (!status.equalsIgnoreCase(order.getStatus().getCode())) {
      order.setStatus(OrderStatus.valueOf(status));
      modelService.save(order);
    }
  }

  private void checkRequestSuccess(OrderDetailsResponse response) {
    if (response.getEtReturn() != null && !response.getEtReturn().isEmpty()) {
      ETReturnDto etReturn = response.getEtReturn().get(0);
      if (etReturn.getType().equals(ErpintegrationConstants.RESPONSE.ET_RETURN.ERROR_TYPE)) {
        throw new ResourceAccessException("ERP request failed with response: " + etReturn.getMessage());
      }
    }
  }

  private void storeOrderDetailsInSession(OrderDetailsResponse response) {
    Map<String, Integer> orderDetailsForReorder = response.getOrderEntries().stream()
            .flatMap(entry -> entry.getSizeVariants().stream())
            .collect(Collectors.toMap(SizeVariantDto::getEan, vr -> Double.valueOf(vr.getTotalQuantity()).intValue()));
    sessionService.setAttribute(String.format(ORDER_DETAILS_SESSION_KEY, response.getCode()), orderDetailsForReorder);
  }

  @Required
  public void setSessionService(SessionService sessionService) {
    this.sessionService = sessionService;
  }

  @Required
  public void setIntegrationService(IntegrationService<OrderDetailsDto, OrderDetailsResponse> integrationService) {
    this.integrationService = integrationService;
  }

  @Required
  public void setCommonI18NService(CommonI18NService commonI18NService) {
    this.commonI18NService = commonI18NService;
  }

  @Required
  public void setB2bOrderService(B2BOrderService b2bOrderService) {
    this.b2bOrderService = b2bOrderService;
  }

  @Required
  public void setModelService(ModelService modelService) {
    this.modelService = modelService;
  }
}