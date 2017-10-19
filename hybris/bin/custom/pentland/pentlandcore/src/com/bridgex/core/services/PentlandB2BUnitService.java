package com.bridgex.core.services;

import de.hybris.platform.b2b.model.B2BUnitModel;

/**
 * @author Created by konstantin.pavlyukov@masterdata.ru on 10/19/2017.
 */
public interface PentlandB2BUnitService {
  B2BUnitModel getUnitBySapID(String sapID);
}
