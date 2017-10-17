package com.bridgex.core.product.dao;

import java.util.List;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.daos.ProductDao;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/17/2017.
 */
public interface PentlandProductDao extends ProductDao{

  /**
   * Find all products for sapBrand with specific flag
   * @param sapBrand
   * @param catalogVersion
   * @param flagField
   * @param flagValue
   * @return
   */
  List<ProductModel> findProductsForSapBrandAndCatalogVersion(String sapBrand, CatalogVersionModel catalogVersion, String flagField, Boolean flagValue);

}
