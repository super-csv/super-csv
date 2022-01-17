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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.supercsv.cellprocessor.ift.DateCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

/**
 * Converts a String to a Date/Time using the {@link SimpleDateFormat} class. If you want to convert from a Date/Time to
 * a String, use the {@link FmtDate}/{@link FmtSqlTime} processor.
 * <p>
 * Some example date formats you can use are:<br>
 * <code>"dd/MM/yyyy"</code> (parses a date formatted as "25/12/2011")<br>
 * <code>"dd-MMM-yy"</code> (parses a date formatted as "25-Dec-11")<br>
 * <code>"yyyy.MM.dd.HH.mm.ss"</code> (parses a date formatted as "2011.12.25.08.36.33"<br>
 * <code>"HH:mm:ss"</code> (formats a time as "05:20:00")<br>
 * <code>"HHmmss"</code> (formats a time as "052000")<br>
 * <code>"HH.mm.ss"</code> (formats a date as "05.20.00"<br>
 * <p>
 * This processor caters for lenient or non-lenient date interpretations (the default is false for constructors without
 * a 'lenient' parameter). See {@link SimpleDateFormat#setLenient(boolean)} for more information.
 * <p>
 * If you don't wish to use the default Locale when parsing Dates (your data is formatted for a different Locale), then
 * use the constructor that accepts a Locale.
 * 
 * @author Pietro Aragona
 * @since 2.4.1
 */
public abstract class ParseDateTimeAbstract extends CellProcessorAdaptor implements StringCellProcessor {
	
	protected final String dateFormat;
	
	protected final boolean lenient;
	
	protected final Locale locale;
	
	protected SimpleDateFormat formatter;
	
	/**
	 * Constructs a new <code>ParseDateTimeAbstract</code> processor which converts a String to a Date/Time using the
	 * supplied date format. This constructor uses non-lenient Date interpretation.
	 * 
	 * @param dateFormat
	 *            the date format to use
	 * @throws NullPointerException
	 *             if dateFormat is null
	 */
	public ParseDateTimeAbstract(final String dateFormat) {
		this(dateFormat, false);
	}
	
	/**
	 * Constructs a new <code>ParseDateTimeAbstract</code> processor which converts a String to a Date/Time using the
	 * supplied date format.
	 * 
	 * @param dateFormat
	 *            the date format to use
	 * @param lenient
	 *            whether date interpretation is lenient
	 * @throws NullPointerException
	 *             if dateFormat is null
	 */
	public ParseDateTimeAbstract(final String dateFormat, final boolean lenient) {
		super();
		checkPreconditions(dateFormat);
		this.dateFormat = dateFormat;
		this.lenient = lenient;
		this.locale = null;
	}
	
	/**
	 * Constructs a new <code>ParseDateTimeAbstract</code> processor which converts a String to a Date/Time using the
	 * supplied date format and Locale.
	 * 
	 * @param dateFormat
	 *            the date format to use
	 * @param lenient
	 *            whether date interpretation is lenient
	 * @param locale
	 *            the Locale used to parse the date
	 * @throws NullPointerException
	 *             if dateFormat or locale is null
	 */
	public ParseDateTimeAbstract(final String dateFormat, final boolean lenient, final Locale locale) {
		super();
		checkPreconditions(dateFormat, locale);
		this.dateFormat = dateFormat;
		this.lenient = lenient;
		this.locale = locale;
	}
	
	/**
	 * Constructs a new <code>ParseDateTimeAbstract</code> processor which converts a String to a Date/Time using the
	 * supplied date format, then calls the next processor in the chain. This constructor uses non-lenient Date
	 * interpretation.
	 * 
	 * @param dateFormat
	 *            the date format to use
	 * @param next
	 *            the next processor in the chain
	 * @throws NullPointerException
	 *             if dateFormat or next is null
	 */
	public ParseDateTimeAbstract(final String dateFormat, final DateCellProcessor next) {
		this(dateFormat, false, next);
	}
	
	/**
	 * Constructs a new <code>ParseDateTimeAbstract</code> processor which converts a String to a Date/Time using the
	 * supplied date format, then calls the next processor in the chain.
	 * 
	 * @param dateFormat
	 *            the date format to use
	 * @param lenient
	 *            whether date interpretation is lenient
	 * @param next
	 *            the next processor in the chain
	 * @throws NullPointerException
	 *             if dateFormat or next is null
	 */
	public ParseDateTimeAbstract(final String dateFormat, final boolean lenient, final DateCellProcessor next) {
		super(next);
		checkPreconditions(dateFormat);
		this.dateFormat = dateFormat;
		this.lenient = lenient;
		this.locale = null;
	}
	
	/**
	 * Constructs a new <code>ParseDateTimeAbstract</code> processor which converts a String to a Date/Time using the
	 * supplied date format and Locale, then calls the next processor in the chain.
	 * 
	 * @param dateFormat
	 *            the date format to use
	 * @param lenient
	 *            whether date interpretation is lenient
	 * @param locale
	 *            the Locale used to parse the date
	 * @param next
	 *            the next processor in the chain
	 * @throws NullPointerException
	 *             if dateFormat, locale, or next is null
	 */
	public ParseDateTimeAbstract(final String dateFormat, final boolean lenient, final Locale locale,
		final DateCellProcessor next) {
		super(next);
		checkPreconditions(dateFormat, locale);
		this.dateFormat = dateFormat;
		this.lenient = lenient;
		this.locale = locale;
	}
	
	/**
	 * Checks the preconditions for creating a new ParseDateTimeAbstract processor with a date format.
	 * 
	 * @param dateFormat
	 *            the date format to use
	 * @throws NullPointerException
	 *             if dateFormat is null
	 */
	private static void checkPreconditions(final String dateFormat) {
		if( dateFormat == null ) {
			throw new NullPointerException("dateFormat should not be null");
		}
	}
	
	/**
	 * Checks the preconditions for creating a new ParseDateTimeAbstract processor with date format and locale.
	 * 
	 * @param dateFormat
	 *            the date format to use
	 * @param locale
	 *            the Locale used to parse the date
	 * @throws NullPointerException
	 *             if dateFormat or locale is null
	 */
	private static void checkPreconditions(final String dateFormat, final Locale locale) {
		if( dateFormat == null ) {
			throw new NullPointerException("dateFormat should not be null");
		} else if( locale == null ) {
			throw new NullPointerException("locale should not be null");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws SuperCsvCellProcessorException
	 *             if value is null, isn't a String, or can't be parsed to a Date
	 */
	public Object execute(final Object value, final CsvContext context) {
		validateInputNotNull(value, context);
		
		if( !(value instanceof String) ) {
			throw new SuperCsvCellProcessorException(String.class, value, context, this);
		}
		
		try {
			if( locale == null ) {
				formatter = new SimpleDateFormat(dateFormat);
			} else {
				formatter = new SimpleDateFormat(dateFormat, locale);
			}
			formatter.setLenient(lenient);
			Object result = parseValue(value);
			return next.execute(result, context);
		}
		catch(final ParseException e) {
			throw new SuperCsvCellProcessorException(String.format("'%s' could not be parsed as a Date", value),
				context, this, e);
		}
	}
	
	/**
	 * @param value
	 *            the value to be formatted
	 * @return A Object (Date/Time) parsed from the string.
	 * @throws ParseException
	 *             if the string cannot be parsed.
	 */
	abstract protected Object parseValue(Object value) throws ParseException;
	
}
