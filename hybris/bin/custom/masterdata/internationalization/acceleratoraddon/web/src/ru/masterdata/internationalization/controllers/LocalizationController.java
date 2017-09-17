package ru.masterdata.internationalization.controllers;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.context.Theme;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.RequestContextUtils;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.AbstractController;
import de.hybris.platform.commerceservices.storesession.StoreSessionService;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import ru.masterdata.internationalization.tags.UnitTag;
import ru.masterdata.internationalization.util.PluralFormMatcher;

/**
 * @author Created by Kate Agievich on 25.05.2015.
 */
@Controller
public class LocalizationController extends AbstractController {

  public static final String CSRF_TOKEN = "CSRFToken";
  public static final String ARGUMENT_SEPARATOR = ",";
  public static final String IS_UNIT = "true";
  public static final String ONLY_TEXT = "true";

  @Autowired
  I18NService       i18NService;
  @Autowired
  CommonI18NService commonI18NService;

  @RequestMapping(value = "/localize")
  public @ResponseBody Map<String, String> localizeMessages(final HttpServletRequest request) {
    Map<String, String[]> parameterMap = new HashMap<>();
    parameterMap.putAll(request.getParameterMap());
    parameterMap.remove(CSRF_TOKEN);
    return getTranslationMap(request, parameterMap);
  }

  private Map<String, String> getTranslationMap(HttpServletRequest request, Map<String, String[]> parameterMap) {
    Theme theme = RequestContextUtils.getTheme(request);
    MessageSource messageSource = theme.getMessageSource();
    LanguageModel currentLanguage = commonI18NService.getCurrentLanguage();
    Locale currentLocale = commonI18NService.getLocaleForLanguage(currentLanguage);

    Map<String, String> localizationMap = new HashMap<>();
    parameterMap.forEach((key, value) -> {
      String code = StringUtils.EMPTY;
      String arguments = StringUtils.EMPTY;
      if (value.length > 0) {
        code = value[0];
      }
      if (value.length > 1) {
        arguments = value[1];
      }
      String message = StringUtils.EMPTY;
      if(value.length > 2 && value[2].equals(IS_UNIT)) {
        int number = Integer.parseInt(arguments);
        int pluralForm = PluralFormMatcher.getPluralForm(number, currentLocale);
        String localizationKey = String.format(UnitTag.LOCALIZATION_KEY_TEMPLATE, code, pluralForm);
        message = number + " " + messageSource.getMessage(localizationKey, new Object[] {number}, currentLocale);
      }
      else {
        message = messageSource.getMessage(code, arguments.split(ARGUMENT_SEPARATOR), currentLocale);
      }
      if(value.length > 3 && value[3].equals(ONLY_TEXT)) {
        Pattern pattern = Pattern.compile("(\\d+)\\s(\\D+)");
        Matcher matcher = pattern.matcher(message);
        if (matcher.matches()) {
          message = matcher.group(2);
        }
      }
      localizationMap.put(key, message);
    });
    return localizationMap;
  }
}
