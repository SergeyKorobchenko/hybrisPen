package com.bridgex.core.integration;

public interface PentlandOrderDetailsIntegrationService<REQUEST, RESPONSE> {

	 RESPONSE requestDataForOrderDetails(REQUEST requestDto,Integer pageValue);
}
