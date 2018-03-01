package ru.masterdata.internationalization.facades.i18n.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.formula.functions.Now;
import org.springframework.beans.factory.annotation.Required;

import ru.masterdata.internationalization.facades.i18n.PentlandI18NFacade;

import de.hybris.platform.commercefacades.i18n.impl.DefaultI18NFacade;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import ru.masterdata.internationalization.model.LocalizationEntryModel;
import ru.masterdata.internationalization.services.LocalizationEntryService;

/**
 * @author Created by ekaterina.agievich@masterdata.ru on 1/23/2018.
 */
public class DefaultPentlandI18NFacade extends DefaultI18NFacade implements PentlandI18NFacade{

  private static final Logger LOG = Logger.getLogger(DefaultPentlandI18NFacade.class);

  private static final String DATE_FORMAT = "yyyy-MM-dd";
  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
  private static final String BANK_HOLIDAYS_KEY = "text.cart.bankHolidays";
  private static final int deliveryDays = 1;

  private LocalizationEntryService localizationEntryService;

  @Override
  public LocalDate getFirstWorkingDayOfMonth() {
    Set<LocalDate> ukHolidays = collectUKHolidays(formatter);

    LocalDate currentDate = LocalDate.now();
    LocalDate firstDayOfMonth = currentDate.with(TemporalAdjusters.firstDayOfMonth());
    TemporalAdjuster firstWorkingDayAdjuster = TemporalAdjusters.ofDateAdjuster(localDate -> {
      DayOfWeek dayOfWeek = localDate.getDayOfWeek();
      LocalDate newDate = localDate;
      while(ukHolidays.contains(newDate) || DayOfWeek.SATURDAY.equals(dayOfWeek) || DayOfWeek.SUNDAY.equals(dayOfWeek)) {
        if(ukHolidays.contains(newDate) || DayOfWeek.SUNDAY.equals(dayOfWeek)){
          newDate = newDate.plusDays(1);
        }else if (DayOfWeek.SATURDAY.equals(dayOfWeek)) {
          newDate = newDate.plusDays(2);
        }
        dayOfWeek = newDate.getDayOfWeek();
      }
      return newDate;
    });

    return firstDayOfMonth.with(firstWorkingDayAdjuster);
  }

  @Override
  public LocalDate getNextAvailableRDD() {
    Set<LocalDate> ukHolidays = collectUKHolidays(formatter);
    LocalDate currentDate = LocalDate.now();

    TemporalAdjuster skipDeliveryDaysAdjuster = TemporalAdjusters.ofDateAdjuster(localDate -> {
			int daysToSkip = deliveryDays;
			if (LocalTime.now().isAfter(LocalTime.of(15, 0, 0))) {
				daysToSkip++;
			}
			while (daysToSkip >= 0) {
				if (ukHolidays.contains(localDate) || DayOfWeek.SUNDAY.equals(localDate.getDayOfWeek())) {
					localDate = localDate.plusDays(1);
				} else if (DayOfWeek.SATURDAY.equals(localDate.getDayOfWeek())) {
					localDate = localDate.plusDays(2);
				} else {
					localDate = localDate.plusDays(1);
					daysToSkip--;
				}

			}

			return localDate;
		});

		return currentDate.with(skipDeliveryDaysAdjuster);

	}

  private Set<LocalDate> collectUKHolidays(DateTimeFormatter formatter) {
    Set<LocalDate> holidays = new HashSet<>();
    try {
      LocalizationEntryModel bankHolidays = localizationEntryService.getLocalizationEntry(BANK_HOLIDAYS_KEY);
      String holidayString = bankHolidays.getLocalizedText(Locale.ENGLISH);
      if(StringUtils.isNotEmpty(holidayString)){
        for(String holiday: holidayString.split(",")){
          try {
            LocalDate holidayDate = LocalDate.parse(holiday, formatter);
            holidays.add(holidayDate);
          }catch(DateTimeParseException e){
            LOG.debug("Holidays are in wrong format");
          }
        }
      }
    }catch(ModelNotFoundException e){
      LOG.debug("No holidays specified, so they will not be taken into account");
    }
    return holidays;
  }

  @Required
  public void setLocalizationEntryService(LocalizationEntryService localizationEntryService) {
    this.localizationEntryService = localizationEntryService;
  }
}
