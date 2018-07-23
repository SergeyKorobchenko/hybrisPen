package com.bridgex.core.services;

import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

/**
 * @author Goncharenko Mikhail, created on 23.07.2018.
 */
public interface PentlandBaseStoreService extends BaseStoreService {

  void setCurrentBaseStore(BaseStoreModel baseStore);

  void setCurrentBaseStore(String baseStoreId);

}
