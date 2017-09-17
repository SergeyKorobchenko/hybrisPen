package ru.masterdata.internationalization.util;

import java.util.Locale;

import de.hybris.platform.commercefacades.storesession.data.LanguageData;

/**
 * @author Created by Kate Agievich on 8/18/2015.
 */
public final class PluralFormMatcher {
  /**
   * Determines the correct plural form index for lookup, with 0 standing for singular form
   * @param number
   * @param locale
   * @return
   */
  public static int getPluralForm(int number, Locale locale) {
    //for additional languages refer to formulas provided http://docs.translatehouse.org/projects/localization-guide/en/latest/l10n/pluralforms.html?id=l10n/pluralforms
    if ("ru".equals(locale.getLanguage()) || "uk".equals(locale.getLanguage())) {
      //determine plural forms for Russian and Ukranian languages
      return number % 10 == 1 && number % 100 != 11 ? 0 : number % 10 >= 2 && number % 10 <= 4 && (number % 100 < 10 || number % 100 >= 20) ? 1 : 2;
    } else if ("ro".equals(locale.getLanguage()) || "mo".equals(locale.getLanguage())){
      return number == 1 ? 0 : (number == 0 || (number % 100 > 0 && number % 100 < 20)) ? 1 : 2;
    } else {
        //default plural formula (fits for English)
        return number != 1 ? 1 : 0;
      }
  }
}
