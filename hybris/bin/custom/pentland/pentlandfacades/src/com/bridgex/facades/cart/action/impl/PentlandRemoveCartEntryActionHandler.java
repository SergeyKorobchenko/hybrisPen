package com.bridgex.facades.cart.action.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import de.hybris.platform.acceleratorfacades.cart.action.exceptions.CartEntryActionException;
import de.hybris.platform.acceleratorfacades.cart.action.impl.RemoveCartEntryActionHandler;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 12/4/2017.
 */
public class PentlandRemoveCartEntryActionHandler extends RemoveCartEntryActionHandler{
  @Override
  public Optional<String> handleAction(final List<Long> entryNumbers) throws CartEntryActionException {
    validateParameterNotNullStandardMessage("entryNumbers", entryNumbers);

    // Since the entry number of the order entries might be updated after each remove of the entry, need to start removing from last entry
    final List<Long> uniqueEntryNumbers = entryNumbers.stream().distinct().collect(Collectors.toList());
    Collections.sort(uniqueEntryNumbers, Collections.reverseOrder());
    try {
      for (final Long entryNumber : uniqueEntryNumbers) {
        getCartFacade().updateCartEntry(entryNumber, -1);
      }
    } catch (final CommerceCartModificationException e) {
      throw new CartEntryActionException("Failed to delete cart entry", e);
    }
    return Optional.empty();
  }
}
