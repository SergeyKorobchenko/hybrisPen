package com.bridgex.core.product;

import com.bridgex.core.model.ProductSizeModel;

/**
 * @author Created by Dmitry Konovalov on 15.11.2017.
 */
public interface ProductSizeService {

  ProductSizeModel getProductSize(String code);

  void evictProductSizeFromCache(String code);

}
