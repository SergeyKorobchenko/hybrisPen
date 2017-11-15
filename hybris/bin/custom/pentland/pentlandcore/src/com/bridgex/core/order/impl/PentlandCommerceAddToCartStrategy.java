package com.bridgex.core.order.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartModificationStatus;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceAddToCartStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/8/2017.
 */
public class PentlandCommerceAddToCartStrategy extends DefaultCommerceAddToCartStrategy{

  @Override
  protected void validateAddToCart(final CommerceCartParameter parameters) throws CommerceCartModificationException {
    final CartModel cartModel = parameters.getCart();
    final ProductModel productModel = parameters.getProduct();

    validateParameterNotNull(cartModel, "Cart model cannot be null");
    validateParameterNotNull(productModel, "Product model cannot be null");
    if (productModel.getVariantType() != null) {
      throw new CommerceCartModificationException("Choose a variant instead of the base product");
    }

    if (parameters.getQuantity() < 0) {
      throw new CommerceCartModificationException("Quantity must not be less than zero");
    }
  }

  @Override
  protected CommerceCartModification doAddToCart(final CommerceCartParameter parameter) throws CommerceCartModificationException {
    CommerceCartModification modification;

    final CartModel cartModel = parameter.getCart();
    final ProductModel productModel = parameter.getProduct();
    final long quantityToAdd = parameter.getQuantity();
    final PointOfServiceModel deliveryPointOfService = parameter.getPointOfService();

    this.beforeAddToCart(parameter);
    validateAddToCart(parameter);

    if (isProductForCode(parameter).booleanValue()) {
      // So now work out what the maximum allowed to be added is (note that this may be negative!)
      final long actualAllowedQuantityChange = getAllowedCartAdjustmentForProduct(cartModel, productModel, quantityToAdd,
                                                                                  deliveryPointOfService);
      final Integer maxOrderQuantity = productModel.getMaxOrderQuantity();
      final long cartLevel = checkCartLevel(productModel, cartModel, deliveryPointOfService);
      final long cartLevelAfterQuantityChange = actualAllowedQuantityChange + cartLevel;

      if (actualAllowedQuantityChange > -1) {
        // We are allowed to add items to the cart
        final CartEntryModel entryModel = addCartEntry(parameter, actualAllowedQuantityChange);
        getModelService().save(entryModel);

        final String statusCode = getStatusCodeAllowedQuantityChange(actualAllowedQuantityChange, maxOrderQuantity,
                                                                     quantityToAdd, cartLevelAfterQuantityChange);

        modification = createAddToCartResp(parameter, statusCode, entryModel, actualAllowedQuantityChange);
      }
      else {
        // Not allowed to add any quantity, or maybe even asked to reduce the quantity
        // Do nothing!
        final String status = getStatusCodeForNotAllowedQuantityChange(maxOrderQuantity, maxOrderQuantity);

        modification = createAddToCartResp(parameter, status, createEmptyCartEntry(parameter), 0);

      }
    }else {
      modification = createAddToCartResp(parameter, CommerceCartModificationStatus.UNAVAILABLE,
                                         createEmptyCartEntry(parameter), 0);
    }

    return modification;
  }

  @Override
  protected void mergeEntry(@Nonnull final CommerceCartModification modification, @Nonnull final CommerceCartParameter parameter)
    throws CommerceCartModificationException {
    ServicesUtil.validateParameterNotNullStandardMessage("modification", modification);
    if (modification.getEntry() == null || Objects.equals(modification.getEntry().getQuantity(), -1L)) {
      // nothing to merge
      return;
    }
    ServicesUtil.validateParameterNotNullStandardMessage("parameter", parameter);
    if (parameter.isCreateNewEntry()) {
      return;
    }
    final AbstractOrderModel cart = modification.getEntry().getOrder();
    if (cart == null) {
      // The entry is not in cart (most likely it's a stub)
      return;
    }
    final AbstractOrderEntryModel mergeTarget = getEntryMergeStrategy().getEntryToMerge(cart.getEntries(),
                                                                                        modification.getEntry());
    if (mergeTarget == null) {
      if (parameter.getEntryNumber() != CommerceCartParameter.DEFAULT_ENTRY_NUMBER) {
        throw new CommerceCartModificationException("The new entry can not be merged into the entry #"
                                                    + parameter.getEntryNumber() + ". Give a correct value or " + CommerceCartParameter.DEFAULT_ENTRY_NUMBER
                                                    + " to accept any suitable entry.");
      }
    }
    else {
      // Merge the original entry into the merge target and remove the original entry.
      final Map<Integer, Long> entryQuantities = new HashMap<>(2);
      entryQuantities.put(mergeTarget.getEntryNumber(), modification.getEntry().getQuantity() + mergeTarget.getQuantity());
      entryQuantities.put(modification.getEntry().getEntryNumber(), -1L);
      getCartService().updateQuantities(parameter.getCart(), entryQuantities);
      modification.setEntry(mergeTarget);
    }

  }
}
