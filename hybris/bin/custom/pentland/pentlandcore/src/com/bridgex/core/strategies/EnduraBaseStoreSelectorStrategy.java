package com.bridgex.core.strategies;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.basecommerce.strategies.BaseStoreSelectorStrategy;
import de.hybris.platform.basecommerce.strategies.impl.DefaultBaseStoreSelectorStrategy;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;

/**
 * @author Goncharenko Mikhail, created on 23.07.2018.
 */
public class EnduraBaseStoreSelectorStrategy extends DefaultBaseStoreSelectorStrategy {

  @Resource(name = "baseSiteService")
  private BaseSiteService siteService;

  @Required
  public void setBaseSiteService(BaseSiteService siteService) {
    this.siteService = siteService;
  }

  @Override
  public BaseStoreModel getCurrentBaseStore() {
    BaseStoreModel result = null;
    BaseSiteModel currentSite = siteService.getCurrentBaseSite();
    if (currentSite != null) {
      List<BaseStoreModel> storeModels = currentSite.getStores();
      if (CollectionUtils.isNotEmpty(storeModels)) {
        result = storeModels.stream()
                            .filter(BaseStoreModel::getDefault)
                            .findFirst()
                            .orElse(storeModels.get(0));
      }
    }
    return result;
  }

}
