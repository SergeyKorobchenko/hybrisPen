package com.bridgex.core.strategies;

/**
 * @author Goncharenko Mikhail, created on 27.07.2018.
 */
public interface CustomerIdentifierStrategy {

  String createUidFromLogin(String login);

}
