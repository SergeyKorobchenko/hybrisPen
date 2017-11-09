package com.bridgex.storefront.forms;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/8/2017.
 */
public class ZeroAddToCartForm {
  @NotNull(message = "{basket.error.quantity.notNull}")
  @Min(value = -1, message = "{basket.error.quantity.invalid}")
  @Digits(fraction = 0, integer = 10, message = "{basket.error.quantity.invalid}")
  private long qty = 0L;

  public void setQty(final long quantity)
  {
    this.qty = quantity;
  }

  public long getQty()
  {
    return qty;
  }
}
