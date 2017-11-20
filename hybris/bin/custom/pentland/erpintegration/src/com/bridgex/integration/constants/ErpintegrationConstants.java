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
		public static final String DEFAULT_DOC_TYPE         = "C";
		public static final String DEFAULT_SERVICE_CONSUMER = "Hybris_B2B";
		public static final String DEFAULT_ERP_FLAG_TRUE    = "X";
		public static final String DEFAULT_APPLICATION_KEY  = "V3";
    public static final String DEFAULT_LANGUAGE = "EN";
	}

	public static class RESPONSE {
		public static class ET_RETURN {
			public static final String SUCCESS_TYPE = "S";
			public static final String INFO_TYPE = "I";
			public static final String WARNING_TYPE = "W";
			public static final String ERROR_TYPE = "E";
		}
	}
}
