/*********************************************************************
 * The Initial Developer of the content of this file is MASTERDATA.
 * All portions of the code written by MASTERDATA are property of
 * MASTRDATA. All Rights Reserved.
 *
 * (c) 2015 by MASTERDATA
 *********************************************************************/

package ru.masterdata.internationalization.dao.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import ru.masterdata.internationalization.dao.LocalizationEntryDao;
import ru.masterdata.internationalization.model.LocalizationEntryModel;

import java.util.List;

/**
 * @author ekaterina.agievich@masterdata.ru
 */
public class DefaultLocalizationEntryDao implements LocalizationEntryDao
{
    @Autowired
    private FlexibleSearchService flexibleSearchService;

    @Override
    public List<LocalizationEntryModel> getLocalizationEntries()
    {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT {").append(LocalizationEntryModel.PK).append("} FROM {").append(LocalizationEntryModel._TYPECODE).append("}");

        FlexibleSearchQuery query = new FlexibleSearchQuery(queryBuilder.toString());
        SearchResult<LocalizationEntryModel> result = flexibleSearchService.search(query);

        return result.getResult();
    }
    @Override
    public LocalizationEntryModel getLocalizationEntryByCode(final String code)
    {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT {").append(LocalizationEntryModel.PK).append("} FROM {").append(LocalizationEntryModel._TYPECODE)
                .append("} WHERE {").append(LocalizationEntryModel.CODE).append("}=?code");

        FlexibleSearchQuery query = new FlexibleSearchQuery(queryBuilder.toString());
        query.addQueryParameter("code", code);

        return flexibleSearchService.searchUnique(query);
    }
}
