package com.bridgex.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.spring.security.CoreUserDetails;
import de.hybris.platform.spring.security.CoreUserDetailsService;
import de.hybris.platform.util.Config;

/**
 * @author Goncharenko Mikhail, created on 30.07.2018.
 */

public class SiteBasedUserDetailsService extends CoreUserDetailsService {

  private UserService     siteBasedUserService;
  private String rolePrefix = "ROLE_";

  @Override
  public CoreUserDetails loadUserByUsername(String username) {
    if (username == null) {
      return null;
    } else {
      UserModel user;
      try {
        user = siteBasedUserService.getUserForUID(username);
      } catch (ModelNotFoundException var6) {
        throw new UsernameNotFoundException("User not found!");
      }
      boolean enabled = !user.isLoginDisabled() && isNotAnonymousOrAnonymousLoginIsAllowed(user);
      String password = user.getEncodedPassword();
      if (password == null) {
        password = "";
      }
      return new CoreUserDetails(user.getUid(), password, enabled,
                                                        true, true, true,
                                                        this.getAuthorities(user),
                                                        JaloSession.getCurrentSession().getSessionContext().getLanguage().getIsoCode());
    }
  }

  private boolean isNotAnonymousOrAnonymousLoginIsAllowed(UserModel user) {
    return !siteBasedUserService.isAnonymousUser(user) || !Config.getBoolean("login.anonymous.always.disabled", true);
  }

  private Collection<GrantedAuthority> getAuthorities(UserModel user) {
    Set<PrincipalGroupModel> groups = user.getAllGroups();
    return groups.stream()
          .map(group -> new SimpleGrantedAuthority(rolePrefix + group.getUid().toUpperCase()))
          .collect(Collectors.toList());
  }

  @Required
  public void setUserService(UserService userService) {
    this.siteBasedUserService = userService;
  }

}
