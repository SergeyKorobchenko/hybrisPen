package com.bridgex.core.product.util;

import java.util.Comparator;

import com.bridgex.core.model.ApparelSizeVariantProductModel;

/**
 * @author Created by dmitry.konovalov@masterdata.ru on 13.11.2017.
 */
public class ProductSizeComparator implements Comparator<ApparelSizeVariantProductModel> {

  @Override
  public int compare(ApparelSizeVariantProductModel o1, ApparelSizeVariantProductModel o2) {
    if (o1.getProductSize() != null && o2.getProductSize() != null) {
       if (o2.getProductSize().getPriority() > o1.getProductSize().getPriority()) {
         return 1;
       } else if (o2.getProductSize().getPriority() < o1.getProductSize().getPriority()) {
         return -1;
       }
    }
    return 0;
  }
}
