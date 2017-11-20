package com.bridgex.pentlandaccountsummaryaddon.cronjob;

import java.util.Collections;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.bridgex.pentlandaccountsummaryaddon.document.service.B2BDocumentService;
import com.bridgex.pentlandaccountsummaryaddon.model.cronjob.DeleteOldDocumentsCronJobModel;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

/**
 * @author Goncharenko Mikhail, created on 17.11.2017.
 */
public class DeleteDocumentsJobPerformable extends AbstractJobPerformable<DeleteOldDocumentsCronJobModel> {

  private static final Logger LOG = Logger.getLogger(com.bridgex.pentlandaccountsummaryaddon.cronjob.DeleteDocumentsJobPerformable.class.getName());

  @Resource
  private B2BDocumentService b2bDocumentService;

  @Override
  public PerformResult perform(DeleteOldDocumentsCronJobModel cronJob) {
    if (cronJob.getNumberOfDay() == null) {
      LOG.error("[perform] number of days is null, aborted.");
      return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
    }

    b2bDocumentService.deleteB2BDocumentFiles(cronJob.getNumberOfDay(), Collections.emptyList(), Collections.emptyList());
    b2bDocumentService.deleteB2BDocuments(cronJob.getNumberOfDay(), Collections.emptyList(), Collections.emptyList());

    return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
  }
}
