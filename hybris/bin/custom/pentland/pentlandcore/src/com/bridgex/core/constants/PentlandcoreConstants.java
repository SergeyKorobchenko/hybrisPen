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
package com.bridgex.core.constants;

/**
 * Global class for all Pentlandcore constants. You can add global constants for your extension into this class.
 */
public final class PentlandcoreConstants extends GeneratedPentlandcoreConstants
{
	public static final String EXTENSIONNAME    = "pentlandcore";

	private PentlandcoreConstants()
	{
		//empty
	}

	public interface SessionParameters {
		String HIDE_PRICES_FOR_USER = "hidePricesForUser";
	}

	// implement here constants used by this extension
	public static final String QUOTE_BUYER_PROCESS = "quote-buyer-process";
	public static final String QUOTE_SALES_REP_PROCESS = "quote-salesrep-process";
	public static final String QUOTE_USER_TYPE = "QUOTE_USER_TYPE";
	public static final String QUOTE_SELLER_APPROVER_PROCESS = "quote-seller-approval-process";
	public static final String QUOTE_TO_EXPIRE_SOON_EMAIL_PROCESS = "quote-to-expire-soon-email-process";
	public static final String QUOTE_EXPIRED_EMAIL_PROCESS = "quote-expired-email-process";
	public static final String QUOTE_POST_CANCELLATION_PROCESS = "quote-post-cancellation-process";

	public static final String PARENT_UPG = "Europe1PriceFactory_Parent_UPG";

	public static final String CTX_ATTRIBUTE_UNITS = "units";
}
