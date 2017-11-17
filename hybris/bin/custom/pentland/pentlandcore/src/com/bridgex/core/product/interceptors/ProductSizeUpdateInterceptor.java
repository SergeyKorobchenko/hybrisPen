package com.bridgex.core.product.interceptors;

import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.model.ProductSizeModel;
import com.bridgex.core.product.ProductSizeService;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;

/**
 * @author Created by Dmitry Konovalov on 15.11.2017.
 */
public class ProductSizeUpdateInterceptor implements PrepareInterceptor, RemoveInterceptor {

  private ProductSizeService productSizeService;

  @Override
  public void onPrepare(Object o, InterceptorContext interceptorContext) throws InterceptorException {
    evictFromCache(o);
  }

  @Override
  public void onRemove(Object o, InterceptorContext interceptorContext) throws InterceptorException {
    evictFromCache(o);
  }

  private void evictFromCache(final Object o) {
    if (o instanceof ProductSizeModel) {
      getProductSizeService().evictProductSizeFromCache(((ProductSizeModel) o).getCode());
    }
  }

  protected ProductSizeService getProductSizeService() {
    return productSizeService;
  }

  @Required
  public void setProductSizeService(ProductSizeService productSizeService) {
    this.productSizeService = productSizeService;
  }

}
