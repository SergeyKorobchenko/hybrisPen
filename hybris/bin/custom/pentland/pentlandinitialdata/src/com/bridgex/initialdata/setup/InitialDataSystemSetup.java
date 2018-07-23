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
package com.bridgex.initialdata.setup;

import de.hybris.platform.commerceservices.dataimport.impl.CoreDataImportService;
import de.hybris.platform.commerceservices.dataimport.impl.SampleDataImportService;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.commerceservices.setup.data.ImportData;
import de.hybris.platform.commerceservices.setup.events.CoreDataImportedEvent;
import de.hybris.platform.commerceservices.setup.events.SampleDataImportedEvent;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;
import com.bridgex.initialdata.constants.PentlandinitialdataConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * This class provides hooks into the system's initialization and update processes.
 */
@SystemSetup(extension = PentlandinitialdataConstants.EXTENSIONNAME)
public class InitialDataSystemSetup extends AbstractSystemSetup
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(InitialDataSystemSetup.class);

	private static final String IMPORT_CORE_DATA = "importCoreData";
	private static final String IMPORT_SAMPLE_DATA = "importSampleData";
	private static final String ACTIVATE_SOLR_CRON_JOBS = "activateSolrCronJobs";
	private static final String IMPORT_PRODUCT_SAMPLE = "importProductSample";

	private static final String IMPORT_SITE = "importSite";

	private static final String PENTLAND = "pentland";
	private static final String PENTLAND_STORE = "pentland";
	private static final List<String> B2B_STORES = Collections.singletonList(PENTLAND_STORE);

	private static final String ENDURA = "endurasport";
	private static final String ENDURA_UK_STORE = "endurasport_uk";
	private static final String ENDURA_US_STORE = "endurasport_us";
	private static final List<String> ENDURA_STORES =  Arrays.asList(ENDURA_UK_STORE,ENDURA_US_STORE);

	private CoreDataImportService coreDataImportService;
	private SampleDataImportService sampleDataImportService;

	/**
	 * Generates the Dropdown and Multi-select boxes for the project data import
	 */
	@Override
	@SystemSetupParameterMethod
	public List<SystemSetupParameter> getInitializationOptions()
	{
		final List<SystemSetupParameter> params = new ArrayList<SystemSetupParameter>();

		final SystemSetupParameter syncProductsParam = new SystemSetupParameter(IMPORT_SITE);
		syncProductsParam.setLabel("Choose site to Import");
		syncProductsParam.addValue(PENTLAND, true);
		syncProductsParam.addValue(ENDURA, false);

		params.add(syncProductsParam);

		params.add(createBooleanSystemSetupParameter(IMPORT_CORE_DATA, "Import Core Data", false));
		params.add(createBooleanSystemSetupParameter(IMPORT_SAMPLE_DATA, "Import Sample Data", false));
		params.add(createBooleanSystemSetupParameter(ACTIVATE_SOLR_CRON_JOBS, "Activate Solr Cron Jobs", false));
		params.add(createBooleanSystemSetupParameter(IMPORT_PRODUCT_SAMPLE, "Import Product Data", false));
		// Add more Parameters here as you require



		return params;
	}

	/**
	 * Implement this method to create initial objects. This method will be called by system creator during
	 * initialization and system update. Be sure that this method can be called repeatedly.
	 * 
	 * @param context
	 *           the context provides the selected parameters and values
	 */
	@SystemSetup(type = Type.ESSENTIAL, process = Process.ALL)
	public void createEssentialData(final SystemSetupContext context)
	{
		// Add Essential Data here as you require
	}

	/**
	 * Implement this method to create data that is used in your project. This method will be called during the system
	 * initialization. <br>
	 * Add import data for each site you have configured
	 *
	 * <pre>
	 * final List<ImportData> importData = new ArrayList<ImportData>();
	 *
	 * final ImportData sampleImportData = new ImportData();
	 * sampleImportData.setProductCatalogName(SAMPLE_PRODUCT_CATALOG_NAME);
	 * sampleImportData.setContentCatalogNames(Arrays.asList(SAMPLE_CONTENT_CATALOG_NAME));
	 * sampleImportData.setStoreNames(Arrays.asList(SAMPLE_STORE_NAME));
	 * importData.add(sampleImportData);
	 *
	 * getCoreDataImportService().execute(this, context, importData);
	 * getEventService().publishEvent(new CoreDataImportedEvent(context, importData));
	 *
	 * getSampleDataImportService().execute(this, context, importData);
	 * getEventService().publishEvent(new SampleDataImportedEvent(context, importData));
	 * </pre>
	 *
	 * @param context
	 *           the context provides the selected parameters and values
	 */
	@SystemSetup(type = Type.PROJECT, process = Process.ALL)
	public void createProjectData(final SystemSetupContext context)
	{
		final List<ImportData> importData = new ArrayList<>();


		final ImportData pentlandImportData = new ImportData();
		pentlandImportData.setProductCatalogName(PENTLAND);
		pentlandImportData.setContentCatalogNames(Collections.singletonList(PENTLAND));
		pentlandImportData.setStoreNames(B2B_STORES);
		importData.add(pentlandImportData);

		final ImportData enduraImportData = new ImportData();
		enduraImportData.setProductCatalogName(ENDURA);
		enduraImportData.setContentCatalogNames(Collections.singletonList(ENDURA));
		enduraImportData.setStoreNames(ENDURA_STORES);
		importData.add(enduraImportData);

		getCoreDataImportService().execute(this, context, importData);
		final String site = context.getParameter(context.getExtensionName() + "_" + IMPORT_SITE);
		if (!ENDURA.equals(site)) {
			getEventService().publishEvent(new CoreDataImportedEvent(context, importData));
		}

		getSampleDataImportService().execute(this, context, importData);
		if (!ENDURA.equals(site)) {
			getEventService().publishEvent(new SampleDataImportedEvent(context, importData));
		}

	}

	public CoreDataImportService getCoreDataImportService()
	{
		return coreDataImportService;
	}

	@Required
	public void setCoreDataImportService(final CoreDataImportService coreDataImportService)
	{
		this.coreDataImportService = coreDataImportService;
	}

	public SampleDataImportService getSampleDataImportService()
	{
		return sampleDataImportService;
	}

	@Required
	public void setSampleDataImportService(final SampleDataImportService sampleDataImportService)
	{
		this.sampleDataImportService = sampleDataImportService;
	}
}
