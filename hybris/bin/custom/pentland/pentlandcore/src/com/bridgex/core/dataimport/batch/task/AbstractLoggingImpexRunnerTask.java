package com.bridgex.core.dataimport.batch.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import de.hybris.platform.acceleratorservices.dataimport.batch.task.AbstractImpexRunnerTask;
import de.hybris.platform.servicelayer.impex.ImpExResource;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.impl.StreamBasedImpExResource;

/**
 * @author Created by ekaterina.agievich@masterdata.ru on 2/7/2018.
 */
public abstract class AbstractLoggingImpexRunnerTask extends AbstractImpexRunnerTask {

  private static final Logger LOG = Logger.getLogger(AbstractLoggingImpexRunnerTask.class);

  /**
   * Process an impex file using the given encoding
   *
   * @param file
   * @param encoding
   * @throws FileNotFoundException
   */
  protected void processFile(final File file, final String encoding) throws FileNotFoundException {
    FileInputStream fis = null;
    try {
      fis = new FileInputStream(file);
      final ImportConfig config = getImportConfig();
      if (config == null) {
        LOG.error(String.format("Import config not found. The file %s won't be imported.", file == null ? null : file.getName()));
        return;
      }
      final ImpExResource resource = new StreamBasedImpExResource(fis, encoding);
      config.setScript(resource);
      LOG.info("Begin importing file " + file.getName());
      final ImportResult importResult = getImportService().importData(config);
      if (importResult.isError() && importResult.hasUnresolvedLines()) {
        LOG.error(importResult.getUnresolvedLines().getPreview());
      }
      LOG.info("End importing file " + file.getName());
    } catch(FileNotFoundException e){
      throw e;
    } catch(Throwable e){
      LOG.error("Unexpected exception while importing file " + file.getName());
      LOG.error(e.getMessage());
    } finally {
      IOUtils.closeQuietly(fis);
    }
  }
}
