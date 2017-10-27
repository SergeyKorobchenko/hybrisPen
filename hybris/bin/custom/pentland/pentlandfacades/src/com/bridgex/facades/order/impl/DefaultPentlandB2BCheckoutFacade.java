package com.bridgex.facades.order.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.enums.B2BUnitType;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BPaymentTypeData;
import de.hybris.platform.b2bacceleratorfacades.order.impl.DefaultB2BCheckoutFacade;
import de.hybris.platform.b2bacceleratorservices.enums.CheckoutPaymentType;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.user.UserModel;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/27/2017.
 */
public class DefaultPentlandB2BCheckoutFacade extends DefaultB2BCheckoutFacade{

  private Map<B2BUnitType, List<CheckoutPaymentType>> b2bPaymentTypeMapping;
  private B2BUnitService<B2BUnitModel, UserModel>     b2bUnitService;

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
}
