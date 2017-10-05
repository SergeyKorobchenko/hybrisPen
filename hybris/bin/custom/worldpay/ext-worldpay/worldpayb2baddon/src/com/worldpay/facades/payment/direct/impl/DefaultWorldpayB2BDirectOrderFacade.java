package com.worldpay.facades.payment.direct.impl;

import com.worldpay.exception.WorldpayException;
import com.worldpay.facades.payment.direct.WorldpayB2BDirectOrderFacade;
import com.worldpay.order.data.WorldpayAdditionalInfoData;
import com.worldpay.payment.DirectResponseData;
import com.worldpay.payment.TransactionStatus;
import com.worldpay.service.model.MerchantInfo;
import de.hybris.platform.b2b.services.B2BOrderService;
import de.hybris.platform.b2bacceleratorfacades.order.B2BCheckoutFacade;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.InvalidCartException;
import org.springframework.beans.factory.annotation.Required;

/**
 * Implementation of the authorise operations that enables the Client Side Encryption with Worldpay
 */
public class DefaultWorldpayB2BDirectOrderFacade extends DefaultWorldpayDirectOrderFacade implements WorldpayB2BDirectOrderFacade {

    private B2BOrderService b2BOrderService;
    private B2BCheckoutFacade b2BCheckoutFacade;

    /**
     * {@inheritDoc}
     */
    @Override
    public DirectResponseData authoriseRecurringPayment(final String orderCode,
                                                        final WorldpayAdditionalInfoData worldpayAdditionalInfoData) throws WorldpayException, InvalidCartException {
        final AbstractOrderModel abstractOrderModel = b2BOrderService.getOrderForCode(orderCode);
        final MerchantInfo merchantInfo = getWorldpayMerchantInfoService().getCurrentSiteMerchant();
        return internalAuthoriseRecurringPayment(abstractOrderModel, worldpayAdditionalInfoData, merchantInfo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DirectResponseData authorise3DSecureOnOrder(final String orderCode,
                                                       final String paResponse,
                                                       final WorldpayAdditionalInfoData worldpayAdditionalInfoData) throws WorldpayException, InvalidCartException {
        final OrderModel orderModel = b2BOrderService.getOrderForCode(orderCode);
        return internalAuthorise3DSecure(orderModel, paResponse, worldpayAdditionalInfoData);
    }

    protected void handleAuthorisedResponse(final DirectResponseData response) throws InvalidCartException {
        final OrderData orderData = b2BCheckoutFacade.placeOrder();
        response.setOrderData(orderData);
        response.setTransactionStatus(TransactionStatus.AUTHORISED);
    }

    @Required
    public void setB2BOrderService(B2BOrderService b2BOrderService) {
        this.b2BOrderService = b2BOrderService;
    }

    @Required
    public void setB2BCheckoutFacade(B2BCheckoutFacade b2BCheckoutFacade) {
        this.b2BCheckoutFacade = b2BCheckoutFacade;
    }
}
