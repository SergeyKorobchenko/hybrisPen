package com.worldpay.service.response;

import com.worldpay.service.WorldpayServiceGateway;
import com.worldpay.service.model.Amount;
import com.worldpay.service.request.RefundServiceRequest;

/**
 * This class represents the details that are passed back from a call to {@link WorldpayServiceGateway#refund(RefundServiceRequest) refund()} in the
 * WorldpayServiceGateway
 * <p/>
 * <p>On top of the standard parameters it provides the amount that has been refunded</p>
 */
public class RefundServiceResponse extends AbstractServiceResponse {

    private Amount amount;

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }
}
