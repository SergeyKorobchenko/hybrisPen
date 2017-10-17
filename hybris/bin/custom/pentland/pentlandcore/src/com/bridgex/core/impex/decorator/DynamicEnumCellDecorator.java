package com.bridgex.core.impex.decorator;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.impex.jalo.header.AbstractDescriptor;
import de.hybris.platform.impex.jalo.header.AbstractImpExCSVCellDecorator;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/17/2017.
 */
public class DynamicEnumCellDecorator extends AbstractImpExCSVCellDecorator {

  @Override
  public String decorate(int position, Map<Integer, String> srcLine) {
    String initialVal = srcLine.get(position);
    if(StringUtils.isNotEmpty(initialVal)) {
      AbstractDescriptor.DescriptorParams descriptorData = this.getColumnDescriptor().getDescriptorData();
      String type = descriptorData.getModifier("type");
      if(StringUtils.isNotEmpty(type)){
        ModelService modelService = ((ModelService) Registry.getApplicationContext().getBean("modelService"));
        EnumerationService enumerationService = ((EnumerationService) Registry.getApplicationContext().getBean("enumerationService"));
        if(enumerationService != null && modelService != null){
          try {
            enumerationService.getEnumerationValue(type, initialVal);
          }catch(UnknownIdentifierException e){
            EnumerationValueModel model = modelService.create(type);
            model.setCode(initialVal);
            modelService.save(model);
          }
        }
      }
    }
    return initialVal;
  }
}
