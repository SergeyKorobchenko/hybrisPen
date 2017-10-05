package com.worldpay.service.payment.impl;

import com.worldpay.service.payment.WorldpayTokenEventReferenceCreationStrategy;
import de.hybris.platform.order.CartService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Required;

/**
 * Creates Token event reference
 */
public class DefaultWorldpayTokenEventReferenceCreationStrategy implements WorldpayTokenEventReferenceCreationStrategy {

    public static final String UNDERSCORE = "_";
    private CartService cartService;

    /**
     * Create Token event reference from cart code
     * @return
     */
    @Override
    public String createTokenEventReference() {
        final String cartCode = cartService.getSessionCart().getCode();
        return cartCode + UNDERSCORE + DateTime.now().getMillis();
    }

    @Required
    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }
}
