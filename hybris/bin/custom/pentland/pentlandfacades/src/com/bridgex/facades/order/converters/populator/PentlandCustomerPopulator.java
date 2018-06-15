package com.bridgex.facades.order.converters.populator;

import de.hybris.platform.commercefacades.user.converters.populator.CustomerPopulator;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.user.CustomerModel;

public class PentlandCustomerPopulator extends CustomerPopulator
{
	@Override
	public void populate(CustomerModel source, CustomerData target) 
	{
		if (source.getAccessRequired() != null)
		{
			target.setAccessRequired(source.getAccessRequired());
		}
		if (source.getAccountNumber() != null)
		{
			target.setAccountNumber(source.getAccountNumber());
		}

		if (source.getCompanyName() != null)
		{
			target.setCompanyName(source.getCompanyName());
		}
		if (source.getUserEmail() != null)
		{
			target.setUserEmail(source.getUserEmail());
		}
		if (source.getPosition() != null)
		{
			target.setPosition(source.getPosition());
		}
		super.populate(source, target);
	}
}
