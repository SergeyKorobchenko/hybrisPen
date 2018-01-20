package com.bridgex.core.impex.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.impex.model.cronjob.ImpExImportCronJobModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.servicelayer.cluster.ClusterService;
import de.hybris.platform.servicelayer.exceptions.ModelInitializationException;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.impl.DefaultImportService;
import de.hybris.platform.servicelayer.impex.impl.ImportCronJobResult;

/**
 * Created by Lenar on 10/13/2017.
 */
public class HotfolderImportService extends DefaultImportService {

  private static final Logger LOG = Logger.getLogger(HotfolderImportService.class);

  private ClusterService clusterService;

  /**
   * Restrict hot folder impex cronjob to current cluster node
   * @param config
   * @return
   */
  public ImportResult importData(ImportConfig config) {
    return this.importDataUsingStandardImpex(config);
  }

  private ImportResult importDataUsingStandardImpex(ImportConfig config) {
    LOG.info("Importing data using standard impex...");
    ImpExImportCronJobModel cronJob = this.getModelService().create("ImpExImportCronJob");

    try {
      this.getModelService().initDefaults(cronJob);
    } catch (ModelInitializationException var4) {
      throw new SystemException(var4);
    }
    LOG.info("Created cronjob for hot folder import " + cronJob.getCode());
    this.configureCronJob(cronJob, config);
    cronJob.setNodeID(clusterService.getClusterId());
    this.getModelService().saveAll(cronJob.getJob(), cronJob);
    LOG.info("Succesfully configured job " + cronJob.getCode() + " to import on cluster node " + cronJob.getNodeID());
    this.importData(cronJob, config.isSynchronous(), config.isRemoveOnSuccess());
    return ((Item)this.getModelService().getSource(cronJob)).isAlive() ? new ImportCronJobResult(cronJob) : new ImportCronJobResult(null);
  }

  @Required
  public void setClusterService(ClusterService clusterService) {
    this.clusterService = clusterService;
  }
}

