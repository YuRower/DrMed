package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;

import org.apache.log4j.Logger;


public class DateUtil {
	static Locale currentLocale = Locale.getDefault();
	static DateTimeFormatter VALIDATION = DateTimeFormatter.ofPattern("yyyy[-MM[-dd]]");

	static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", currentLocale);
	private final static Logger LOGGER = Logger.getLogger(DateUtil.class);

	public static String format(LocalDate string) {
		LOGGER.debug(string + " will be formated ");
		if (string == null) {
			return null;
		}
		LOGGER.debug(String.valueOf(formatter.format(string)) + " string was formatted ");

		return formatter.format(string);
	}

	public static LocalDate parse(String dateString) {
		try {
			LOGGER.debug(dateString + " will be parsed ");
			LOGGER.debug(LocalDate.parse(dateString, formatter));
			return LocalDate.parse(dateString, formatter);
		} catch (DateTimeParseException e) {
			LOGGER.error(e);
			return null;
		}
	}

static TemporalAccessor parseDate(String dateAsString) {
		return VALIDATION.parseBest(dateAsString, LocalDate::from, LocalDate::from);
	}

	public static boolean validDate(String dateAsString) {
		try {
			parseDate(dateAsString);
			LOGGER.debug("Valid Date" + dateAsString);
			return true;
		} catch (DateTimeParseException e) {
			return false;
		}
	}
}