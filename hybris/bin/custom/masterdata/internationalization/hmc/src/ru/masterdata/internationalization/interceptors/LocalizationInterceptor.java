/*********************************************************************
 * The Initial Developer of the content of this file is MASTERDATA.
 * All portions of the code written by MASTERDATA are property of
 * MASTRDATA. All Rights Reserved.
 *
 * (c) 2015 by MASTERDATA
 *********************************************************************/

package internationalization.interceptors;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import internationalization.services.LocalizationEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import ru.masterdata.internationalization.model.LocalizationEntryModel;

/**
 * @author ekaterina.agievich@masterdata.ru
 */
public class LocalizationInterceptor implements PrepareInterceptor, RemoveInterceptor
{

    @Autowired
    private LocalizationEntryService localizationEntryService;

    @Override
    public void onPrepare(final Object o, final InterceptorContext interceptorContext) throws InterceptorException
    {
        evictFromCache(o);
    }

    @Override
    public void onRemove(final Object o, final InterceptorContext interceptorContext) throws InterceptorException
    {
        evictFromCache(o);
    }

    private void evictFromCache(final Object o)
    {
        if(o instanceof LocalizationEntryModel)
        {
            LocalizationEntryModel entryModel = (LocalizationEntryModel)o;
            localizationEntryService.evictLocalizationEntryFromCache(entryModel.getCode());
        }
    }
}
