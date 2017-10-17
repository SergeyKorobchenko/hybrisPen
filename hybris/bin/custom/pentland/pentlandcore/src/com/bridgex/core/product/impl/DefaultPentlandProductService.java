package com.bridgex.core.product.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.product.PentlandProductService;
import com.bridgex.core.product.dao.PentlandProductDao;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.impl.DefaultProductService;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/17/2017.
 */
public class DefaultPentlandProductService extends DefaultProductService implements PentlandProductService{

  private PentlandProductDao pentlandProductDao;

  @Override
  public List<ProductModel> findSMUProductsForSapBrandAndCatalogVersion(String sapBrand, CatalogVersionModel catalogVersion) {
    return pentlandProductDao.findProductsForSapBrandAndCatalogVersion(sapBrand, catalogVersion, ProductModel.SMU, true);
  }

  @Override
  public List<ProductModel> findClearanceProductsForSapBrandAndCatalogVersion(String sapBrand, CatalogVersionModel catalogVersion) {
    return pentlandProductDao.findProductsForSapBrandAndCatalogVersion(sapBrand, catalogVersion, ProductModel.CLEARANCE, true);
  }

  @Required
  public void setPentlandProductDao(PentlandProductDao pentlandProductDao) {
    this.pentlandProductDao = pentlandProductDao;
  }
}
