package com.bridgex.core.address.dao;

import java.util.List;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.user.daos.AddressDao;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/30/2017.
 */
public interface PentlandAddressDao extends AddressDao {

  /**
   * Find all visible shipping addresses for b2bUnit
   * @param b2BUnitModel
   * @return
   */
  List<AddressModel> findDeliveryAddressesForSapId(B2BUnitModel b2BUnitModel);
}
