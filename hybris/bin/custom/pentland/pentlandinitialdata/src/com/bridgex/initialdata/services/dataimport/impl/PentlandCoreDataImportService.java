package com.bridgex.initialdata.services.dataimport.impl;

import java.util.List;

import de.hybris.platform.commerceservices.dataimport.impl.CoreDataImportService;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.commerceservices.setup.data.ImportData;
import de.hybris.platform.commerceservices.util.ResponsiveUtils;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.validation.services.ValidationService;

public class PentlandCoreDataImportService extends CoreDataImportService {

  public static final String IMPORT_CORE_DATA = "importCoreData";

  @Override
  public void execute(final AbstractSystemSetup systemSetup, final SystemSetupContext context, final List<ImportData> importData)
  {
    final boolean importCoreData = systemSetup.getBooleanSystemSetupParameter(context, IMPORT_CORE_DATA);

    if (importCoreData)
    {
      for (final ImportData data : importData)
      {
        importAllData(systemSetup, context, data, false);
      }

      final ValidationService validation = getBeanForName("validationService");
      validation.reloadValidationEngine();
    }
  }

  @Override
  protected void importCommonData(final String extensionName)
  {
    getSetupImpexService().importImpexFile(String.format("/%s/import/coredata/common/essential-data.impex", extensionName),
                                           true);
    getSetupImpexService().importImpexFile(String.format("/%s/import/coredata/common/countries.impex", extensionName), false);
    getSetupImpexService().importImpexFile(String.format("/%s/import/coredata/common/delivery-modes.impex", extensionName),
                                           false);
    getSetupImpexService().importImpexFile(String.format("/%s/import/coredata/common/themes.impex", extensionName), false);
  }

  @Override
  protected void importProductCatalog(final String extensionName, final String productCatalogName)
  {
    getSetupImpexService()
      .importImpexFile(
        String.format("/%s/import/coredata/productCatalogs/%sProductCatalog/catalog.impex", extensionName,
                      productCatalogName), false);
  }

  @Override
  protected void importContentCatalog(final String extensionName, final String contentCatalogName)
  {
    getSetupImpexService()
      .importImpexFile(
        String.format("/%s/import/coredata/contentCatalogs/%sContentCatalog/catalog.impex", extensionName,
                      contentCatalogName), false);
    getSetupImpexService().importImpexFile(
      String.format("/%s/import/coredata/contentCatalogs/%sContentCatalog/cms-content.impex", extensionName,
                    contentCatalogName), false);
    getSetupImpexService().importImpexFile(
      String.format("/%s/import/coredata/contentCatalogs/%sContentCatalog/cms-mobile-content.impex", extensionName,
                    contentCatalogName), false);
    getSetupImpexService().importImpexFile(
      String.format("/%s/import/coredata/contentCatalogs/%sContentCatalog/email-content.impex", extensionName,
                    contentCatalogName), false);
    if (ResponsiveUtils.isResponsive())
    {
      getSetupImpexService().importImpexFile(
        String.format("/%s/import/coredata/contentCatalogs/%sContentCatalog/cms-responsive-content.impex", extensionName,
                      contentCatalogName), false);
    }
  }

  @Override
  protected void importStore(final String extensionName, final String storeName, final String productCatalogName)
  {
    getSetupImpexService().importImpexFile(
      String.format("/%s/import/coredata/stores/%s/store.impex", extensionName, storeName), false);
    getSetupImpexService().importImpexFile(String.format("/%s/import/coredata/stores/%s/site.impex", extensionName, storeName),
                                           false);
    if (ResponsiveUtils.isResponsive())
    {
      getSetupImpexService().importImpexFile(
        String.format("/%s/import/coredata/stores/%s/store-responsive.impex", extensionName, storeName), false);
      getSetupImpexService().importImpexFile(
        String.format("/%s/import/coredata/stores/%s/site-responsive.impex", extensionName, storeName), false);
    }
  }

  @Override
  protected void importSolrIndex(final String extensionName, final String storeName)
  {
    getSetupImpexService().importImpexFile(String.format("/%s/import/coredata/stores/%s/solr.impex", extensionName, storeName),
                                           false);

    getSetupSolrIndexerService().createSolrIndexerCronJobs(String.format("%sIndex", storeName));

    getSetupImpexService().importImpexFile(
      String.format("/%s/import/coredata/stores/%s/solrtrigger.impex", extensionName, storeName), false);
  }

  @Override
  protected void importJobs(final String extensionName, final String storeName)
  {
    getSetupImpexService().importImpexFile(String.format("/%s/import/coredata/stores/%s/jobs.impex", extensionName, storeName),
                                           false);
  }


}
