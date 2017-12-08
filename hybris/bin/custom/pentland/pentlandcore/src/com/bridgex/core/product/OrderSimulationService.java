package com.bridgex.core.product;

import java.util.List;

import com.bridgex.integration.domain.MultiBrandCartDto;
import com.bridgex.integration.domain.MultiBrandCartResponse;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;

/**
 * @author Created by konstantin.pavlyukov on 11/8/2017.
 */
public interface OrderSimulationService {

    MultiBrandCartResponse simulateOrder(MultiBrandCartDto request);

    boolean successResponse(MultiBrandCartResponse response);
}
