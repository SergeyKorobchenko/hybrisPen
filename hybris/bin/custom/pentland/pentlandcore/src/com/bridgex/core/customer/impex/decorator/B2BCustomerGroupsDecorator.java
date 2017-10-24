package com.bridgex.core.customer.impex.decorator;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import de.hybris.platform.impex.jalo.header.AbstractImpExCSVCellDecorator;

/**
 * Created by dmitry.konovalov@masterdata.ru on 24.10.2017.
 */
public class B2BCustomerGroupsDecorator extends AbstractImpExCSVCellDecorator {

  private static final int SOLD_TO_POSITION = 4;
  private static final String GROUPS_DELIMITER = ",";

  @Override
  public String decorate(int position, Map<Integer, String> srcLine) {
    String groupsVal = srcLine.get(position);
    String soldToVal = srcLine.get(SOLD_TO_POSITION);
    return StringUtils.isNotEmpty(soldToVal) ? groupsVal + GROUPS_DELIMITER + soldToVal : groupsVal;
  }

}
