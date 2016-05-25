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
import java.util.Locale;

import org.supercsv.cellprocessor.ift.DateCellProcessor;

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
 * <p>
 * If you don't wish to use the default Locale when parsing Dates (your data is formatted for a different Locale), then
 * use the constructor that accepts a Locale.
 * 
 * @author Kasper B. Graversen
 * @author James Bassett
 * @author Pietro Aragona
 */
public class ParseDate extends ParseDateTimeAbstract {
	
	/**
	 * {@inheritDoc}
	 */
	public ParseDate(String dateFormat, boolean lenient, DateCellProcessor next) {
		super(dateFormat, lenient, next);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ParseDate(String dateFormat, boolean lenient, Locale locale, DateCellProcessor next) {
		super(dateFormat, lenient, locale, next);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ParseDate(String dateFormat, boolean lenient, Locale locale) {
		super(dateFormat, lenient, locale);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ParseDate(String dateFormat, boolean lenient) {
		super(dateFormat, lenient);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ParseDate(String dateFormat, DateCellProcessor next) {
		super(dateFormat, next);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ParseDate(String dateFormat) {
		super(dateFormat);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object parseValue(Object value) throws ParseException {
		final Date result = formatter.parse((String) value);
		return result;
	}
	
}
