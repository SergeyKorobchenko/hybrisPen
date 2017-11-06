package com.bridgex.core.services;

import java.util.Collection;
import java.util.List;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.core.model.user.AddressModel;

/**
 * @author Created by konstantin.pavlyukov@bridge-x.com on 10/19/2017.
 */
public interface PentlandB2BUnitService extends B2BUnitService<B2BUnitModel, B2BCustomerModel> {

  B2BUnitModel getUnitBySapID(String sapID);

  /**
   * Find all delivery addresses for B2BUnit
   * @param b2bUnit
   * @return
   */
  List<AddressModel> findDeliveryAddressesForUnits(B2BUnitModel b2bUnit);

  /**
   * Gets all parent units of a user.
   *
   * @param employee
   *           the employee
   * @return the parent
   */
  Collection<B2BUnitModel> getFirstLevelParents(final B2BCustomerModel employee);
}
