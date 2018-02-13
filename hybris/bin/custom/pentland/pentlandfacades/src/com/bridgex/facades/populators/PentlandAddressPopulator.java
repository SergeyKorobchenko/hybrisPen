package com.bridgex.facades.populators;

import de.hybris.platform.commercefacades.user.converters.populator.AddressPopulator;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.user.AddressModel;

/**
 * @author Created by ekaterina.agievich@bridge-x.com on 11/15/2017.
 */
public class PentlandAddressPopulator extends AddressPopulator{

  @Override
  public void populate(final AddressModel source, final AddressData target) {
    super.populate(source, target);
    target.setSapId(source.getAddressID());
    target.setDisplayName(source.getDisplayName());
  }
}
