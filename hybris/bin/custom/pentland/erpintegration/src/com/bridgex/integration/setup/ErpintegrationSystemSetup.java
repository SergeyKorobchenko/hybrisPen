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
package com.bridgex.integration.setup;

import static com.bridgex.integration.constants.ErpintegrationConstants.PLATFORM_LOGO_CODE;

import de.hybris.platform.core.initialization.SystemSetup;

import java.io.InputStream;

import com.bridgex.integration.constants.ErpintegrationConstants;
import com.bridgex.integration.service.ErpintegrationService;


@SystemSetup(extension = ErpintegrationConstants.EXTENSIONNAME)
public class ErpintegrationSystemSetup
{
	private final ErpintegrationService erpintegrationService;

	public ErpintegrationSystemSetup(final ErpintegrationService erpintegrationService)
	{
		this.erpintegrationService = erpintegrationService;
	}

	@SystemSetup(process = SystemSetup.Process.INIT, type = SystemSetup.Type.ESSENTIAL)
	public void createEssentialData()
	{
		erpintegrationService.createLogo(PLATFORM_LOGO_CODE);
	}

	private InputStream getImageStream()
	{
		return ErpintegrationSystemSetup.class.getResourceAsStream("/erpintegration/sap-hybris-platform.png");
	}
}
