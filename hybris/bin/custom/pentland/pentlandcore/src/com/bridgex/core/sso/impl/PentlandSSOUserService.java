package com.bridgex.core.sso.impl;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.samlsinglesignon.DefaultSSOService;
import java.util.Collection;
import org.apache.commons.collections.CollectionUtils;

/**
 * Workaround for missing usergroup field. Could be removed if we get the data we need
 * @author Created by ekaterina.agievich@bridgex.com on 10/11/2017.
 */
public class PentlandSSOUserService extends DefaultSSOService {
  @Override
  public UserModel getOrCreateSSOUser(final String id, final String name, final Collection<String> roles)
  {
    if (CollectionUtils.isEmpty(roles))
    {
      roles.add("asagent");
    }
    return super.getOrCreateSSOUser(id, name, roles);
  }
}
