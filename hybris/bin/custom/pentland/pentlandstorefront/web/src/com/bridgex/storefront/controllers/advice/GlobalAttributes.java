package com.bridgex.storefront.controllers.advice;

import javax.annotation.Resource;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.bridgex.core.constants.PentlandcoreConstants;

import de.hybris.platform.servicelayer.session.SessionService;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/24/2017.
 */
@ControllerAdvice
public class GlobalAttributes {

  public static final String HIDE_PRICES_ATTRIBUTE = PentlandcoreConstants.SessionParameters.HIDE_PRICES_FOR_USER;
  @Resource
  private SessionService sessionService;

  @ModelAttribute(HIDE_PRICES_ATTRIBUTE)
  public boolean hidePrices(){
    Boolean hidePrices = sessionService.getAttribute(HIDE_PRICES_ATTRIBUTE);
    return BooleanUtils.isTrue(hidePrices);
  }
}
