package com.bridgex.b2cstorefront.controllers.integration;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

import java.util.Collections;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.bridgex.facades.cart.dto.AddCartRequestDto;
import com.bridgex.facades.cart.dto.CartEntryDto;

import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;

/**
 * @author Goncharenko Mikhail, created on 24.07.2018.
 */
@Controller
@RequestMapping("/cart/addcart")
public class AddCartController extends BaseIntegrationController{

  @Resource
  private CartFacade cartFacade;

  private static final String REDIRECT_CART_URL = REDIRECT_PREFIX + "/cart";

  @PostMapping(consumes = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
  public String addOrUpdateCart(@RequestBody AddCartRequestDto request) throws CommerceCartModificationException {
    baseSiteService.setCurrentBaseSite(request.getBaseSiteId(), true);
    baseStoreService.setCurrentBaseStore(request.getBaseStoreId());
    cartFacade.removeSessionCart();
    for (CartEntryDto entry : request.getEntries()) {
      cartFacade.addToCart(entry.getCode(), entry.getQty());
    }
    return REDIRECT_CART_URL;
  }

  @ExceptionHandler(Exception.class)
  @ResponseBody
  public ResponseEntity handleErrors(final Exception ex) {
    return ResponseEntity.badRequest().body(Collections.singletonMap("message",ex.getMessage()));
  }

}
