package com.bridgex.core.customer.impex.decorator;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import de.hybris.platform.impex.jalo.header.AbstractImpExCSVCellDecorator;

/**
 * Created by dmitry.konovalov@masterdata.ru on 24.10.2017.
 */
public class UserNameDecorator extends AbstractImpExCSVCellDecorator {

  private static final String NAME_SEPARATOR = " ";

  @Override
  public String decorate(int position, Map<Integer, String> srcLine) {
    String firstName = srcLine.get(position);
    int nextValuePosition = position + 1;
    String lastName = srcLine.containsKey(nextValuePosition) ? srcLine.get(nextValuePosition) : StringUtils.EMPTY;
    return firstName + NAME_SEPARATOR + lastName;
  }

}
