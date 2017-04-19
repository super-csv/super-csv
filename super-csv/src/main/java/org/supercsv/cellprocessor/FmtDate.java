/*
 * Copyright 2007 Kasper B. Graversen
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.supercsv.cellprocessor;

import org.supercsv.cellprocessor.ift.DateCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Converts a date into a formatted string using the {@link SimpleDateFormat} class. If you want to convert from a
 * String to a Date, use the {@link ParseDate} processor.
 * <p>
 * Some example date formats you can use are:<br>
 * <code>"dd/MM/yyyy"</code> (formats a date as "25/12/2011")<br>
 * <code>"dd-MMM-yy"</code> (formats a date as "25-Dec-11")<br>
 * <code>"yyyy.MM.dd.HH.mm.ss"</code> (formats a date as "2011.12.25.08.36.33"<br>
 * <code>"E, dd MMM yyyy HH:mm:ss Z"</code> (formats a date as "Tue, 25 Dec 2011 08:36:33 -0500")<br>
 *
 * @author Dominique De Vito
 * @author James Bassett
 * @since 1.50
 */
public class FmtDate extends CellProcessorAdaptor implements DateCellProcessor {

	/**
	 * format to apply on date
	 */
	private final String dateFormat;

	/**
	 * Locale TimeZone
	 */
	private Locale locale = null;

	/**
	 * Constructs a new <tt>FmtDate</tt> processor, which converts a date into a formatted string using
	 * SimpleDateFormat.
	 *
	 * @param dateFormat the date format String (see {@link SimpleDateFormat})
	 * @throws NullPointerException if dateFormat is null
	 */
	public FmtDate(final String dateFormat) {
		this(dateFormat, (Locale) null);
	}

	/**
	 * Constructs a new <tt>FmtDate</tt> processor, which converts a date into a formatted string using
	 * SimpleDateFormat, then calls the next processor in the chain.
	 *
	 * @param dateFormat the date format String (see {@link SimpleDateFormat})
	 * @param next       the next processor in the chain
	 * @throws NullPointerException if dateFormat or next is null
	 */
	public FmtDate(final String dateFormat, final StringCellProcessor next) {
		this(dateFormat, null, next);
	}

	/**
	 * Constructs a new <tt>FmtDate</tt> processor, which converts a date into a formatted string using
	 * SimpleDateFormat and the given Locale
	 *
	 * @param dateFormat the date format String (see {@link SimpleDateFormat})
	 * @param locale     the locale to use when format date (see {@link SimpleDateFormat})
	 * @throws NullPointerException if dateFormat is null
	 */
	public FmtDate(final String dateFormat, final Locale locale) {
		super();
		checkPreconditions(dateFormat);
		this.dateFormat = dateFormat;
		this.locale = locale;
	}

	/**
	 * Constructs a new <tt>FmtDate</tt> processor, which converts a date into a formatted string using
	 * SimpleDateFormat and the given Locale, then calls the next processor in the chain.
	 *
	 * @param dateFormat the date format String (see {@link SimpleDateFormat})
	 * @param locale     the locale to use when format date (see {@link SimpleDateFormat})
	 * @param next       the next processor in the chain
	 * @throws NullPointerException if dateFormat or next is null
	 */
	public FmtDate(final String dateFormat, final Locale locale, final StringCellProcessor next) {
		super(next);
		checkPreconditions(dateFormat);
		this.dateFormat = dateFormat;
		this.locale = locale;
	}

	/**
	 * Checks the preconditions for creating a new FmtDate processor.
	 *
	 * @param dateFormat the date format String
	 * @throws NullPointerException if dateFormat is null
	 */
	private static void checkPreconditions(final String dateFormat) {
		if (dateFormat == null) {
			throw new NullPointerException("dateFormat should not be null");
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws SuperCsvCellProcessorException if value is null or is not a Date, or if dateFormat is not a valid date format
	 */
	public Object execute(final Object value, final CsvContext context) {
		validateInputNotNull(value, context);

		if (!(value instanceof Date)) {
			throw new SuperCsvCellProcessorException(Date.class, value, context, this);
		}

		final SimpleDateFormat formatter;
		try {
			if (locale == null) {
				formatter = new SimpleDateFormat(dateFormat);
			} else {
				formatter = new SimpleDateFormat(dateFormat, locale);
			}
		} catch (IllegalArgumentException e) {
			throw new SuperCsvCellProcessorException(String.format("'%s' is not a valid date format", dateFormat),
					context, this, e);
		}

		String result = formatter.format((Date) value);
		return next.execute(result, context);
	}
}
