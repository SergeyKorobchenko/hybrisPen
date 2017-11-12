package com.bridgex.core.order.impl;

import de.hybris.platform.acceleratorservices.order.impl.DefaultCartServiceForAccelerator;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/8/2017.
 */
public class PentlandCartServiceForAccelerator extends DefaultCartServiceForAccelerator{

  private static final int APPEND_AS_LAST = -1;

  @Override
  protected void checkQuantity(final long qty, final int number) {
    if (qty <= -1) {
      throw new IllegalArgumentException("Quantity must be zero or greater");
    }
    if (number < APPEND_AS_LAST) {
      throw new IllegalArgumentException("Number must be greater or equal -1");
    }
  }

}
