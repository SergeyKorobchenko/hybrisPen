package com.bridgex.facades.customer.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.constants.PentlandcoreConstants;
import com.bridgex.core.customer.PentlandCustomerAccountService;
import com.bridgex.core.services.PentlandB2BUnitService;
import com.bridgex.core.strategies.CustomerIdentifierStrategy;
import com.bridgex.facades.customer.PentlandCustomerFacade;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commercefacades.customer.impl.DefaultCustomerFacade;
import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commercefacades.user.data.RegisterData;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.exceptions.CalculationException;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/24/2017.
 */
public class DefaultPentlandCustomerFacade extends DefaultCustomerFacade implements PentlandCustomerFacade {

  private static final Logger LOG = Logger.getLogger(DefaultPentlandCustomerFacade.class);

  private PentlandB2BUnitService         b2BUnitService;
  private PentlandCustomerAccountService pentlandCustomerAccountService;
  private CheckoutFacade                 checkoutFacade;
  private ProductFacade                  productFacade;
  private CustomerIdentifierStrategy     customerIdentifierStrategy;

  @Override
  public void loginSuccess(){
    final CustomerData userData = getCurrentCustomer();

    // First thing to do is to try to change the user on the session cart
    if (getCartService().hasSessionCart()) {
      getCartService().changeCurrentCartUser(getCurrentUser());
    }

    // Update the session currency (which might change the cart currency)
    if (!updateSessionCurrency(userData.getCurrency(), getStoreSessionFacade().getDefaultCurrency())) {
      // Update the user
      getUserFacade().syncSessionCurrency();
    }
    if (!updateSessionLanguage(userData.getLanguage(), getStoreSessionFacade().getDefaultLanguage())) {
      // Update the user
      getUserFacade().syncSessionLanguage();
    }

    // Calculate the cart after setting everything up
    if (getCartService().hasSessionCart()) {
      final CartModel sessionCart = getCartService().getSessionCart();

      // Clean the existing info on the cart if it does not beling to the current user
      getCartCleanStrategy().cleanCart(sessionCart);
      try {
        final CommerceCartParameter parameter = new CommerceCartParameter();
        parameter.setEnableHooks(true);
        parameter.setCart(sessionCart);
        getCommerceCartService().recalculateCart(parameter);
      }catch (final CalculationException ex) {
        LOG.error("Failed to recalculate order [" + sessionCart.getCode() + "]", ex);
      }
    }
    UserModel currentUser = getCurrentUser();
    if(currentUser instanceof B2BCustomerModel){
      B2BCustomerModel currentCustomer = (B2BCustomerModel) currentUser;
      boolean hidePrices = currentCustomer.isHidePrices();
      getSessionService().setAttribute(PentlandcoreConstants.SessionParameters.HIDE_PRICES_FOR_USER, hidePrices);
    }
  }

  @Override
  public void setCustomerHidePricesOption(boolean hidePrices) {
    UserModel currentUser = getCurrentUser();
    if(currentUser instanceof B2BCustomerModel){
      B2BCustomerModel currentCustomer = (B2BCustomerModel) currentUser;
      currentCustomer.setHidePrices(hidePrices);
      getModelService().save(currentCustomer);
      getSessionService().setAttribute(PentlandcoreConstants.SessionParameters.HIDE_PRICES_FOR_USER, currentCustomer.isHidePrices());
    }
  }

  @Override
  public boolean hasMarkFors() {
    UserModel currentUser = getCurrentUser();
    final CartData cartData = checkoutFacade.getCheckoutCart();
    List<OrderEntryData> entries = cartData.getEntries();
    boolean hasMitreProduct=false;
    for (OrderEntryData orderEntryData : entries) {
    	ProductData productForCodeAndOptions = productFacade.getProductForCodeAndOptions(orderEntryData.getProduct().getCode(),Arrays.asList(ProductOption.BRAND));
        orderEntryData.setProduct(productForCodeAndOptions);
        if(orderEntryData.getProduct().getBrandCode()!=null)
        {
        	if(orderEntryData.getProduct().getBrandCode().equals("05"))
        	{
        		hasMitreProduct=true;
        	}
        }
	}
    
    if(currentUser instanceof B2BCustomerModel) {
      B2BCustomerModel currentCustomer = (B2BCustomerModel) currentUser;
      Collection<B2BUnitModel> firstLevelParents = b2BUnitService.getFirstLevelParents(currentCustomer);

      if(CollectionUtils.isNotEmpty(firstLevelParents)){
        List<AddressModel> allUnitAddresses = new ArrayList<>();
        for(B2BUnitModel b2BUnit: firstLevelParents){
          allUnitAddresses.addAll(b2BUnitService.findDeliveryAddressesForUnits(b2BUnit));
        }
        boolean hasMarkFors = allUnitAddresses.stream()
                                          .filter(address -> BooleanUtils.isTrue(address.getMarkForAddress()) || CollectionUtils.isNotEmpty(address.getMarkForAddresses()))
                                          .findFirst()
                                          .isPresent();
        return (hasMarkFors && hasMitreProduct) ;
      }
    }
    return false;
  }

  @Override
  protected void setUidForRegister(RegisterData registerData, CustomerModel customer) {
    String uid = customerIdentifierStrategy.createUidFromLogin(registerData.getLogin());
    customer.setUid(uid);
    customer.setOriginalUid(registerData.getLogin());
  }

  @Required
  public void setB2BUnitService(PentlandB2BUnitService b2BUnitService) {
    this.b2BUnitService = b2BUnitService;
  }

@Override
public List<AddressData> getDeliveryAddressesForCustomer() {
	
	UserModel currentUser = getCurrentUser();
	List<AddressModel> deliveryAddressesForCustomer = getPentlandCustomerAccountService().getDeliveryAddressesForCustomer(currentUser);

	     return new ArrayList<>(getAddressConverter().convertAll(deliveryAddressesForCustomer));
	
}

public PentlandCustomerAccountService getPentlandCustomerAccountService() {
	return pentlandCustomerAccountService;
}

public void setPentlandCustomerAccountService(PentlandCustomerAccountService pentlandCustomerAccountService) {
	this.pentlandCustomerAccountService = pentlandCustomerAccountService;
}
public CheckoutFacade getCheckoutFacade() {
	return checkoutFacade;
}

public void setCheckoutFacade(CheckoutFacade checkoutFacade) {
	this.checkoutFacade = checkoutFacade;
}

public ProductFacade getProductFacade() {
	return productFacade;
}

public void setProductFacade(ProductFacade productFacade) {
	this.productFacade = productFacade;
}

  public void setCustomerIdentifierStrategy(CustomerIdentifierStrategy customerIdentifierStrategy) {
    this.customerIdentifierStrategy = customerIdentifierStrategy;
  }
}
