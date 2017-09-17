/*********************************************************************
 * The Initial Developer of the content of this file is MASTERDATA.
 * All portions of the code written by MASTERDATA are property of
 * MASTRDATA. All Rights Reserved.
 *
 * (c) 2015 by MASTERDATA
 *********************************************************************/

package ru.masterdata.internationalization.messagesource;

import java.text.MessageFormat;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.util.StringUtils;

import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import ru.masterdata.internationalization.model.LocalizationEntryModel;
import ru.masterdata.internationalization.services.LocalizationEntryService;

/**
 * @author ekaterina.agievich@masterdata.ru
 */
public class CustomLocalizationMessageSource extends AbstractMessageSource
{
    private static final Logger LOG                         = Logger.getLogger(CustomLocalizationMessageSource.class);
    private static final String NO_TRANSLATION              = "No translation found for code ";
    private static final String FALLBACK_MESSAGE            = "Could not find translation in current locale for %s. Falling back to default locale";
    private static final  String EXCEPTION_WHILE_TRANSLATING = "Exception while translating ";

    @Autowired
    private LocalizationEntryService localizationEntryService;

    private String fallbackLocale;

    @Override
    protected MessageFormat resolveCode(final String code, final Locale locale)
    {
        String message;
        try
        {
            LocalizationEntryModel entryModel = localizationEntryService.getLocalizationEntry(code);
            try
            {
                message = entryModel.getLocalizedText(locale);
                if(StringUtils.isEmpty(message)){
                    throw new RuntimeException();
                }
            }
            catch(Exception e)
            {
                LOG.debug(String.format(FALLBACK_MESSAGE, code));
                message = entryModel.getLocalizedText(Locale.forLanguageTag(fallbackLocale));
            }
            if(StringUtils.isEmpty(message))
            {
                LOG.info(NO_TRANSLATION + code);
                return null;
            }
        }
        catch (UnknownIdentifierException ex)
        {
            LOG.warn(NO_TRANSLATION + code);
            return null;
        }
        catch (Exception e)
        {
            LOG.warn(EXCEPTION_WHILE_TRANSLATING + code);
            return null;
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
