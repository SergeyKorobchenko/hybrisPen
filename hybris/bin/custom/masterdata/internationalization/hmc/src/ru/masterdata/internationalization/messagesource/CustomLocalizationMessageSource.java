/*********************************************************************
 * The Initial Developer of the content of this file is MASTERDATA.
 * All portions of the code written by MASTERDATA are property of
 * MASTRDATA. All Rights Reserved.
 *
 * (c) 2015 by MASTERDATA
 *********************************************************************/

package internationalization.messagesource;

import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import internationalization.services.LocalizationEntryService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.util.StringUtils;
import ru.masterdata.internationalization.model.LocalizationEntryModel;

import java.text.MessageFormat;
import java.util.Locale;

/**
 * @author ekaterina.agievich@masterdata.ru
 */
public class CustomLocalizationMessageSource extends AbstractMessageSource
{
    private static final Logger LOG = Logger.getLogger(CustomLocalizationMessageSource.class);

    @Autowired
    LocalizationEntryService localizationEntryService;
    private String fallbackLocale;

    @Override
    protected MessageFormat resolveCode(final String code, final Locale locale)
    {
        String message = null;
        try
        {
            LocalizationEntryModel entryModel = localizationEntryService.getLocalizationEntry(code);
            try
            {
                message = entryModel.getLocalizedText(locale);
            }
            catch(Exception e)
            {
                LOG.debug("Exception while translating " + code + ". Falling back to default locale");
                message = entryModel.getLocalizedText(Locale.forLanguageTag(fallbackLocale));
            }
            if(StringUtils.isEmpty(message))
            {
                LOG.info("No translation found for code " + code);
                message = code;
            }
        }
        catch (UnknownIdentifierException ex)
        {
            LOG.info("No translation found for code " + code);
        }
        catch (Exception e)
        {
            LOG.warn("Exception while translating " + code);
            message = code;
        }
        return new MessageFormat(message, locale);
    }
    public String getFallbackLocale()
    {
        return fallbackLocale;
    }
    public void setFallbackLocale(final String fallbackLocale)
    {
        this.fallbackLocale=fallbackLocale;
    }
}
