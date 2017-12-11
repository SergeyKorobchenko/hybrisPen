package com.bridgex.core.product.util;

import java.util.Comparator;
import java.util.Optional;

import com.bridgex.core.model.ApparelSizeVariantProductModel;

/**
 * @author Created by dmitry.konovalov on 13.11.2017.
 */
public class ProductSizeComparator implements Comparator<ApparelSizeVariantProductModel> {

  @Override
  public int compare(final ApparelSizeVariantProductModel o1, final ApparelSizeVariantProductModel o2) {

    return Integer.compare(Optional.ofNullable(o1.getSizeNo()).orElse(Integer.MAX_VALUE), Optional.ofNullable(o2.getSizeNo()).orElse(Integer.MAX_VALUE));
  }
}
