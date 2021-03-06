package com.worldpay.worldpayresponsemock.responses.impl;

import com.worldpay.internal.model.*;
import com.worldpay.worldpayresponsemock.responses.WorldpayDirectAuthoriseResponseBuilder;

import java.util.List;
import java.util.Optional;

import static com.worldpay.worldpayresponsemock.builders.AddressBuilder.anAddressBuilder;
import static com.worldpay.worldpayresponsemock.builders.PaymentBuilder.aPaymentBuilder;
import static com.worldpay.worldpayresponsemock.builders.TokenBuilder.aTokenBuilder;

/**
 * {@inheritDoc}
 */
public class DefaultWorldpayDirectAuthoriseResponseBuilder implements WorldpayDirectAuthoriseResponseBuilder {

    private static final String AUTHORISED = "AUTHORISED";
    private static final String NEW_TOKEN_EVENT = "NEW";
    private static final String OBFUSCATED_PAN = "4111********1111";
    private static final String VISA_SSL = "VISA-SSL";

    /**
     * {@inheritDoc}
     */
    @Override
    public PaymentService buildDirectResponse(final PaymentService request) {
        final Submit submitRequest = (Submit) request.getSubmitOrModifyOrInquiryOrReplyOrNotifyOrVerify().get(0);

        final OrderStatus orderStatus = createOrderStatus(submitRequest);

        final Reply reply = new Reply();
        reply.getOrderStatusOrBatchStatusOrErrorOrAddressCheckResponseOrRefundableAmountOrAccountBatchOrShopperOrOkOrFuturePayAgreementStatusOrShopperAuthenticationResultOrFuturePayPaymentResultOrPricePointOrPaymentOptionOrToken().add(orderStatus);

        final PaymentService paymentService = new PaymentService();
        paymentService.setMerchantCode(request.getMerchantCode());
        paymentService.getSubmitOrModifyOrInquiryOrReplyOrNotifyOrVerify().add(reply);

        return paymentService;
    }

    // Needed because of false positive caused by java8 semantics
    @SuppressWarnings("squid:S2583")
    private OrderStatus createOrderStatus(final Submit submitRequest) {
        boolean shouldCreateToken = false;
        String tokenReason = "tokenReason";
        String authenticatedShopperId = null;

        final OrderStatus orderStatus = new OrderStatus();

        final List<Object> requestElements = submitRequest.getOrderOrOrderBatchOrShopperOrFuturePayAgreementOrMakeFuturePayPaymentOrIdentifyMeRequestOrPaymentTokenCreate();
        for (Object requestElement : requestElements) {
            if (requestElement instanceof Shopper) {
                Shopper shopper = (Shopper) requestElement;
                authenticatedShopperId = shopper.getAuthenticatedShopperID();
            }

            if (requestElement instanceof Order) {
                Order requestOrder = (Order) requestElement;
                final List<Object> orderElements = requestOrder.getDescriptionOrAmountOrRiskOrOrderContentOrPaymentMethodMaskOrPaymentDetailsOrPayAsOrderOrShopperOrShippingAddressOrBillingAddressOrBranchSpecificExtensionOrRedirectPageAttributeOrPaymentMethodAttributeOrEchoDataOrStatementNarrativeOrHcgAdditionalDataOrThirdPartyDataOrShopperAdditionalDataOrApprovedAmountOrMandateOrAuthorisationAmountStatusOrDynamic3DSOrCreateTokenOrOrderLinesOrSubMerchantDataOrDynamicMCCOrDynamicInteractionTypeOrInfo3DSecureOrSession();
                final Amount intAmount = (Amount) orderElements.stream().filter(Amount.class::isInstance).findFirst().get();
                final Payment payment = aPaymentBuilder()
                        .withTransactionAmount(intAmount.getValue()).withLastEvent(AUTHORISED).build();
                orderStatus.setOrderCode(requestOrder.getOrderCode());
                orderStatus.
                        getReferenceOrBankAccountOrErrorOrPaymentOrCardBalanceOrPaymentAdditionalDetailsOrBillingAddressDetailsOrOrderModificationOrJournalOrRequestInfoOrFxApprovalRequiredOrZappRTPOrContent().
                        add(payment);

                Optional<Object> createTokenOptional = orderElements.stream().filter(CreateToken.class::isInstance).findFirst();
                if (createTokenOptional.isPresent()) {
                    shouldCreateToken = true;
                    tokenReason = ((CreateToken) createTokenOptional.get()).getTokenReason().getvalue();
                }
            }
        }

        if (shouldCreateToken) {
            final Address addressForCardDetails = anAddressBuilder().build();
            final Token token = aTokenBuilder().withTokenEvent(NEW_TOKEN_EVENT).withCardBrand(VISA_SSL)
                    .withCardSubBrand("CREDIT").withIssuerCountryCode("GB").withObfuscatedPAN(OBFUSCATED_PAN)
                    .withCardHolderName("TEST_NAME").withCardAddress(addressForCardDetails).withAuthenticatedShopperId(authenticatedShopperId)
                    .withTokenDetailsTokenReason(tokenReason).withTokenReason(tokenReason).build();

            orderStatus.setToken(token);
        }
        return orderStatus;
    }
}
