package com.bridgex.initialdata.services.dataimport.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import de.hybris.platform.commerceservices.dataimport.impl.SampleDataImportService;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.commerceservices.setup.data.ImportData;
import de.hybris.platform.commerceservices.util.ResponsiveUtils;
import de.hybris.platform.core.initialization.SystemSetupContext;

public class PentlandSampleDataImportService extends SampleDataImportService implements InitializingBean{

  public static final String IMPORT_SAMPLE_DATA                         = "importSampleData";
  private static final String IMPORT_SITE = "importSite";
  public static final String BTG_EXTENSION_NAME                         = "btg";
  public static final String CMS_COCKPIT_EXTENSION_NAME                 = "cmscockpit";
  public static final String PRODUCT_COCKPIT_EXTENSION_NAME             = "productcockpit";
  public static final String CS_COCKPIT_EXTENSION_NAME                  = "cscockpit";
  public static final String REPORT_COCKPIT_EXTENSION_NAME              = "reportcockpit";
  public static final String MCC_EXTENSION_NAME                         = "mcc";
  public static final String CUSTOMER_SUPPORT_BACKOFFICE_EXTENSION_NAME = "customersupportbackoffice";
  public static final String ORDER_MANAGEMENT_BACKOFFICE_EXTENSION_NAME = "ordermanagementbackoffice";
  public static final String ASSISTED_SERVICE_EXTENSION_NAME            = "assistedservicestorefront";
  private static final String IMPORT_PRODUCT_SAMPLE = "importProductSample";
  private boolean importProductSample = true;


  private String environment;
  private boolean isCleanInstall = false;

  @Override
  public void afterPropertiesSet() throws Exception {
      isCleanInstall = "prod".equals(environment);
  }

  @Override
  public void execute(final AbstractSystemSetup systemSetup, final SystemSetupContext context, final List<ImportData> importData)
  {
    final boolean importSampleData = systemSetup.getBooleanSystemSetupParameter(context, IMPORT_SAMPLE_DATA);
    importProductSample = systemSetup.getBooleanSystemSetupParameter(context, IMPORT_PRODUCT_SAMPLE);
    final String site = context.getParameter(context.getExtensionName() + "_" + IMPORT_SITE);

    if (importSampleData) {
      for (final ImportData data : importData) {
        if (org.apache.commons.lang.StringUtils.isNotBlank(data.getProductCatalogName()) && data.getProductCatalogName().equals(site)) {
          importAllData(systemSetup, context, data, true);
        }
      }
    }
  }

  @Override
  protected void importCommonData(final String extensionName)
  {
    if (isExtensionLoaded(CMS_COCKPIT_EXTENSION_NAME)) {
      getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/cockpits/cmscockpit/cmscockpit-users.impex", extensionName), false);
    }

    if (isExtensionLoaded(PRODUCT_COCKPIT_EXTENSION_NAME)) {
      getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/cockpits/productcockpit/productcockpit-users.impex", extensionName), false);
    }

    if (isExtensionLoaded(CS_COCKPIT_EXTENSION_NAME)) {
      getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/cockpits/cscockpit/cscockpit-users.impex", extensionName), false);
    }

    if (isExtensionLoaded(REPORT_COCKPIT_EXTENSION_NAME)) {
      getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/cockpits/reportcockpit/reportcockpit-users.impex", extensionName), false);

      if (isExtensionLoaded(MCC_EXTENSION_NAME)) {
        getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/cockpits/reportcockpit/reportcockpit-mcc-links.impex", extensionName), false);
      }
    }

    if (isExtensionLoaded(CUSTOMER_SUPPORT_BACKOFFICE_EXTENSION_NAME) || isExtensionLoaded(ORDER_MANAGEMENT_BACKOFFICE_EXTENSION_NAME)) {
      getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/backoffice/customersupport/customersupport-groups.impex", extensionName), false);

      getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/backoffice/customersupport/customersupport-users.impex", extensionName), false);
      getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/backoffice/customersupport/customersupport-savedqueries.impex", extensionName), false);
      getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/backoffice/customersupport/customersupport-accessrights.impex", extensionName), false);
      getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/backoffice/customersupport/customersupport-restrictions.impex", extensionName), false);
    }

    if (isExtensionLoaded(CUSTOMER_SUPPORT_BACKOFFICE_EXTENSION_NAME) && isExtensionLoaded(ASSISTED_SERVICE_EXTENSION_NAME)) {
      getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/backoffice/customersupport/customersupport-assistedservice-groups.impex", extensionName), false);
    }

    if(this.importProductSample) {
      //user sample data
      getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/customers/sample_users.impex", extensionName), false);

      //user orders sample data
      getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/customers/sample_orders.impex", extensionName), false);
    }
  }

  @Override
  protected void importProductCatalog(final String extensionName, final String productCatalogName)
  {
    if(this.importProductSample) {
      // Load Units
      getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/classifications-units.impex", extensionName, productCatalogName),
                                             false);

      // Load Categories
      getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/categories.impex", extensionName, productCatalogName), false);
      getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/categories-visibility.impex", extensionName, productCatalogName), false);
      getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/categories-classifications.impex",
                                                           extensionName,
                                                           productCatalogName), false);

      // Load Suppliers
      getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/suppliers.impex", extensionName, productCatalogName), false);
      getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/suppliers-media.impex", extensionName, productCatalogName), false);

      // Load medias for Categories as Suppliers loads some new Categories
      getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/categories-media.impex", extensionName, productCatalogName),
                                             false);

      // Load Products
      getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/products.impex", extensionName, productCatalogName), false);
      getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/products-media.impex", extensionName, productCatalogName), false);
      getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/products-classifications.impex",
                                                           extensionName,
                                                           productCatalogName), false);

      // Load Products Relations
      getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/products-relations.impex", extensionName, productCatalogName),
                                             false);

      // Load Products Fixes
      getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/products-fixup.impex", extensionName, productCatalogName), false);

      // Load Prices
      getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/products-prices.impex", extensionName, productCatalogName), false);

      // Load Stock Levels
      getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/products-stocklevels.impex", extensionName, productCatalogName),
                                             false);
      getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/products-pos-stocklevels.impex",
                                                           extensionName,
                                                           productCatalogName), false);

      // Load Taxes
      getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/products-tax.impex", extensionName, productCatalogName), false);

      // Load Users
      getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/users.impex", extensionName, productCatalogName), false);
    }else{
      // Load Units
      getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/classifications-units.impex", extensionName, productCatalogName),
                                             false);

      // Load Categories
      getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/categories-prod.impex", extensionName, productCatalogName), false);
      getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/categories-visibility.impex", extensionName, productCatalogName), false);
      getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/categories-classifications.impex",
                                                           extensionName,
                                                           productCatalogName), false);
    }

  }

  @Override
  protected void importContentCatalog(final String extensionName, final String contentCatalogName)
  {
    getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/contentCatalogs/%sContentCatalog/cms-content.impex", extensionName, contentCatalogName), false);
    getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/contentCatalogs/%sContentCatalog/cms-mobile-content.impex", extensionName, contentCatalogName),
                                           false);
    getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/contentCatalogs/%sContentCatalog/email-content.impex", extensionName, contentCatalogName), false);
    if (ResponsiveUtils.isResponsive()) {
      getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/contentCatalogs/%sContentCatalog/cms-responsive-content.impex",
                                                           extensionName,
                                                           contentCatalogName), false);
    }

    getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/contentCatalogs/%sContentCatalog/ave-asm-content.impex", extensionName, contentCatalogName), false);
  }

  @Override
  protected void importStore(final String extensionName, final String storeName, final String productCatalogName)
  {
    if(this.importProductSample) {
      getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/stores/%s/points-of-service-media.impex", extensionName, storeName), false);
      getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/stores/%s/points-of-service.impex", extensionName, storeName), false);
      if (isExtensionLoaded(BTG_EXTENSION_NAME)) {
        getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/stores/%s/btg.impex", extensionName, storeName), false);
      }
      getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/stores/%s/warehouses.impex", extensionName, storeName), false);

      getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/productCatalogs/%sProductCatalog/reviews.impex", extensionName, productCatalogName), false);

      getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/stores/%s/promotions.impex", extensionName, storeName), false);
    }
  }

  @Override
  protected void importJobs(final String extensionName, final String storeName)
  {
    getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/stores/%s/jobs.impex", extensionName, storeName), false);
  }

  @Override
  protected void importSolrIndex(final String extensionName, final String storeName)
  {
    getSetupImpexService().importImpexFile(String.format("/%s/import/sampledata/stores/%s/solr.impex", extensionName, storeName), false);

    getSetupSolrIndexerService().createSolrIndexerCronJobs(String.format("%sIndex", storeName));
  }

  public void setEnvironment(String environment) {
    this.environment = environment;
  }

}
