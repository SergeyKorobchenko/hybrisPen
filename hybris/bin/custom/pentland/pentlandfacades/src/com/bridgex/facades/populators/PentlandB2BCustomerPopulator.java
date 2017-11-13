package com.bridgex.facades.populators;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2bcommercefacades.company.converters.populators.B2BCustomerPopulator;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.commercefacades.user.data.CustomerData;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/27/2017.
 */
public class PentlandB2BCustomerPopulator extends B2BCustomerPopulator {

  @Override
  protected void populateUnit(final B2BCustomerModel customer, final CustomerData target)
  {
    // minimal properties are populated, as require by customer paginated page.
    final B2BUnitModel parent = getB2bUnitService().getParent(customer);
    if (parent != null)
    {
      final B2BUnitData b2BUnitData = new B2BUnitData();
      b2BUnitData.setUid(parent.getUid());
      b2BUnitData.setName(parent.getLocName());
      b2BUnitData.setActive(Boolean.TRUE.equals(parent.getActive()));
      b2BUnitData.setCustomerType(parent.getCustomerType());

      target.setUnit(b2BUnitData);
    }
  }
}
