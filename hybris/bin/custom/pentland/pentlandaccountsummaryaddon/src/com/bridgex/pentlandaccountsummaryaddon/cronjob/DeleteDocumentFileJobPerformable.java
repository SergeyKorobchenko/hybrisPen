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
package com.bridgex.pentlandaccountsummaryaddon.cronjob;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.bridgex.pentlandaccountsummaryaddon.document.service.B2BDocumentService;
import com.bridgex.pentlandaccountsummaryaddon.model.cronjob.DeleteDocumentFileCronJobModel;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

public class DeleteDocumentFileJobPerformable extends AbstractJobPerformable<DeleteDocumentFileCronJobModel>
{

	@Resource
	private B2BDocumentService b2bDocumentService;

	private static final Logger LOG = Logger.getLogger(com.bridgex.pentlandaccountsummaryaddon.cronjob.DeleteDocumentFileJobPerformable.class.getName());

	@Override
	public PerformResult perform(final DeleteDocumentFileCronJobModel arg0)
	{

		 // if arg0.getNumberOfDay() set to null by mistake, make sure not to delete any media files. so set the number of
		 // day to 1000 years(365*1000)

		int numberOfDay = 365 * 1000;

		if (arg0.getNumberOfDay() == null)
		{
			LOG.error("[perform] number of days = null => set to 365*1000 in order to not delete any media files.");
		}
		else
		{
			numberOfDay = arg0.getNumberOfDay();
		}


		b2bDocumentService.deleteB2BDocumentFiles(numberOfDay, arg0.getDocumentTypeList(), arg0.getDocumentStatusList());

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);

	}

}
