package com.bridgex.facades.order;

import com.bridgex.core.enums.B2BUnitType;
import de.hybris.platform.b2bacceleratorfacades.order.B2BCheckoutFacade;

import javax.annotation.Nullable;

/**
 * Created by naatsms on 20.11.17.
 */
public interface PentlandB2BCheckoutFacade extends B2BCheckoutFacade{

    @Nullable
    B2BUnitType getCurrentCustomerType();

    void createCartFromSessionDetails(String orderCode);

}
