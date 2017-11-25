package com.bridgex.facades.order;

import de.hybris.platform.b2bacceleratorfacades.order.B2BCheckoutFacade;

/**
 * Created by naatsms on 20.11.17.
 */
public interface PentlandB2BCheckoutFacade extends B2BCheckoutFacade{

    void createCartFromSessionDetails(String orderCode);

}
