package com.bridgex.facades.order.impl;

import java.util.Map;
import java.util.stream.Collectors;

import com.bridgex.facades.order.PentlandCartFacade;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.impl.DefaultCartFacade;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.VariantMatrixElementData;
import de.hybris.platform.core.model.order.CartModel;

/**
 * Created by dmitry.konovalov@masterdata.ru on 30.10.2017.
 */
public class DefaultPentlandCartFacade extends DefaultCartFacade implements PentlandCartFacade {

  @Override
  public void saveB2BCartData(CartData cartData) {
    if (hasSessionCart()) {
      final CartModel cart = getCartService().getSessionCart();
      cart.setPurchaseOrderNumber(cartData.getPurchaseOrderNumber());
      cart.setRdd(cartData.getRdd());
      cart.setCustomerNotes(cartData.getCustomerNotes());
      getModelService().save(cart);
    }
  }

  @Override
  public void populateVariantMatrixQuantity(ProductData productData) {
    if (hasSessionCart() && productData.getVariantMatrix() != null) {
      final CartModel cart = getCartService().getSessionCart();
      final Map<String, Long> codeToQuantity = getCodeToQuantityMap(cart);
      VariantMatrixElementData root = getVariantMatrixRoot(productData);
      root.getElements().stream().forEach(e -> populateSizeQuantities(e, codeToQuantity));
    }
  }

  protected void populateSizeQuantities(VariantMatrixElementData colorLevelVariantData, final Map<String, Long> codeToQuantity) {
    colorLevelVariantData.getElements().stream().filter(e -> codeToQuantity.containsKey(e.getVariantOption().getCode()))
                         .forEach(e -> e.setQty(codeToQuantity.get(e.getVariantOption().getCode())));
  }

  protected Map<String, Long> getCodeToQuantityMap(CartModel cart) {
    return cart.getEntries().stream().collect(Collectors.toMap(e -> e.getProduct().getCode(), e -> e.getQuantity()));
  }

  protected VariantMatrixElementData getVariantMatrixRoot(ProductData productData) {
    return productData.getVariantMatrix().get(0);
  }

}
