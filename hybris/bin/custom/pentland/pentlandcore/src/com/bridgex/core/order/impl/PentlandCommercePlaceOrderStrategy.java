package com.bridgex.core.order.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.order.PentlandOrderExportService;
import com.bridgex.core.services.PentlandB2BUnitService;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commerceservices.order.impl.DefaultCommercePlaceOrderStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.exceptions.CalculationException;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/2/2017.
 */
public class PentlandCommercePlaceOrderStrategy extends DefaultCommercePlaceOrderStrategy{

  private static final Logger LOG = Logger.getLogger(PentlandCommercePlaceOrderStrategy.class);

  private PentlandOrderExportService pentlandOrderExportService;
  private PentlandB2BUnitService     b2BUnitService;

  @Override
  public CommerceOrderResult placeOrder(final CommerceCheckoutParameter parameter) throws InvalidCartException
  {
    final CartModel cartModel = parameter.getCart();
    validateParameterNotNull(cartModel, "Cart model cannot be null");
    final CommerceOrderResult result = new CommerceOrderResult();
    try {
      beforePlaceOrder(parameter);

      final CustomerModel customer = (CustomerModel) cartModel.getUser();
      validateParameterNotNull(customer, "Customer model cannot be null");

      final OrderModel orderModel = getOrderService().createOrderFromCart(cartModel);
      if (orderModel != null) {
        // Reset the Date attribute for use in determining when the order was placed
        orderModel.setDate(new Date());

        // Store the current site and store on the order
        orderModel.setSite(getBaseSiteService().getCurrentBaseSite());
        orderModel.setStore(getBaseStoreService().getCurrentBaseStore());
        orderModel.setLanguage(getCommonI18NService().getCurrentLanguage());

        if (parameter.getSalesApplication() != null) {
          orderModel.setSalesApplication(parameter.getSalesApplication());
        }


        getModelService().saveAll(customer, orderModel);

        if (cartModel.getPaymentInfo() != null && cartModel.getPaymentInfo().getBillingAddress() != null) {
          final AddressModel billingAddress = cartModel.getPaymentInfo().getBillingAddress();
          billingAddress.setSapCustomerID(StringUtils.EMPTY);
          orderModel.setPaymentAddress(billingAddress);
          orderModel.getPaymentInfo().setBillingAddress(getModelService().clone(billingAddress));
          getModelService().save(orderModel.getPaymentInfo());
        }
        getModelService().save(orderModel);

        String sapCustomerID = "";
        //remove sapCustomerId from all addresses to avoid them showing up in address listings
        AddressModel deliveryAddress = orderModel.getDeliveryAddress();
        if(deliveryAddress != null){
          sapCustomerID = deliveryAddress.getSapCustomerID();
          deliveryAddress.setSapCustomerID(StringUtils.EMPTY);
          deliveryAddress.setAddressID(String.join("-", orderModel.getCode(),deliveryAddress.getAddressID()));
          getModelService().save(deliveryAddress);
        }
        AddressModel markFor = orderModel.getMarkFor();
        if(markFor != null){
          if(StringUtils.isEmpty(sapCustomerID)){
            sapCustomerID = markFor.getSapCustomerID();
          }
          markFor.setSapCustomerID(StringUtils.EMPTY);
          markFor.setAddressID(String.join("-", orderModel.getCode(),markFor.getAddressID()));
          getModelService().save(markFor);
        }

        //check that cart b2bUnit is the same as the one address was for
        if(StringUtils.isNotEmpty(sapCustomerID)){
          B2BUnitModel unit = orderModel.getUnit()!=null?orderModel.getUnit():b2BUnitService.getParent((B2BCustomerModel) orderModel.getUser());
          if(!sapCustomerID.equals(unit.getSapID())){
            B2BUnitModel unitBySapID = b2BUnitService.getUnitBySapID(sapCustomerID);
            orderModel.setUnit(unitBySapID);
            getModelService().save(orderModel);
          }
        }

        // Calculate the order now that it has been copied
        try {
          getCalculationService().calculateTotals(orderModel, false);
          getExternalTaxesService().calculateExternalTaxes(orderModel);
        }
        catch (final CalculationException ex) {
          LOG.error("Failed to calculate order [" + orderModel + "]", ex);
        }

        getModelService().refresh(orderModel);
        getModelService().refresh(customer);

        result.setOrder(orderModel);

        this.beforeSubmitOrder(parameter, result);

        getOrderService().submitOrder(orderModel);

        pentlandOrderExportService.exportOrder(orderModel);
      }
      else {
        throw new IllegalArgumentException(String.format("Order was not properly created from cart %s", cartModel.getCode()));
      }
    }finally {
      getExternalTaxesService().clearSessionTaxDocument();
    }

    this.afterPlaceOrder(parameter, result);
    return result;
  }

  @Required
  public void setPentlandOrderExportService(PentlandOrderExportService pentlandOrderExportService) {
    this.pentlandOrderExportService = pentlandOrderExportService;
  }

  @Required
  public void setB2BUnitService(PentlandB2BUnitService b2BUnitService) {
    this.b2BUnitService = b2BUnitService;
  }
}
