package com.bridgex.core.impex.decorator;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import de.hybris.platform.impex.jalo.header.AbstractDescriptor;
import de.hybris.platform.impex.jalo.header.AbstractImpExCSVCellDecorator;

public class PriceGroupDynamicEnumCellDecorator extends AbstractImpExCSVCellDecorator{

	@Override
	public String decorate(int position, Map<Integer, String> srcLine) {
		// TODO Auto-generated method stub
		String initialVal = srcLine.get(position);
		if(StringUtils.isNotEmpty(initialVal)) {
			AbstractDescriptor.DescriptorParams descriptorData = this.getColumnDescriptor().getDescriptorData();
			String type = descriptorData.getModifier("groups");
			String brandCode = srcLine.get(8);
			if(StringUtils.isNotEmpty(brandCode)){
				if(position==2 &&brandCode.contains("08"))
				{
					initialVal="8A";
				}
			}
		}
		return initialVal;
	}
}
