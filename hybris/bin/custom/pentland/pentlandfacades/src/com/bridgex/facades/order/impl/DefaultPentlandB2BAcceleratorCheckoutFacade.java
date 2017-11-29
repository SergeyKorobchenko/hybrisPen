package com.bridgex.facades.order.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.order.PentlandCommerceCheckoutService;
import com.bridgex.facades.order.PentlandAcceleratorCheckoutFacade;

import de.hybris.platform.b2bacceleratorfacades.order.impl.DefaultB2BAcceleratorCheckoutFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.AddressModel;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/27/2017.
 */
public class DefaultPentlandB2BAcceleratorCheckoutFacade extends DefaultB2BAcceleratorCheckoutFacade implements PentlandAcceleratorCheckoutFacade{

  private PentlandCommerceCheckoutService pentlandCommerceCheckoutService;

  @Override
  public boolean isNewAddressEnabledForCart() {
    return false;
  }
  @Override
  public boolean isRemoveAddressEnabledForCart(){
    return false;
  }

  @Override
  public List<AddressData> findMarkForAddressesForShippingAddress(String addressId) {

    AddressModel deliveryAddress = getAddressModelForID(addressId);
    Collection<AddressModel> markForAddresses = deliveryAddress.getMarkForAddresses();

    if(CollectionUtils.isNotEmpty(markForAddresses)){
      List<AddressModel> markFors = markForAddresses.stream().filter(AddressModel::getMarkForAddress).collect(Collectors.toList());
      return Converters.convertAll(markFors, getAddressConverter());
    }

    return Collections.EMPTY_LIST;
  }

  @Override
  public boolean setMarkForAddressForCart(String addressId) {
    final CartModel cartModel = getCart();
    AddressModel deliveryAddress = getAddressModelForID(addressId);

    final CommerceCheckoutParameter parameter = createCommerceCheckoutParameter(cartModel, true);
    parameter.setMarkFor(deliveryAddress);
    return pentlandCommerceCheckoutService.setMarkForAddress(parameter);

  }

  @Override
  public boolean removeMarkForAddressFromCart() {
    final CartModel cartModel = getCart();
    final CommerceCheckoutParameter parameter = createCommerceCheckoutParameter(cartModel, true);
    parameter.setMarkFor(null);
    return pentlandCommerceCheckoutService.setMarkForAddress(parameter);
  }

  @Override
  public void cleanupZeroQuantityEntries() {
    final CartModel cartModel = getCart();
    if(CollectionUtils.isNotEmpty(cartModel.getEntries())){
      Map<Integer, Long> quantities = new HashMap<>();
      cartModel.getEntries().stream().filter(entry -> entry.getQuantity() == 0).forEach(entry -> quantities.put(entry.getEntryNumber(), null));
      if(MapUtils.isNotEmpty(quantities)){
        getCartService().updateQuantities(cartModel, quantities);
      }
    }
  }

  private AddressModel getAddressModelForID(String addressId) {return getModelService().get(PK.fromLong(Long.parseLong(addressId)));}

  @Required
  public void setPentlandCommerceCheckoutService(PentlandCommerceCheckoutService pentlandCommerceCheckoutService) {
    this.pentlandCommerceCheckoutService = pentlandCommerceCheckoutService;
  }
}
