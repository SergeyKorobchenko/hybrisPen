package com.bridgex.facades.customer.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.constants.PentlandcoreConstants;
import com.bridgex.core.services.PentlandB2BUnitService;
import com.bridgex.facades.customer.PentlandCustomerFacade;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commercefacades.customer.impl.DefaultCustomerFacade;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/24/2017.
 */
public class DefaultPentlandCustomerFacade extends DefaultCustomerFacade implements PentlandCustomerFacade {

  private PentlandB2BUnitService b2BUnitService;

  @Override
  public void loginSuccess(){
    super.loginSuccess();
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
        return hasMarkFors;
      }
    }
    return false;
  }

  @Required
  public void setB2BUnitService(PentlandB2BUnitService b2BUnitService) {
    this.b2BUnitService = b2BUnitService;
  }
}
