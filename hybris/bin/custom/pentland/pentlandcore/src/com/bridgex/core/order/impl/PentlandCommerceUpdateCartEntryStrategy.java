package com.bridgex.core.order.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import java.util.HashSet;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartModificationStatus;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceUpdateCartEntryStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.servicelayer.model.ModelService;

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

  protected CommerceCartModification modifyEntry(final CartModel cartModel, final AbstractOrderEntryModel entryToUpdate,
                                                 final long actualAllowedQuantityChange, final long newQuantity, final Integer maxOrderQuantity)
  {
    // Now work out how many that leaves us with on this entry
    final long entryNewQuantity = entryToUpdate.getQuantity() + actualAllowedQuantityChange;

    final ModelService modelService = getModelService();

    if (entryNewQuantity <= -1)
    {
      final CartEntryModel entry = new CartEntryModel()
      {
        @Override
        public Double getBasePrice()
        {
          return null;
        }

        @Override
        public Double getTotalPrice()
        {
          return null;
        }
      };
      entry.setOrder(cartModel);
      entry.setEntryGroupNumbers(new HashSet<>(entryToUpdate.getEntryGroupNumbers()));
      entry.setProduct(entryToUpdate.getProduct());

      // The allowed new entry quantity is zero or negative
      // just remove the entry
      modelService.remove(entryToUpdate);
      modelService.refresh(cartModel);
      normalizeEntryNumbers(cartModel);

      getCommerceCartCalculationStrategy().calculateCart(cartModel);

      // Return an empty modification
      final CommerceCartModification modification = new CommerceCartModification();
      modification.setEntry(entry);
      modification.setQuantity(0);
      // We removed all the quantity from this row
      modification.setQuantityAdded(-entryToUpdate.getQuantity());

      if (newQuantity == -1)
      {
        modification.setStatusCode(CommerceCartModificationStatus.SUCCESS);
      }
      else
      {
        modification.setStatusCode(CommerceCartModificationStatus.LOW_STOCK);
      }

      return modification;
    }
    else
    {
      // Adjust the entry quantity to the new value
      entryToUpdate.setQuantity(entryNewQuantity);
      modelService.save(entryToUpdate);
      modelService.refresh(cartModel);

      getCommerceCartCalculationStrategy().calculateCart(cartModel);
      modelService.refresh(entryToUpdate);

      // Return the modification data
      final CommerceCartModification modification = new CommerceCartModification();
      modification.setQuantityAdded(actualAllowedQuantityChange);
      modification.setEntry(entryToUpdate);
      modification.setQuantity(entryNewQuantity);

      if (isMaxOrderQuantitySet(maxOrderQuantity) && entryNewQuantity == maxOrderQuantity.longValue())
      {
        modification.setStatusCode(CommerceCartModificationStatus.MAX_ORDER_QUANTITY_EXCEEDED);
      }
      else if (newQuantity == entryNewQuantity)
      {
        modification.setStatusCode(CommerceCartModificationStatus.SUCCESS);
      }
      else
      {
        modification.setStatusCode(CommerceCartModificationStatus.LOW_STOCK);
      }

      return modification;
    }
  }

}
