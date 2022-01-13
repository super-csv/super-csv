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
import java.text.SimpleDateFormat;

import org.supercsv.cellprocessor.ift.DateCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

/**
 * Converts a time into a formatted string using the {@link SimpleDateFormat} class. If you want to convert from a
 * String to a Time, use the {@link ParseSqlTime} processor.
 * <p>
 * Some example time formats you can use are:<br>
 * <code>"HH:mm:ss"</code> (formats a time as "05:20:00")<br>
 * <code>"HHmmss"</code> (formats a time as "052000")<br>
 * <code>"HH.mm.ss"</code> (formats a date as "05.20.00"<br>
 * 
 * @author Pietro Aragona
 * @since 2.4.1
 */
public class FmtSqlTime extends CellProcessorAdaptor implements DateCellProcessor {
	
	private final String dateFormat;
	
	/**
	 * Constructs a new <code>FmtTime</code> processor, which converts a time into a formatted string using
	 * SimpleDateFormat.
	 * 
	 * @param dateFormat
	 *            the date format String (see {@link SimpleDateFormat})
	 * @throws NullPointerException
	 *             if dateFormat is null
	 */
	public FmtSqlTime(final String dateFormat) {
		super();
		checkPreconditions(dateFormat);
		this.dateFormat = dateFormat;
	}
	
	/**
	 * Constructs a new <code>FmtTime</code> processor, which converts a time into a formatted string using
	 * SimpleDateFormat, then calls the next processor in the chain.
	 * 
	 * @param dateFormat
	 *            the time format String (see {@link SimpleDateFormat})
	 * @param next
	 *            the next processor in the chain
	 * @throws NullPointerException
	 *             if dateFormat or next is null
	 */
	public FmtSqlTime(final String dateFormat, final StringCellProcessor next) {
		super(next);
		checkPreconditions(dateFormat);
		this.dateFormat = dateFormat;
	}
	
	/**
	 * Checks the preconditions for creating a new FmtTime processor.
	 * 
	 * @param dateFormat
	 *            the date format String
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
	 * @throws SuperCsvCellProcessorException
	 *             if value is null or is not a Time, or if dateFormat is not a valid date format
	 */
	public Object execute(final Object value, final CsvContext context) {
		validateInputNotNull(value, context);
		
		if( !(value instanceof Time) ) {
			throw new SuperCsvCellProcessorException(Time.class, value, context, this);
		}
		
		final SimpleDateFormat formatter;
		try {
			formatter = new SimpleDateFormat(dateFormat);
		}
		catch(IllegalArgumentException e) {
			throw new SuperCsvCellProcessorException(String.format("'%s' is not a valid date format", dateFormat),
				context, this, e);
		}
		
		String result = formatter.format((Time) value);
		return next.execute(result, context);
	}
}
