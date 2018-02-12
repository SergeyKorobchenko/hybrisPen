package com.bridgex.facades.order.converters.populator;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.bridgex.core.category.PentlandCategoryService;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.order.converters.populator.OrderHistoryPopulator;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

/**
 * Created by dmitry.konovalov@masterdata.ru on 18.10.2017.
 */
public class PentlandOrderHistoryPopulator extends OrderHistoryPopulator {

  private static final Logger LOG = Logger.getLogger(PentlandOrderHistoryPopulator.class);

  private PentlandCategoryService categoryService;

  @Override
  public void populate(final OrderModel source, final OrderHistoryData target)
  {
    super.populate(source, target);
    if(source.getOrderType() != null) {
      target.setOrderType(getEnumerationService().getEnumerationName(source.getOrderType()));
    }
    target.setRdd(source.getRdd());
    target.setPurchaseOrderNumber(source.getPurchaseOrderNumber());
    target.setTotalQty(source.getTotalQty());
    if(StringUtils.isNotEmpty(source.getSapBrand())) {
      try {
        CategoryModel brandCategory = categoryService.getCategoryForCode(source.getSapBrand());
        target.setBrand(brandCategory.getName());
      }catch(ModelNotFoundException | AmbiguousIdentifierException e){
        LOG.debug("Error while retrieving brand category for order: " + e);
      }
    }
  }

  @Required
  public void setCategoryService(PentlandCategoryService categoryService) {
    this.categoryService = categoryService;
  }
}
