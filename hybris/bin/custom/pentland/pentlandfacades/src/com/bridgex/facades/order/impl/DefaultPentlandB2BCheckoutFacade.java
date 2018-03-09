package com.bridgex.facades.order.impl;

import static de.hybris.platform.util.localization.Localization.getLocalizedString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bridgex.core.order.PentlandCartService;
import com.bridgex.facades.order.PentlandB2BCheckoutFacade;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.enums.B2BUnitType;

import de.hybris.platform.b2b.model.B2BCommentModel;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.b2bacceleratorfacades.checkout.data.PlaceOrderData;
import de.hybris.platform.b2bacceleratorfacades.exception.EntityValidationException;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BPaymentTypeData;
import de.hybris.platform.b2bacceleratorfacades.order.impl.DefaultB2BCheckoutFacade;
import de.hybris.platform.b2bacceleratorservices.enums.CheckoutPaymentType;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.exceptions.CalculationException;

import javax.annotation.Nullable;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/27/2017.
 */
public class DefaultPentlandB2BCheckoutFacade extends DefaultB2BCheckoutFacade implements PentlandB2BCheckoutFacade {

  private Map<B2BUnitType, List<CheckoutPaymentType>> b2bPaymentTypeMapping;
  private B2BUnitService<B2BUnitModel, UserModel>     b2bUnitService;
  private PentlandCartService                         pentlandCartService;
  private CalculationService                          calculationService;

  private static final String CART_CHECKOUT_DELIVERYADDRESS_INVALID = "cart.deliveryAddress.invalid";
  private static final String CART_CHECKOUT_DELIVERYMODE_INVALID = "cart.deliveryMode.invalid";
  private static final String CART_CHECKOUT_PAYMENTINFO_EMPTY = "cart.paymentInfo.empty";
  private static final String CART_CHECKOUT_NOT_CALCULATED = "cart.not.calculated";
  private static final String CART_CHECKOUT_QUOTE_REQUIREMENTS_NOT_SATISFIED = "cart.quote.requirements.not.satisfied";
  private static final String CART_CHECKOUT_PAYMENTTYPE_INVALID = "cart.paymenttype.invalid";

  private static final Logger LOG = Logger.getLogger(DefaultPentlandB2BCheckoutFacade.class);

  @Override
  @Nullable
  public B2BUnitType getCurrentCustomerType() {
    final B2BCustomerModel currentUser = (B2BCustomerModel)getCurrentUserForCheckout();

    final B2BUnitModel parent = getB2bUnitService().getParent(currentUser);
    return parent.getCustomerType();
  }

  @Override
  public List<B2BPaymentTypeData> getPaymentTypes() {

    final List<CheckoutPaymentType> checkoutPaymentTypes = new ArrayList<>();

    final B2BCustomerModel currentUser = (B2BCustomerModel)getCurrentUserForCheckout();

    final B2BUnitModel parent = getB2bUnitService().getParent(currentUser);
    if(parent.getCustomerType() != null){
      b2bPaymentTypeMapping.get(parent.getCustomerType()).forEach(paymentType -> {
        checkoutPaymentTypes.add(getEnumerationService().getEnumerationValue(CheckoutPaymentType._TYPECODE, paymentType.getCode()));
      });
    }else {
      checkoutPaymentTypes.addAll(getEnumerationService().getEnumerationValues(CheckoutPaymentType._TYPECODE));
    }
    return Converters.convertAll(checkoutPaymentTypes, getB2bPaymentTypeDataConverter());
  }

  @Override
  protected boolean isValidCheckoutCart(final PlaceOrderData placeOrderData)
  {
    final CartData cartData = getCheckoutCart();
    final boolean valid = true;

//    if (!cartData.isCalculated()) {
//      throw new EntityValidationException(getLocalizedString(CART_CHECKOUT_NOT_CALCULATED));
//    }

    if (cartData.getDeliveryAddress() == null) {
      throw new EntityValidationException(getLocalizedString(CART_CHECKOUT_DELIVERYADDRESS_INVALID));
    }

    final boolean accountPaymentType = CheckoutPaymentType.ACCOUNT.getCode().equals(cartData.getPaymentType().getCode());
    if (!accountPaymentType && cartData.getPaymentInfo() == null) {
      throw new EntityValidationException(getLocalizedString(CART_CHECKOUT_PAYMENTINFO_EMPTY));
    }

    if (Boolean.TRUE.equals(placeOrderData.getNegotiateQuote()) && !cartData.getQuoteAllowed()) {
      throw new EntityValidationException(getLocalizedString(CART_CHECKOUT_QUOTE_REQUIREMENTS_NOT_SATISFIED));
    }

    return valid;
  }

  @Override
  public void createCartFromSessionDetails(String orderCode) {
    CartModel cartModel = pentlandCartService.createCartFromSessionDetails(orderCode);
    try {
      getCalculationService().calculate(cartModel);
    }
    catch (CalculationException e) {
      LOG.error("Error during cart calculation", e);
    }
  }

    
  protected void setPaymentTypeForCart(final String paymentType, final CartModel cartModel)
  {
    final List<CheckoutPaymentType> checkoutPaymentTypes = getEnumerationService()
      .getEnumerationValues(CheckoutPaymentType._TYPECODE);
    if (!checkoutPaymentTypes.contains(CheckoutPaymentType.valueOf(paymentType)))
    {
      throw new EntityValidationException(getLocalizedString(CART_CHECKOUT_PAYMENTTYPE_INVALID));
    }

    cartModel.setPaymentType(CheckoutPaymentType.valueOf(paymentType));
    if (CheckoutPaymentType.ACCOUNT.getCode().equals(paymentType))
    {
      cartModel.setPaymentInfo(getCommerceCartService().createInvoicePaymentInfo(cartModel));
    }

  }

  @Override
  public CartData updateCheckoutCart(final CartData cartData) {
    final CartModel cartModel = getCart();
    if (cartModel == null) {
      return null;
    }
    // set payment type
    if (cartData.getPaymentType() != null) {
      final String newPaymentTypeCode = cartData.getPaymentType().getCode();

      // clear delivery address, delivery mode and payment details when changing payment type
      if (cartModel.getPaymentType() == null || !newPaymentTypeCode.equalsIgnoreCase(cartModel.getPaymentType().getCode())) {
        cartModel.setDeliveryAddress(null);
        cartModel.setDeliveryMode(null);
        cartModel.setPaymentInfo(null);
      }

      setPaymentTypeForCart(newPaymentTypeCode, cartModel);
    }

    // set purchase order number
    if (cartData.getPurchaseOrderNumber() != null) {
      cartModel.setPurchaseOrderNumber(cartData.getPurchaseOrderNumber());
    }

    // set delivery address
    if (cartData.getDeliveryAddress() != null) {
      setDeliveryAddress(cartData.getDeliveryAddress());
    }

    // set quote request description
    if (cartData.getB2BComment() != null) {
      final B2BCommentModel b2bComment = getModelService().create(B2BCommentModel.class);
      b2bComment.setComment(cartData.getB2BComment().getComment());
      getB2bCommentService().addComment(cartModel, b2bComment);
    }

    getModelService().save(cartModel);
    return getCheckoutCart();
  }

  @Required
  public void setB2bPaymentTypeMapping(Map<B2BUnitType, List<CheckoutPaymentType>> b2bPaymentTypeMapping) {
    this.b2bPaymentTypeMapping = b2bPaymentTypeMapping;
  }

  public B2BUnitService<B2BUnitModel, UserModel> getB2bUnitService() {
    return b2bUnitService;
  }

  @Required
  public void setB2bUnitService(B2BUnitService<B2BUnitModel, UserModel> b2bUnitService) {
    this.b2bUnitService = b2bUnitService;
  }

  public PentlandCartService getPentlandCartService() {
    return pentlandCartService;
  }

  @Required
  public void setPentlandCartService(PentlandCartService pentlandCartService) {
    this.pentlandCartService = pentlandCartService;
  }

  protected CalculationService getCalculationService() {
    return calculationService;
  }

  @Required
  public void setCalculationService(CalculationService calculationService) {
    this.calculationService = calculationService;
  }
}
