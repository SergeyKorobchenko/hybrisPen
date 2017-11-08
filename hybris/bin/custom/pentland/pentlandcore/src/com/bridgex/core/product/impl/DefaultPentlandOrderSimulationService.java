package com.bridgex.core.product.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.product.OrderSimulationService;
import com.bridgex.integration.domain.MultiBrandCartDto;
import com.bridgex.integration.domain.MultiBrandCartResponse;
import com.bridgex.integration.service.IntegrationService;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;

/**
 * @author Created by konstantin.pavlyukov on 11/8/2017.
 */
public class DefaultPentlandOrderSimulationService implements OrderSimulationService {

  private IntegrationService<MultiBrandCartDto, MultiBrandCartResponse> integrationService;

  public IntegrationService<MultiBrandCartDto, MultiBrandCartResponse> getIntegrationService() {
    return integrationService;
  }

  @Required
  public void setIntegrationService(IntegrationService<MultiBrandCartDto, MultiBrandCartResponse> integrationService) {
    this.integrationService = integrationService;
  }

  @Override
  public void simulateProduct(ProductModel product) {

  }

  @Override
  public void simulateOrderForm(List<ProductModel> products) {

  }

  @Override
  public void simulateFutureOrderForm(List<ProductModel> products) {

  }

  @Override
  public void simulateCart(CartModel cart) {

  }
}
