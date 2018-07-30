package com.bridgex.security;

import java.util.Collections;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import de.hybris.platform.acceleratorstorefrontcommons.security.AbstractAcceleratorAuthenticationProvider;
import de.hybris.platform.core.Constants;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.jalo.user.LoginToken;
import de.hybris.platform.servicelayer.user.impl.DefaulPasswordEncoderService;
import de.hybris.platform.spring.security.CoreUserDetails;

/**
 * @author Goncharenko Mikhail, created on 30.07.2018.
 */
abstract public class AbstractPentlandAuthenticationProvider extends AbstractAcceleratorAuthenticationProvider {

  private static final Logger LOG = Logger.getLogger(AbstractAcceleratorAuthenticationProvider.class);

  private DefaulPasswordEncoderService passwordEncoderService;
  private final UserDetailsChecker postAuthenticationChecks = new AbstractPentlandAuthenticationProvider.DefaultPostAuthenticationChecks();

  public static final String CORE_AUTHENTICATION_PROVIDER_BAD_CREDENTIALS = "CoreAuthenticationProvider.badCredentials";
  public static final String BAD_CREDENTIALS                              = "Bad credentials";

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    if (Registry.hasCurrentTenant() && JaloConnection.getInstance().isSystemInitialized()) {
      final String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
      final boolean isBruteForceAttack = getBruteForceAttackCounter().isAttack(username);
      UserDetails userDetails = null;

      try {
        //will returns with uid
        userDetails = this.retrieveUser(username);
      }
      catch (UsernameNotFoundException e) {
        if (isBruteForceAttack) {
          LOG.warn("Brute force attack attempt for non existing user name " + username);
        }
        throw new BadCredentialsException(messages.getMessage(CORE_AUTHENTICATION_PROVIDER_BAD_CREDENTIALS, BAD_CREDENTIALS), e);
      }

      this.getPreAuthenticationChecks().check(userDetails);
      //
      UserModel user = getUserService().getUserForUID(userDetails.getUsername());

      if (isBruteForceAttack) {
        user.setLoginDisabled(true);
        getModelService().save(user);
        getBruteForceAttackCounter().resetUserCounter(user.getUid());
        throw new BadCredentialsException(messages.getMessage(CORE_AUTHENTICATION_PROVIDER_BAD_CREDENTIALS, BAD_CREDENTIALS));
      }

      if (!getUserService().isMemberOfGroup(user, getUserService().getUserGroupForUID(Constants.USER.CUSTOMER_USERGROUP))) {
        throw new BadCredentialsException(messages.getMessage(CORE_AUTHENTICATION_PROVIDER_BAD_CREDENTIALS, BAD_CREDENTIALS));
      }

      Object credential = authentication.getCredentials();
      checkPassword(user, credential);
      additionalAuthenticationChecks(userDetails, (AbstractAuthenticationToken) authentication);
      postAuthenticationChecks.check(userDetails);
      getUserService().setCurrentUser(user);
      return this.createSuccessAuthentication(authentication, userDetails);
    }
    else {
      return this.createSuccessAuthentication(authentication,
                                              new CoreUserDetails("systemNotInitialized", "systemNotInitialized", true, false, true, true, Collections.EMPTY_LIST, (String) null));
    }
  }

  private void checkPassword(UserModel user, Object credential) {
    if (credential instanceof String) {
      if (!passwordEncoderService.isValid(user, (String) credential)) {
        throw new BadCredentialsException(this.messages.getMessage("CoreAuthenticationProvider.badCredentials", "Bad credentials"));
      }
    }
    else if (!(credential instanceof LoginToken) ||
            passwordEncoderService.isValid(user, user.getPasswordEncoding(), ((LoginToken) credential).getPassword())) {
        throw new BadCredentialsException(this.messages.getMessage("CoreAuthenticationProvider.badCredentials", "Bad credentials"));
    }
  }

  @Required
  public void setPasswordEncoderService(DefaulPasswordEncoderService passwordEncoderService) {
    this.passwordEncoderService = passwordEncoderService;
  }

  private class DefaultPostAuthenticationChecks implements UserDetailsChecker {
    private DefaultPostAuthenticationChecks() {
    }

    public void check(UserDetails user) {
      if (!user.isCredentialsNonExpired()) {
        throw new CredentialsExpiredException(AbstractPentlandAuthenticationProvider.this.messages.getMessage("CoreAuthenticationProvider.credentialsExpired",
                                                                                                              "User credentials have expired"));
      }
    }
  }

}