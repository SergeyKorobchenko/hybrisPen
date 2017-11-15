package com.bridgex.core.product.util;

import java.util.Comparator;

import com.bridgex.core.model.ApparelSizeVariantProductModel;
import com.bridgex.core.model.ProductSizeModel;
import com.bridgex.core.product.ProductSizeService;

import de.hybris.platform.core.Registry;

/**
 * @author Created by dmitry.konovalov on 13.11.2017.
 */
public class ProductSizeComparator implements Comparator<ApparelSizeVariantProductModel> {

  private ProductSizeService productSizeService;

  public ProductSizeComparator() {
    this.productSizeService = (ProductSizeService) Registry.getApplicationContext().getBean("productSizeService");;
  }

  @Override
  public int compare(ApparelSizeVariantProductModel o1, ApparelSizeVariantProductModel o2) {
    ProductSizeModel o1Size = productSizeService.getProductSize(o1.getSize());
    ProductSizeModel o2Size = productSizeService.getProductSize(o2.getSize());

    if (o1Size != null && o2Size != null) {
       if (o2Size.getPriority() > o1Size.getPriority()) {
         return 1;
       } else if (o2Size.getPriority() < o1Size.getPriority()) {
         return -1;
       }
    }
    return 0;
  }
}
