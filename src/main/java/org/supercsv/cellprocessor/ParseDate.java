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
import java.util.Date;

import org.supercsv.cellprocessor.ift.DateCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.ClassCastInputCSVException;
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CsvContext;

/**
 * Converts a String to a Date using the {@link SimpleDateFormat} class. If you want to convert from a Date to a String,
 * use the {@link FmtDate} processor.
 * <p>
 * Some example date formats you can use are:<br>
 * <code>"dd/MM/yyyy"</code> (parses a date formatted as "25/12/2011")<br>
 * <code>"dd-MMM-yy"</code> (parses a date formatted as "25-Dec-11")<br>
 * <code>"yyyy.MM.dd.HH.mm.ss"</code> (parses a date formatted as "2011.12.25.08.36.33"<br>
 * <code>"E, dd MMM yyyy HH:mm:ss Z"</code> (parses a date formatted as "Tue, 25 Dec 2011 08:36:33 -0500")<br>
 * <p>
 * This processor caters for lenient or non-lenient date interpretations (the default is false for constructors without
 * a 'lenient' parameter). See {@link SimpleDateFormat#setLenient(boolean)} for more information.
 * 
 * @author Kasper B. Graversen
 * @author James Bassett
 */
public class ParseDate extends CellProcessorAdaptor implements StringCellProcessor {
	
	private final String dateFormat;
	
	private final boolean lenient;
	
	/**
	 * Constructs a new <tt>ParseDate</tt> processor which converts a String to a Date using the supplied date format.
	 * This constructor uses non-lenient Date interpretation.
	 * 
	 * @param dateFormat
	 *            the date format to use
	 * @throws NullPointerException
	 *             if dateFormat is null
	 */
	public ParseDate(final String dateFormat) {
		this(dateFormat, false);
	}
	
	/**
	 * Constructs a new <tt>ParseDate</tt> processor which converts a String to a Date using the supplied date format.
	 * 
	 * @param dateFormat
	 *            the date format to use
	 * @param lenient
	 *            whether date interpretation is lenient
	 * @throws NullPointerException
	 *             if dateFormat is null
	 */
	public ParseDate(final String dateFormat, final boolean lenient) {
		super();
		checkPreconditions(dateFormat);
		this.dateFormat = dateFormat;
		this.lenient = lenient;
	}
	
	/**
	 * Constructs a new <tt>ParseDate</tt> processor which converts a String to a Date using the supplied date format,
	 * then calls the next processor in the chain. This constructor uses non-lenient Date interpretation.
	 * 
	 * @param dateFormat
	 *            the date format to use
	 * @param next
	 *            the next processor in the chain
	 * @throws NullPointerException
	 *             if dateFormat or next is null
	 */
	public ParseDate(final String dateFormat, final DateCellProcessor next) {
		this(dateFormat, false, next);
	}
	
	/**
	 * Constructs a new <tt>ParseDate</tt> processor which converts a String to a Date using the supplied date format,
	 * then calls the next processor in the chain.
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
	public ParseDate(final String dateFormat, final boolean lenient, final DateCellProcessor next) {
		super(next);
		checkPreconditions(dateFormat);
		this.dateFormat = dateFormat;
		this.lenient = lenient;
	}
	
	/**
	 * Checks the preconditions for creating a new ParseDate processor.
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
	 * {@inheritDoc}
	 * 
	 * @throws ClassCastInputCSVException
	 *             if value isn't a String
	 * @throws NullInputException
	 *             if value is null
	 * @throws SuperCSVException
	 *             if value can't be parsed to a Date
	 */
	public Object execute(final Object value, final CsvContext context) {
		validateInputNotNull(value, context);
		
		if( !(value instanceof String) ) {
			throw new ClassCastInputCSVException(value, String.class, context, this);
		}
		
		try {
			final SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
			formatter.setLenient(lenient);
			final Date result = formatter.parse((String) value);
			return next.execute(result, context);
		}
		catch(final ParseException e) {
			throw new SuperCSVException(String.format("'%s' could not be parsed as a Date", value), context, this, e);
		}
	}
}
