/*********************************************************************
 * The Initial Developer of the content of this file is MASTERDATA.
 * All portions of the code written by MASTERDATA are property of
 * MASTRDATA. All Rights Reserved.
 *
 * (c) 2015 by MASTERDATA
 *********************************************************************/

package ru.masterdata.internationalization.setup;

import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.commerceservices.setup.data.ImportData;
import de.hybris.platform.commerceservices.setup.events.CoreDataImportedEvent;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;
import de.hybris.platform.util.Config;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ekaterina.agievich@masterdata.ru
 */
@SystemSetup(extension="internationalization")
public class CoreDataSystemSetup extends AbstractSystemSetup
{

    private static final Logger LOG = Logger.getLogger(CoreDataSystemSetup.class);
    private static final String IMPORT_INTERNATIONALIZATION_DATA="importInternationalizationCoreData";
    private static final String IMPORT_DIR = "/internationalization/import/coredata";

    @Override
    @SystemSetupParameterMethod
    public List<SystemSetupParameter> getInitializationOptions()
    {
        final List<SystemSetupParameter> params = new ArrayList<>();
        params.add(createBooleanSystemSetupParameter(IMPORT_INTERNATIONALIZATION_DATA, "Import frontend localization", true));
        return params;
    }

    @SystemSetup(type = SystemSetup.Type.PROJECT, process = SystemSetup.Process.ALL)
    public void createProjectData(final SystemSetupContext context){
        if(getBooleanSystemSetupParameter(context, IMPORT_INTERNATIONALIZATION_DATA)){
            final String importDir = Config.getParameter("internationalization.resources.dir").replaceAll("\\\\", "/");
            final File dir = new File(importDir + IMPORT_DIR);

                for(File file : FileUtils.listFiles(dir, null, true))
                {
                    final String path = file.getAbsolutePath().replaceAll("\\\\", "/");
                    importImpexFile(context, path.replaceAll(importDir, ""));
                }
        }
    }
}
