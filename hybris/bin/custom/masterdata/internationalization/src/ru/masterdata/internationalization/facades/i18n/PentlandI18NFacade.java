package ru.masterdata.internationalization.facades.i18n;

import java.time.LocalDate;

import de.hybris.platform.commercefacades.i18n.I18NFacade;

/**
 * @author Created by ekaterina.agievich@masterdata.ru on 1/23/2018.
 */
public interface PentlandI18NFacade extends I18NFacade{
  LocalDate getFirstWorkingDayOfMonth();
}
