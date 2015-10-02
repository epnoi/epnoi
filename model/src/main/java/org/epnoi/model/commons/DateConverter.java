package org.epnoi.model.commons;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateConverter {

	private static List<SimpleDateFormat> knownPatterns = null;

	// -------------------------------------------------------------------------------------------------

	private static void _init() {
		if (knownPatterns == null) {
			knownPatterns = new ArrayList<SimpleDateFormat>();

			knownPatterns.add(new SimpleDateFormat(
					"EEE, dd MMM yyyy HH:mm:ss zzzz", Locale.ENGLISH));

			knownPatterns
					.add(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH));
			knownPatterns.add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",
					Locale.ENGLISH));
			knownPatterns.add(new SimpleDateFormat(
					"EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH));
		}
	}

	// -------------------------------------------------------------------------------------------------

	public static String convertDateFormat(String dateExpression) {

		_init();
		for (SimpleDateFormat pattern : knownPatterns) {
			try {
				// Take a try
				Date parsedDate = pattern.parse(dateExpression);
				SimpleDateFormat dt1 = new SimpleDateFormat(
						"yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
				return (dt1.format(parsedDate));
			} catch (ParseException pe) {
				// Loop on
			}
		}
		System.err.println("No known Date format found: " + dateExpression);
		return null;

	}
}
