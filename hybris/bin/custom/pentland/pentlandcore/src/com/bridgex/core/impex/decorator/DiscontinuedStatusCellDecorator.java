package com.bridgex.core.impex.decorator;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bridgex.core.enums.DiscontinuedStatus;

import de.hybris.platform.impex.jalo.header.AbstractImpExCSVCellDecorator;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/17/2017.
 */
public class DiscontinuedStatusCellDecorator extends AbstractImpExCSVCellDecorator{
  @Override
  public String decorate(int position, Map<Integer, String> srcLine) {
    String sapCode = srcLine.get(position);
    if(StringUtils.isEmpty(sapCode)){
      return DiscontinuedStatus.D03.getCode();
    }
    //already decorated
    if(StringUtils.startsWith(sapCode, "D")){
      return sapCode;
    }
    return "D" + sapCode;
  }
}
