package com.bridgex.core.order.impl;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.commerceservices.order.CommerceDeliveryAddressStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.model.ModelService;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/31/2017.
 */
public class DefaultMarkForAddressStrategy implements CommerceDeliveryAddressStrategy{

  private ModelService modelService;

  @Override
  public boolean storeDeliveryAddress(CommerceCheckoutParameter parameter) {
    final CartModel cartModel = parameter.getCart();
    final AddressModel addressModel = parameter.getMarkFor();

    modelService.refresh(cartModel);

    if(BooleanUtils.isTrue(addressModel.getMarkForAddress())){
      cartModel.setMarkFor(addressModel);
      modelService.save(addressModel);
      return true;
    }

    return false;
  }

  @Required
  public void setModelService(ModelService modelService) {
    this.modelService = modelService;
  }
}
