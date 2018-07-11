package com.bridgex.core.impex.decorator;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import de.hybris.platform.impex.jalo.header.AbstractDescriptor;
import de.hybris.platform.impex.jalo.header.AbstractImpExCSVCellDecorator;

public class B2BUnitDynamicEnumCellDecorator extends AbstractImpExCSVCellDecorator {

	@Override
	public String decorate(int position, Map<Integer, String> srcLine) {
		// TODO Auto-generated method stub
		String initialVal = srcLine.get(position);
		if(StringUtils.isNotEmpty(initialVal)) {
			AbstractDescriptor.DescriptorParams descriptorData = this.getColumnDescriptor().getDescriptorData();
			String type = descriptorData.getModifier("groups");
			String brandCode = srcLine.get(5);
			if(StringUtils.isNotEmpty(brandCode)){
				if(position==1 &&brandCode.contains("08")&&initialVal.contains("8A"))
				{
					initialVal="8A";
				}
				else if(position==9 &&brandCode.contains("08")&&initialVal.contains("8A"))
				{
					initialVal="8A";
				}
				else if(position==10 &&brandCode.contains("08")&&initialVal.contains("8A"))
				{
					initialVal="8A";
				}
			}
		}
		return initialVal;
	}
}
