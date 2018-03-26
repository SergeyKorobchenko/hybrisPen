package com.bridgex.facades.order.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.model.ApparelSizeVariantProductModel;
import com.bridgex.core.order.PentlandCommerceCheckoutService;
import com.bridgex.facades.order.PentlandAcceleratorCheckoutFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.b2bacceleratorfacades.order.impl.DefaultB2BAcceleratorCheckoutFacade;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
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
  public List<AddressData> findMarkForAddressesForCustomerShippingAddress(List<AddressData> addresses) {
    final Set<AddressModel> customerAddresses = new HashSet<AddressModel>();
    for (AddressData addressData : addresses) {
     
     AddressModel deliveryAddress = getAddressModelForID(addressData.getId());
       Collection<AddressModel> markForAddresses = deliveryAddress.getMarkForAddresses();

       if(CollectionUtils.isNotEmpty(markForAddresses)){
         List<AddressModel> markFors = markForAddresses.stream().filter(AddressModel::getMarkForAddress).collect(Collectors.toList());
         customerAddresses.addAll(markFors);
       }
    }
   return Converters.convertAll(customerAddresses, getAddressConverter());
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

  @Override
  public boolean isCartTotalQuantityZero() {
    final CartModel cartModel = getCart();
    if(CollectionUtils.isNotEmpty(cartModel.getEntries())){
      return !cartModel.getEntries().stream().filter(entry -> entry.getQuantity() > 0).findAny().isPresent();
    }
    return true;
  }

  @Override
  public boolean cartHasZeroQuantityBaseEntries() {
    final CartModel cartModel = getCart();
    if(CollectionUtils.isNotEmpty(cartModel.getEntries())){
      Map<ProductModel, List<AbstractOrderEntryModel>> entriesGroupedByBaseProduct =
        cartModel.getEntries().stream().filter(entry -> entry.getProduct() instanceof ApparelSizeVariantProductModel)
                  .collect(Collectors.groupingBy(entry -> ((VariantProductModel)(((VariantProductModel) entry.getProduct()).getBaseProduct())).getBaseProduct()));
      for(Map.Entry<ProductModel, List<AbstractOrderEntryModel>> entry: entriesGroupedByBaseProduct.entrySet()){
        if (entry.getValue().stream().collect(Collectors.summingLong(AbstractOrderEntryModel::getQuantity)) == 0){
          return true;
        }
      }
      return false;
    }
    return true;
  }

  private AddressModel getAddressModelForID(String addressId) {return getModelService().get(PK.fromLong(Long.parseLong(addressId)));}

  @Required
  public void setPentlandCommerceCheckoutService(PentlandCommerceCheckoutService pentlandCommerceCheckoutService) {
    this.pentlandCommerceCheckoutService = pentlandCommerceCheckoutService;
  }
}
