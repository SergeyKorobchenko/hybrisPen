package com.bridgex.core.product.impl;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import com.bridgex.core.model.ProductSizeModel;
import com.bridgex.core.product.ProductSizeService;
import com.bridgex.core.product.dao.ProductSizeDao;

/**
 * @author Created by Dmitry Konovalov on 15.11.2017.
 */
public class DefaultProductSizeService implements ProductSizeService {

  private ProductSizeDao productSizeDao;

  private static final String PRODUCT_SIZE_CACHE = "PRODUCT_SIZES";

  @Override
  @Cacheable(value=PRODUCT_SIZE_CACHE, key="#code")
  public ProductSizeModel getProductSize(final String code) {
    return getProductSizeDao().findByCode(code);
  }

  @Override
  @CacheEvict(value=PRODUCT_SIZE_CACHE, key="#code")
  public void evictProductSizeFromCache(final String code) {

  }

  protected ProductSizeDao getProductSizeDao() {
    return productSizeDao;
  }

  @Required
  public void setProductSizeDao(ProductSizeDao productSizeDao) {
    this.productSizeDao = productSizeDao;
  }

}
