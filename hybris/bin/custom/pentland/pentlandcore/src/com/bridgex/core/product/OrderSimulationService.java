package com.bridgex.core.product;

import java.util.List;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;

/**
 * @author Created by konstantin.pavlyukov on 11/8/2017.
 */
public interface OrderSimulationService {

  void simulateProduct(final ProductModel product);

  void simulateOrderForm(final List<ProductModel> products);

  void simulateFutureOrderForm(final List<ProductModel> products);

  void simulateCart(final CartModel cart);

}
