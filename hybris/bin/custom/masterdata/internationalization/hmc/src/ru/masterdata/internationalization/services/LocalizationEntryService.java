/*********************************************************************
 * The Initial Developer of the content of this file is MASTERDATA.
 * All portions of the code written by MASTERDATA are property of
 * MASTRDATA. All Rights Reserved.
 *
 * (c) 2015 by MASTERDATA
 *********************************************************************/

package internationalization.services;

import ru.masterdata.internationalization.model.LocalizationEntryModel;

/**
 * @author ekaterina.agievich@masterdata.ru
 */
public interface LocalizationEntryService
{

    LocalizationEntryModel getLocalizationEntry(String code);

    void evictLocalizationEntryFromCache(String code);

}