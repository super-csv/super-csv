package org.supercsv.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Helper methods for creating and manipulating Dates, Calendars etc..
 * 
 * @since 0.2
 * @author Kasper B. Graversen, (c) 2007
 */
public class DateHelper {
	
	/**
	 * An easy and non-depricated non-lenient way of creating Date objects.
	 * 
	 * @param year
	 *            the year, e.g. 2007 is year 2007
	 * @param month
	 *            the month, where 1 == January
	 * @param dayOfMonth
	 *            the day of the month, where 1 == first day of the month
	 * @return a Date object with time set to midnight, ie. hour = 00, minutes = 00, seconds = 00 and milis = 000
	 * @since 0.02
	 */
	public static Date date(final int year, final int month, final int dayOfMonth) {
		final Calendar cal = Calendar.getInstance();
		cal.setLenient(false);
		cal.set(year, month - 1, dayOfMonth, 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	/**
	 * An easy and non-depricated non-lenient way of creating Date objects.
	 * 
	 * @param year
	 *            the year, e.g. 2007 is year 2007
	 * @param month
	 *            the month, where 1 == January
	 * @param dayOfMonth
	 *            the day of the month, where 1 == first day of the month
	 * @param hour
	 *            the hour in 24 hour format where 0 == midnight
	 * @param minute
	 *            is the minute 0-59
	 * @param second
	 *            is the seconds 0-59
	 * @return a Date object with time set to midnight, ie. hour = 00, minutes = 00, seconds = 00 and milis = 000
	 * @since 0.02
	 */
	public static Date date(final int year, final int month, final int dayOfMonth, final int hour, final int minute,
			final int second) {
		final Calendar cal = Calendar.getInstance();
		cal.setLenient(false);
		cal.set(year, month - 1, dayOfMonth, hour, minute, second);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
}
