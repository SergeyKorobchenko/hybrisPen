package com.bridgex.facades.order.converters.populator;

import de.hybris.platform.commercefacades.order.converters.populator.CartPopulator;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;

/**
 * Created by dmitry.konovalov@masterdata.ru on 26.10.2017.
 */
public class PentlandCartPopulator<T extends CartData> extends CartPopulator<T> {

  @Override
  public void populate(final CartModel source, final T target) {
    super.populate(source, target);
  }

  @Override
  protected void addCommon(final AbstractOrderModel source, final AbstractOrderData target) {
    super.addCommon(source, target);

    target.setRdd(source.getRdd());
    target.setPurchaseOrderNumber(source.getPurchaseOrderNumber());
    target.setCustomerNotes(source.getCustomerNotes());

    this.addMarkForAddress(source, target);
  }

  private void addMarkForAddress(final AbstractOrderModel source, final AbstractOrderData target)
  {
    if (source.getMarkFor() != null)
    {
      target.setMarkForAddress(getAddressConverter().convert(source.getMarkFor()));
    }
  }

  protected void addTotals(final AbstractOrderModel source, final AbstractOrderData prototype) {
    final double orderDiscountsAmount = getOrderDiscountsAmount(source);
    final double quoteDiscountsAmount = getQuoteDiscountsAmount(source);

    prototype.setTotalPrice(createPrice(source, source.getTotalPrice()));
    prototype.setTotalTax(createPrice(source, source.getTotalTax()));
    final double subTotal = source.getSubtotal() - orderDiscountsAmount - quoteDiscountsAmount;
    final PriceData subTotalPriceData = createPrice(source, subTotal);
    prototype.setSubTotal(subTotalPriceData);
    prototype.setSubTotalWithoutQuoteDiscounts(createPrice(source, subTotal + quoteDiscountsAmount));
    prototype.setTotalPriceWithTax(prototype.getTotalPrice());
  }

}
