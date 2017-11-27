package com.bridgex.facades.customer.impl;

import com.bridgex.core.constants.PentlandcoreConstants;
import com.bridgex.facades.customer.PentlandCustomerFacade;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.commercefacades.customer.impl.DefaultCustomerFacade;
import de.hybris.platform.core.model.user.UserModel;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/24/2017.
 */
public class DefaultPentlandCustomerFacade extends DefaultCustomerFacade implements PentlandCustomerFacade {

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
}
