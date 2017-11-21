package com.bridgex.facades.order.converters.populator;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.category.PentlandCategoryService;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.order.converters.populator.OrderPopulator;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/21/2017.
 */
public class PentlandOrderPopulator extends OrderPopulator {

  private static final Logger LOG = Logger.getLogger(PentlandOrderPopulator.class);

  private PentlandCategoryService categoryService;

  @Override
  public void populate(final OrderModel source, final OrderData target) {
    super.populate(source, target);

    target.setOrderType(source.getOrderType());
    target.setRdd(source.getRdd());
    target.setTotalUnitCount(source.getTotalQty());

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
}
