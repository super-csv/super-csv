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
package org.supercsv.cellprocessor.time;

import java.time.Period;
import java.time.format.DateTimeParseException;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

/**
 * Converts a String to a Period.
 * The input String must conform to the ISO 8601 period format,
 * recognised by {@link Period#parse(CharSequence)}.
 * For example, "P6Y3M7D" represents 6 years, 3 months, 7 days.
 *
 * @author Ludovico Fischer
 * @since 2.4.0
 */
public class ParsePeriod extends CellProcessorAdaptor implements StringCellProcessor {

	/**
	 * Constructs a new <code>ParsePeriod</code> processor, which parses a String as
	 * a Period.
	 */
	public ParsePeriod() {
		super();
	}

	/**
	 * Constructs a new <code>ParsePeriod</code> processor, which parses a String as
	 * a Period, then calls the next processor in the chain.
	 *
	 * @param next the next processor in the chain
	 * @throws NullPointerException if next is null
	 */
	public ParsePeriod(final CellProcessor next) {
		super(next);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws SuperCsvCellProcessorException if value is null or is not a String
	 */
	public Object execute(final Object value, final CsvContext context) {
		validateInputNotNull(value, context);
		if( !(value instanceof String) ) {
			throw new SuperCsvCellProcessorException(String.class, value, context, this);
		}

		final String string = (String) value;
		final Period result;

		try {
			result = Period.parse(string);
		}
		catch(DateTimeParseException e) {
			throw new SuperCsvCellProcessorException("Failed to parse value as a Period", context, this, e);
		}

		return next.execute(result, context);
	}

}
