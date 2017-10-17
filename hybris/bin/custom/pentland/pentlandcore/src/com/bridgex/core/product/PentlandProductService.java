package com.bridgex.core.product;

import java.util.List;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/17/2017.
 */
public interface PentlandProductService extends ProductService{

  /**
   * Find all SMU products in catalog version with specified sapBrand
   * @param sapBrand
   * @param catalogVersion
   * @return
   */
  List<ProductModel> findSMUProductsForSapBrandAndCatalogVersion(String sapBrand, CatalogVersionModel catalogVersion);

  /**
   * Find all Clearance products in catalog version with specified sapBrand
   * @param sapBrand
   * @param catalogVersion
   * @return
   */
  List<ProductModel> findClearanceProductsForSapBrandAndCatalogVersion(String sapBrand, CatalogVersionModel catalogVersion);
}
