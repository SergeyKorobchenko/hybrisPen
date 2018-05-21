package com.bridgex.facades.customer;

import de.hybris.platform.commercefacades.user.data.RegisterData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;

public interface PentlandCustomerRegFacade {
	 public void saveFormData(RegisterData data) throws DuplicateUidException;
	} 
