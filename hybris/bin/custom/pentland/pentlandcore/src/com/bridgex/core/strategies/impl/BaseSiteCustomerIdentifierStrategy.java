package com.bridgex.core.strategies.impl;

import javax.annotation.Resource;

import com.bridgex.core.strategies.CustomerIdentifierStrategy;

import de.hybris.platform.site.BaseSiteService;

/**
 * @author Goncharenko Mikhail, created on 27.07.2018.
 */
public class BaseSiteCustomerIdentifierStrategy implements CustomerIdentifierStrategy {

  @Resource
  private BaseSiteService baseSiteService;

  @Override
  public String createUidFromLogin(String login) {
    String prefix = "[" + baseSiteService.getCurrentBaseSite().getUid() + "]";
    return prefix + login;
  }

}
