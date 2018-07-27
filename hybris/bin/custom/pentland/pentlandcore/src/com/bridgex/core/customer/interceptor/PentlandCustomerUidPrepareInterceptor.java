package com.bridgex.core.customer.interceptor;

import org.apache.commons.lang.StringUtils;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;

/**
 * @author Goncharenko Mikhail, created on 27.07.2018.
 */
public class PentlandCustomerUidPrepareInterceptor implements PrepareInterceptor<CustomerModel> {

  @Override
  public void onPrepare(CustomerModel customer, InterceptorContext ctx) throws InterceptorException {

    if (ctx.isNew(customer)) {
      adjustIds(customer);
    }

  }

  private void adjustIds(CustomerModel customer) {
    final String original = customer.getOriginalUid();
    final String uid = customer.getUid();
    if (StringUtils.isNotEmpty(uid))
    {
      customer.setUid(uid.toLowerCase());
    }
    if (StringUtils.isNotEmpty(original))
    {
      customer.setOriginalUid(original.toLowerCase());
    }
  }

}
