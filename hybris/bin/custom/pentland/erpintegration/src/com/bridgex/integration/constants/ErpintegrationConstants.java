/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.bridgex.integration.constants;

/**
 * Global class for all Erpintegration constants. You can add global constants for your extension into this class.
 */
public final class ErpintegrationConstants extends GeneratedErpintegrationConstants
{
	public static final String EXTENSIONNAME = "erpintegration";

	private ErpintegrationConstants()
	{
		//empty to avoid instantiating this constant class
	}

	// implement here constants used by this extension

  public static final String PLATFORM_LOGO_CODE = "erpintegrationPlatformLogo";

	public static final String ERP_USERNAME = "erp.integration.username";

	public static final String ERP_PASSWORD = "erp.integration.password";

	public static class REQUEST {
		public static final String DEFAULT_DOC_TYPE = "C";
		public static final String DEFAULT_SERVICE_CONSUMER = "Hybris_B2B";

	}
}
