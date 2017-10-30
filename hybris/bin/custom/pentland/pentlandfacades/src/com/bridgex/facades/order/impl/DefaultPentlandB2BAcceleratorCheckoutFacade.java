package com.bridgex.facades.order.impl;

import de.hybris.platform.b2bacceleratorfacades.order.impl.DefaultB2BAcceleratorCheckoutFacade;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/27/2017.
 */
public class DefaultPentlandB2BAcceleratorCheckoutFacade extends DefaultB2BAcceleratorCheckoutFacade{

  @Override
  public boolean isNewAddressEnabledForCart() {
    return false;
  }
  @Override
  public boolean isRemoveAddressEnabledForCart(){
    return false;
  }

}
