package com.bridgex.core.product;

import java.util.List;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.variants.model.VariantProductModel;

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

  /**
   * Convert media during hot folder import
   * @param product
   * @param mediaContainerCode
   * @return
   */
  boolean convertAssignedMedia(ProductModel product, String mediaContainerCode);

  /**
   * Get an attribute value from a product.
   * If the attribute value is null and the product is a variant then the same attribute will be
   * requested from the base product.
   *
   * @param productModel the product
   * @param attribute    the name of the attribute to lookup
   * @return the value of the attribute
   */
  Object getProductAttribute(final ProductModel productModel, final String attribute);

}
