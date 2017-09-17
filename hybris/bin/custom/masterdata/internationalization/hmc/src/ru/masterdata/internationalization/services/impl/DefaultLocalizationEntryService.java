/*********************************************************************
 * The Initial Developer of the content of this file is MASTERDATA.
 * All portions of the code written by MASTERDATA are property of
 * MASTRDATA. All Rights Reserved.
 *
 * (c) 2015 by MASTERDATA
 *********************************************************************/

package internationalization.services.impl;

import internationalization.dao.LocalizationEntryDao;
import internationalization.services.LocalizationEntryService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import ru.masterdata.internationalization.model.LocalizationEntryModel;

/**
 * @author ekaterina.agievich@masterdata.ru
 */
public class DefaultLocalizationEntryService implements LocalizationEntryService
{
    private static final Logger LOG = Logger.getLogger(DefaultLocalizationEntryService.class);

    public static final String LOCALIZATION_CACHE="LOCALIZATION";

    @Autowired
    private LocalizationEntryDao localizationEntryDao;

    @Override
    @Cacheable(value=LOCALIZATION_CACHE, key="#code")
    public LocalizationEntryModel getLocalizationEntry(final String code)
    {
        return localizationEntryDao.getLocalizationEntryByCode(code);
    }

    @Override
    @CacheEvict(value=LOCALIZATION_CACHE, key="#code")
    public void evictLocalizationEntryFromCache(final String code)
    {
    }
}
