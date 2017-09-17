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
package com.bridgex.fulfilmentprocess.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import com.bridgex.fulfilmentprocess.constants.PentlandfulfilmentprocessConstants;

@SuppressWarnings("PMD")
public class PentlandfulfilmentprocessManager extends GeneratedPentlandfulfilmentprocessManager
{
	public static final PentlandfulfilmentprocessManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (PentlandfulfilmentprocessManager) em.getExtension(PentlandfulfilmentprocessConstants.EXTENSIONNAME);
	}
	
}
