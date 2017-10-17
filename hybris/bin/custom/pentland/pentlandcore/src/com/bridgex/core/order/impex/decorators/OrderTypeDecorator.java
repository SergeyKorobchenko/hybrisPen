package com.bridgex.core.order.impex.decorators;

import java.util.List;
import java.util.Map;

import org.fest.util.Collections;

import com.bridgex.core.enums.OrderType;

import de.hybris.platform.core.Registry;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.CSVCellDecorator;

/**
 * Created by dmitry.konovalov@masterdata.ru on 17.10.2017.
 */
public class OrderTypeDecorator implements CSVCellDecorator {

  @Override
  public String decorate(int position, Map<Integer, String> srcLine) {
    String parsedValue = srcLine.get(position);
    List<OrderType> existingValues = ((EnumerationService) Registry.getApplicationContext().getBean("enumerationService")).getEnumerationValues(OrderType.class);
    if (Collections.isEmpty(existingValues) || !existingValues.contains(OrderType.valueOf(parsedValue))) {
      addValue(parsedValue);
    }
    return parsedValue;
  }

  protected void addValue(String value) {
    ModelService modelService = ((ModelService) Registry.getApplicationContext().getBean("modelService"));
    OrderType type = OrderType.valueOf(value);
    modelService.save(type);
  }

}
