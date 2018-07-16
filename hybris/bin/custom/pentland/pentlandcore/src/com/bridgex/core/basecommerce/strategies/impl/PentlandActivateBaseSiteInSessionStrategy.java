package com.bridgex.core.basecommerce.strategies.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.basecommerce.strategies.impl.DefaultActivateBaseSiteInSessionStrategy;
import de.hybris.platform.europe1.constants.Europe1Constants;
import de.hybris.platform.servicelayer.session.SessionService;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;

public class PentlandActivateBaseSiteInSessionStrategy extends DefaultActivateBaseSiteInSessionStrategy {

    protected SessionService sessionService;

    public SessionService getSessionService() {
        return sessionService;
    }

    @Required
    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public void activate(BaseSiteModel site) {
        super.activate(site);
        if (site.getDefaultUpg() != null) {
            getSessionService().setAttribute(Europe1Constants.PARAMS.UPG,
                    Collections.singletonList(site.getDefaultUpg()));
        }
    }
}
