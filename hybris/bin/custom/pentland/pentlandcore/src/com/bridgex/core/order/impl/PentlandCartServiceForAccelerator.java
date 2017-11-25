package com.bridgex.core.order.impl;

import java.util.*;

import com.bridgex.core.order.PentlandCartService;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;

import de.hybris.platform.acceleratorservices.order.impl.DefaultCartServiceForAccelerator;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/8/2017.
 */
public class PentlandCartServiceForAccelerator extends DefaultCartServiceForAccelerator implements PentlandCartService{
  private static final Logger LOG = Logger.getLogger(PentlandCartServiceForAccelerator.class);

  private static final int APPEND_AS_LAST = -1;
  public static final String ORDER_DETAILS_KEY = "orderDetails:";
  private ProductService productService;

  public void setProductService(ProductService productService) {
    this.productService = productService;
  }

  @Override
  protected void checkQuantity(final long qty, final int number) {
    if (qty <= -1) {
      throw new IllegalArgumentException("Quantity must be zero or greater");
    }
    if (number < APPEND_AS_LAST) {
      throw new IllegalArgumentException("Number must be greater or equal -1");
    }
  }

  @Override
  public void updateQuantities(final CartModel cart, final Map<Integer, Long> quantities)
  {
    if (!MapUtils.isEmpty(quantities)) {
      final Collection<CartEntryModel> toRemove = new LinkedList<CartEntryModel>();
      final Collection<CartEntryModel> toSave = new LinkedList<CartEntryModel>();
      for (final Map.Entry<CartEntryModel, Long> e : getEntryQuantityMap(cart, quantities).entrySet()) {
        final CartEntryModel cartEntry = e.getKey();
        final Long quantity = e.getValue();
        if (quantity == null || quantity < 0) {
          toRemove.add(cartEntry);
        }else {
          cartEntry.setQuantity(quantity);
          toSave.add(cartEntry);
        }
      }
      getModelService().removeAll(toRemove);
      getModelService().saveAll(toSave);
      getModelService().refresh(cart);
    }
  }

  private Map<CartEntryModel, Long> getEntryQuantityMap(final CartModel cart, final Map<Integer, Long> quantities) {
    final List<CartEntryModel> entries = (List)cart.getEntries();

    final Map<CartEntryModel, Long> ret = new LinkedHashMap<CartEntryModel, Long>();

    for (final Map.Entry<Integer, Long> q : quantities.entrySet()) {
      final Integer entryNumber = q.getKey();
      final Long quantity = q.getValue();
      ret.put(getEntry(entries, entryNumber), quantity);
    }

    return ret;
  }

  private CartEntryModel getEntry(final List<CartEntryModel> entries, final Integer entryNumber) {
    for (final CartEntryModel e : entries) {
      if (entryNumber.equals(e.getEntryNumber())) {
        return e;
      }
    }
    throw new IllegalArgumentException("no cart entry found with entry number " + entryNumber + " (got " + entries + ")");
  }

  @Override
  public CartModel createCartFromSessionDetails(String orderCode) {
    Map<String, Integer> orderDetailsForReorder = getSessionService().getAttribute(ORDER_DETAILS_KEY + orderCode);
    removeSessionCart();
    CartModel cart = getSessionCart();
    orderDetailsForReorder.forEach((k,v) -> Optional.ofNullable(getProductForCode(k))
                                                    .ifPresent(product -> addNewEntry(cart, product, v, null)));
    getModelService().save(cart);
    return cart;
  }

  private ProductModel getProductForCode(String k) {
    try {
      return productService.getProductForCode(k);
    } catch (UnknownIdentifierException e) {
      if (LOG.isDebugEnabled()) {
        LOG.debug(String.format("Product with code %s not found", k));
      }
      return null;
    }
  }
}
