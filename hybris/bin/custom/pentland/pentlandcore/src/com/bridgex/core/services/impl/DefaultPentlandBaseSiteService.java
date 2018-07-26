package com.bridgex.core.services.impl;

import com.bridgex.core.services.PentlandBaseSiteService;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.site.impl.DefaultBaseSiteService;

public class DefaultPentlandBaseSiteService extends DefaultBaseSiteService implements PentlandBaseSiteService {

    @Override
    public boolean isB2BChannel() {
        return SiteChannel.B2B.equals(getCurrentBaseSite().getChannel());
    }
}
