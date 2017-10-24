package com.bridgex.core.services;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;

/**
 * @author Created by konstantin.pavlyukov@bridge-x.com on 10/19/2017.
 */
public interface PentlandB2BUnitService extends B2BUnitService<B2BUnitModel, B2BCustomerModel> {
  B2BUnitModel getUnitBySapID(String sapID);
}
