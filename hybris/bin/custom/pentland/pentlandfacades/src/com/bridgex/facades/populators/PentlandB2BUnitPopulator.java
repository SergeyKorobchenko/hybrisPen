package com.bridgex.facades.populators;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2bcommercefacades.company.converters.populators.B2BUnitPopulator;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 10/27/2017.
 */
public class PentlandB2BUnitPopulator extends B2BUnitPopulator {

  @Override
  public void populate(final B2BUnitModel source, final B2BUnitData target)
  {
    super.populate(source, target);
    target.setCustomerType(source.getCustomerType());
  }

}
