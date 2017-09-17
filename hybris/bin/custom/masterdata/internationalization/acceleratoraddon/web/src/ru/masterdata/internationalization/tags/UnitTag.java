package ru.masterdata.internationalization.tags;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.springframework.context.MessageSource;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.util.TagUtils;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import ru.masterdata.internationalization.enums.LocalizedUnit;
import ru.masterdata.internationalization.util.PluralFormMatcher;

/**
 * @author Created by Kate Agievich on 8/18/2015.
 */
public final class UnitTag extends SimpleTagSupport {

  private CommonI18NService commonI18NService = Registry.getApplicationContext().getBean("commonI18NService", CommonI18NService.class);

  public static final String LOCALIZATION_KEY_TEMPLATE = "internationalization.%s.%s";
  private int number;
  private LocalizedUnit localizedUnit;
  private String var;
  private String scope = "page";

  /**
   * Adds specified unit in correct plural form for current locale
   */
  public void doTag() throws JspException, IOException {
    final PageContext pageContext = (PageContext) getJspContext();
    final HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
    final MessageSource messageSource = RequestContextUtils.getTheme(request).getMessageSource();

    //formula only works correctly for positive numbers
    int absNumber = Math.abs(number);
    Locale currentLocale = (Locale)((JaloSession)pageContext.getSession().getAttribute("jalosession")).getAttribute("locale");
    if (currentLocale == null) {
      LanguageModel lang = commonI18NService.getCurrentLanguage();
      Locale locale = commonI18NService.getLocaleForLanguage(lang);
      currentLocale = locale == null ? Locale.getDefault() : locale;
    }
    int pluralForm = PluralFormMatcher.getPluralForm(absNumber, currentLocale);
    String localizationKey = String.format(LOCALIZATION_KEY_TEMPLATE, localizedUnit.getCode(), pluralForm);
    String message = number + " " + messageSource.getMessage(localizationKey, new Object[] {number}, currentLocale);

    if(this.var != null) {
      pageContext.setAttribute(this.var, message, TagUtils.getScope(this.scope));
    } else {
      pageContext.getOut().print(message);
    }

  }

  public void setNumber(int number) {
    this.number = number;
  }

  public void setLocalizedUnit(LocalizedUnit localizedUnit) {
    this.localizedUnit = localizedUnit;
  }

  public void setVar(String var) {
    this.var = var;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }

}
