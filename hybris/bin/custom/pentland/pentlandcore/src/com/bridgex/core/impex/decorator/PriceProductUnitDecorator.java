package com.bridgex.core.impex.decorator;

import java.util.Collections;
import java.util.Map;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.impex.jalo.header.AbstractImpExCSVCellDecorator;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

/**
 * @author Created by konstantin.pavlyukov on 10/23/2017.
 */
public class PriceProductUnitDecorator extends AbstractImpExCSVCellDecorator {

  private static final String CATALOG_ID = "pentlandProductCatalog";
  private static final String CATALOG_VERSION_ID = "Online";
  private static final String DEFAULT_UNIT = "EA";

  @Override
  public String decorate(int position, Map<Integer, String> srcLine) {
    String productCode = srcLine.get(position);
    ProductService service = (ProductService)Registry.getApplicationContext().getBean("productService");
    CatalogVersionService versionService = (CatalogVersionService)Registry.getApplicationContext().getBean("catalogVersionService");
    CatalogVersionModel version = versionService.getCatalogVersion(CATALOG_ID, CATALOG_VERSION_ID);
    versionService.setSessionCatalogVersions(Collections.singletonList(version));
    try {
      ProductModel product = service.getProductForCode(version, productCode);
      return product.getUnit().getCode();
    }catch(UnknownIdentifierException e){
      return DEFAULT_UNIT;
    }

  }
}
