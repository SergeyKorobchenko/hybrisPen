package com.bridgex.core.product.util;

import java.util.Comparator;

import com.bridgex.core.model.ApparelSizeVariantProductModel;

/**
 * @author Created by dmitry.konovalov on 13.11.2017.
 */
public class ProductSizeComparator implements Comparator<ApparelSizeVariantProductModel> {

  @Override
  public int compare(final ApparelSizeVariantProductModel o1, final ApparelSizeVariantProductModel o2) {
//    final ProductSizeModel o1Size = productSizeService.getProductSize(o1.getSize());
//    final ProductSizeModel o2Size = productSizeService.getProductSize(o2.getSize());
//
//    return Integer.compare(Optional.ofNullable(o1Size).orElse(new ProductSizeModel()).getPriority(), Optional.ofNullable(o2Size).orElse(new ProductSizeModel()).getPriority());

    return Integer.compare(o1.getSizeNo(), o2.getSizeNo());
  }
}
