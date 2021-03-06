package com.bridgex.facades.order.converters.populator;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.category.PentlandCategoryService;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.order.converters.populator.OrderPopulator;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/21/2017.
 */
public class PentlandOrderPopulator extends OrderPopulator {

  private static final Logger LOG = Logger.getLogger(PentlandOrderPopulator.class);

  private PentlandCategoryService categoryService;
  private EnumerationService      enumerationService;

  @Override
  public void populate(final OrderModel source, final OrderData target) {
    super.populate(source, target);

    if(source.getOrderType() != null) {
      target.setOrderType(enumerationService.getEnumerationName(source.getOrderType()));
    }
    target.setRdd(source.getRdd());
    if(source.getTotalQty() != null) {
      target.setTotalUnitCount(source.getTotalQty());
    }
    target.setCreditCheckPassed(source.isCreditCheckPassed());
    target.setPurchaseOrderNumber(source.getPurchaseOrderNumber());

    populateSapBrand(source, target);
  }

  private void populateSapBrand(OrderModel source, OrderData target) {
    String sapBrand = source.getSapBrand();
    if(StringUtils.isNotEmpty(sapBrand)){
      try {
        CategoryModel brandCategory = categoryService.getCategoryForCode(sapBrand);
        target.setBrand(brandCategory.getName());
      }catch(UnknownIdentifierException | AmbiguousIdentifierException e){
        LOG.debug("Failed to identify brand for code " + sapBrand);
      }
    }
  }

  @Required
  public void setCategoryService(PentlandCategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @Required
  public void setEnumerationService(EnumerationService enumerationService) {
    this.enumerationService = enumerationService;
  }
}
