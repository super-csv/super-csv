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

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.supercsv.cellprocessor.ift.DateCellProcessor;

/**
 * Converts a String to a Time using the {@link SimpleDateFormat} class. If you want to convert from a Time to a String,
 * use the {@link FmtSqlTime} processor.
 * <p>
 * Some example date formats you can use are:<br>
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
public class ParseSqlTime extends ParseDateTimeAbstract {
	
	/**
	 * {@inheritDoc}
	 */
	public ParseSqlTime(String dateFormat, boolean lenient, DateCellProcessor next) {
		super(dateFormat, lenient, next);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ParseSqlTime(String dateFormat, boolean lenient, Locale locale, DateCellProcessor next) {
		super(dateFormat, lenient, locale, next);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ParseSqlTime(String dateFormat, boolean lenient, Locale locale) {
		super(dateFormat, lenient, locale);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ParseSqlTime(String dateFormat, boolean lenient) {
		super(dateFormat, lenient);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ParseSqlTime(String dateFormat, DateCellProcessor next) {
		super(dateFormat, next);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ParseSqlTime(String dateFormat) {
		super(dateFormat);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object parseValue(Object value) throws ParseException {
		final Date date = formatter.parse((String) value);
		final Time result = new Time(date.getTime());
		return result;
	}
	
}
