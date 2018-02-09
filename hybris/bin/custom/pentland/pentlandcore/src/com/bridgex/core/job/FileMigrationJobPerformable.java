package com.bridgex.core.job;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import com.bridgex.core.model.FileMigrationCronJobModel;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

/**
 * @author Created by ekaterina.agievich@masterdata.ru on 2/9/2018.
 */
public class FileMigrationJobPerformable extends AbstractJobPerformable<FileMigrationCronJobModel> {

  private static final Logger LOG = Logger.getLogger(FileMigrationJobPerformable.class);
  @Override
  public PerformResult perform(FileMigrationCronJobModel fileMigrationCronJobModel) {
    Assert.notNull(fileMigrationCronJobModel.getSourceFolder(), "Cannot run cronjob, source folder not specified");
    Assert.notNull(fileMigrationCronJobModel.getTargetFolder(), "Cannot run cronjob, target folder not specified");
    String sourceFolder = fileMigrationCronJobModel.getSourceFolder();
    String targetFolder = fileMigrationCronJobModel.getTargetFolder();
    String fileMask = "*";
    if(StringUtils.isNotEmpty(fileMigrationCronJobModel.getFileMask())){
      fileMask = fileMigrationCronJobModel.getFileMask();
    }

    Path source = FileSystems.getDefault().getPath(sourceFolder);
    if(Files.notExists(source)){
      LOG.error("Source folder doesn't exist");
      return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
    }
    Path target = FileSystems.getDefault().getPath(targetFolder);
    if(Files.notExists(target)){
      try {
        Files.createDirectory(target);
      }
      catch (IOException e) {
        LOG.error("Failed to create target folder: " + e);
        return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
      }
    }

    PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + fileMask);

    AtomicInteger count = new AtomicInteger(0);
    try {
      Files.list(source).filter(file -> matcher.matches(file.getFileName())).forEach(file -> {
        try {
          Files.move(file, target.resolve(file.getFileName()), REPLACE_EXISTING);
          count.incrementAndGet();
        }
        catch (IOException e) {
          LOG.error(e.getClass() + " while trying to move file: " + e.getMessage());
        }
      });
    }
    catch (IOException e) {
      LOG.error(e.getClass() + " while listing files in source folder: " + e.getMessage());
      return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
    }

    LOG.info(String.format("Successfully moved %d files. ", count.get()));
    return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
  }
}
