package com.bridgex.core.service.interceptor;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

/**
 * @author Created by konstantin.pavlyukov@masterdata.ru on 10/17/2017.
 */
public class PentlandB2BUnitModelValidateInterceptor implements ValidateInterceptor {

  @Override
  public void onValidate(final Object model, final InterceptorContext ctx) throws InterceptorException
  {
    //empty
  }

}
