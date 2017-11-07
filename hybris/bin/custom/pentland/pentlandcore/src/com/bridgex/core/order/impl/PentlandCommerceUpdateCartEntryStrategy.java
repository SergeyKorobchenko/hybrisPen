package com.bridgex.core.order.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceUpdateCartEntryStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;

/**
 * Created by dmitry.konovalov@masterdata.ru on 26.10.2017.
 */
public class PentlandCommerceUpdateCartEntryStrategy extends DefaultCommerceUpdateCartEntryStrategy {

  @Override
  public CommerceCartModification updateQuantityForCartEntry(final CommerceCartParameter parameters)
    throws CommerceCartModificationException
  {
    beforeUpdateCartEntry(parameters);
    final CartModel cartModel = parameters.getCart();
    final long newQuantity = parameters.getQuantity();
    final long entryNumber = parameters.getEntryNumber();

    validateParameterNotNull(cartModel, "Cart model cannot be null");
    CommerceCartModification modification;

    final AbstractOrderEntryModel entryToUpdate = getEntryForNumber(cartModel, (int) entryNumber);
    validateEntryBeforeModification(newQuantity, entryToUpdate);
    final Integer maxOrderQuantity = Integer.MAX_VALUE;
    // Work out how many we want to add (could be negative if we are
    // removing items)
    final long quantityToAdd = newQuantity - entryToUpdate.getQuantity().longValue();

    //Now do the actual cartModification
    modification = modifyEntry(cartModel, entryToUpdate, quantityToAdd, newQuantity, maxOrderQuantity);
    afterUpdateCartEntry(parameters, modification);
    return modification;

  }

}
