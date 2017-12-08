package com.bridgex.core.order;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartService;

/**
 * Created by naatsms on 20.11.17.
 */
public interface PentlandCartService extends CartService {

    CartModel createCartFromSessionDetails(String orderCode);

}
