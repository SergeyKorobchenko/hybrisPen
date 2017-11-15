package com.bridgex.core.integration;

import com.bridgex.integration.domain.OrderDetailsDto;
import com.bridgex.integration.domain.OrderDetailsResponse;

/**
 * @author Goncharenko Mikhail, created on 15.11.2017.
 */
public interface PentlandOrderDetailsService {
  OrderDetailsResponse requestData(OrderDetailsDto request);
}
