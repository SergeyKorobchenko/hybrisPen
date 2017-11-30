package com.bridgex.core.product.util;

import java.util.Comparator;
import java.util.Optional;

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
  public int compare(final ApparelSizeVariantProductModel o1, final ApparelSizeVariantProductModel o2) {
    final ProductSizeModel o1Size = productSizeService.getProductSize(o1.getSize());
    final ProductSizeModel o2Size = productSizeService.getProductSize(o2.getSize());

    return Integer.compare(Optional.ofNullable(o1Size).orElse(new ProductSizeModel()).getPriority(), Optional.ofNullable(o2Size).orElse(new ProductSizeModel()).getPriority());
  }
}
