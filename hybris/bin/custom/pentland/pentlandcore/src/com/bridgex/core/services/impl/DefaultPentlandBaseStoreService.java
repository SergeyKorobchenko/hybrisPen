package com.bridgex.core.services.impl;

import javax.annotation.Resource;

import com.bridgex.core.services.PentlandBaseStoreService;

import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.impl.DefaultBaseStoreService;

/**
 * @author Goncharenko Mikhail, created on 23.07.2018.
 */
public class DefaultPentlandBaseStoreService extends DefaultBaseStoreService implements PentlandBaseStoreService {

  private static final String BASE_STORE_ATTRIBUTE_KEY = "currentBaseStore";

  private SessionService sessionService;

  @Override
  public void setCurrentBaseStore(BaseStoreModel baseStore) {
    sessionService.setAttribute(BASE_STORE_ATTRIBUTE_KEY, baseStore);
  }

  @Override
  public void setCurrentBaseStore(String baseStoreId) {
    sessionService.setAttribute(BASE_STORE_ATTRIBUTE_KEY, getBaseStoreForUid(baseStoreId));
  }

  @Resource(name = "sessionService")
  public void setSessionService(SessionService sessionService) {
    this.sessionService = sessionService;
  }

  @Override
  public BaseStoreModel getCurrentBaseStore() {
    BaseStoreModel sessionStore = sessionService.getAttribute(BASE_STORE_ATTRIBUTE_KEY);
    if (sessionStore == null) {
      sessionStore = super.getCurrentBaseStore();
    }
    return sessionStore;
  }
}
