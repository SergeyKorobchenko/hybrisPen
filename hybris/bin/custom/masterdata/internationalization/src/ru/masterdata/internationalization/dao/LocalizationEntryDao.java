/*********************************************************************
 * The Initial Developer of the content of this file is MASTERDATA.
 * All portions of the code written by MASTERDATA are property of
 * MASTRDATA. All Rights Reserved.
 *
 * (c) 2015 by MASTERDATA
 *********************************************************************/

package ru.masterdata.internationalization.dao;

import ru.masterdata.internationalization.model.LocalizationEntryModel;

import java.util.List;

/**
 * @author ekaterina.agievich@masterdata.ru
 */
public interface LocalizationEntryDao
{
    List<LocalizationEntryModel> getLocalizationEntries();

    LocalizationEntryModel getLocalizationEntryByCode(String code);
}
