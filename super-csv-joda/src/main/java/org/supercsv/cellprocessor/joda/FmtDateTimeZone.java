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
package org.supercsv.cellprocessor.joda;

import org.joda.time.DateTimeZone;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

/**
 * Converts a Joda DateTimeZone to a String (the ID of the timezone, e.g.
 * 'Australia/Brisbane').
 * 
 * @since 2.3.0
 * @author James Bassett
 */
public class FmtDateTimeZone extends CellProcessorAdaptor {

	/**
	 * Constructs a new <tt>FmtDateTimeZone</tt> processor, which formats a Joda
	 * DateTimeZone as a String.
	 */
	public FmtDateTimeZone() {
	}

	/**
	 * Constructs a new <tt>FmtDateTimeZone</tt> processor, which formats a Joda
	 * DateTimeZone as a String, then calls the next processor in the chain.
	 * 
	 * @param next
	 *            next processor in the chain
	 * @throws NullPointerException
	 *             if next is null
	 */
	public FmtDateTimeZone(final CellProcessor next) {
		super(next);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws SuperCsvCellProcessorException
	 *             if value is null or not a DateTimeZone
	 */
	public Object execute(final Object value, final CsvContext context) {
		validateInputNotNull(value, context);
		if (!(value instanceof DateTimeZone)) {
			throw new SuperCsvCellProcessorException(DateTimeZone.class, value,
					context, this);
		}
		final DateTimeZone dateTimeZone = (DateTimeZone) value;
		final String result = dateTimeZone.toString();
		return next.execute(result, context);
	}

}
