package com.bridgex.storefront.controllers.pages;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.bridgex.facades.order.PentlandCartFacade;
import com.bridgex.storefront.forms.PentlandCartForm;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.AbstractController;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;

/**
 * @author Created by dmitry.konovalov@masterdata.ru on 13.11.2017.
 */
@Controller
public class UpdateCartController extends AbstractController
{

  private static final Logger LOG = Logger.getLogger(UpdateCartController.class);

  @Resource(name = "pentlandCartFacade")
  private PentlandCartFacade pentlandCartFacade;

  @PostMapping(value = "/cart/update-all")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateCartQuantitiesAll(@RequestBody final PentlandCartForm cartForm) {
    CartData cart = pentlandCartFacade.getSessionCart();
    cart.setPurchaseOrderNumber(cartForm.getPurchaseOrderNumber());
    cart.setRdd(cartForm.getRequestedDeliveryDate());
    cart.setCustomerNotes(cartForm.getCustomerNotes());
    pentlandCartFacade.saveB2BCartData(cart);

    final Set<String> multidErrorMsgs = new HashSet<String>();
    final List<CartModificationData> modificationDataList = new ArrayList<CartModificationData>();

    for (final OrderEntryData cartEntry : cartForm.getCartEntries()) {
      if (isValidProductEntry(cartEntry)) {
        if (!isValidQuantity(cartEntry)) {
          multidErrorMsgs.add("basket.error.quantity.invalid");
        }
        else {
          final String errorMsg = updateEntry(modificationDataList, cartEntry);
          if (StringUtils.isNotEmpty(errorMsg)) {
            multidErrorMsgs.add(errorMsg);
          }
        }
      }
    }
  }

  protected String updateEntry(final List<CartModificationData> modificationDataList, final OrderEntryData cartEntry)
  {
    String errorMsg = StringUtils.EMPTY;
    try
    {
      final CartModificationData cartModificationData = pentlandCartFacade.updateCartEntry(cartEntry);
      modificationDataList.add(cartModificationData);
    }
    catch (final CommerceCartModificationException ex)
    {
      errorMsg = "basket.error.occurred";
    }
    return errorMsg;
  }

  protected boolean isValidProductEntry(final OrderEntryData cartEntry)
  {
    return cartEntry.getProduct() != null && StringUtils.isNotBlank(cartEntry.getProduct().getCode());
  }

  protected boolean isValidQuantity(final OrderEntryData cartEntry)
  {
    return cartEntry.getQuantity() != null && cartEntry.getQuantity().longValue() >= 0;
  }

}
