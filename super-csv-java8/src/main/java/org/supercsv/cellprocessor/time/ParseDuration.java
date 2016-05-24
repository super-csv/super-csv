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

import java.time.Duration;
import java.time.format.DateTimeParseException;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.i18n.SuperCsvMessages;
import org.supercsv.util.CsvContext;

/**
 * Converts a String to a Duration.
 * The String should be in the ISO 8601 duration format as defined in
 * {@link Duration#parse(CharSequence)}
 * For example, "PT1M12.345S" represents 1 minute, 12 seconds and 345
 * milliseconds.
 *
 * @author Ludovico Fischer
 * @since 2.4.0
 */
public class ParseDuration extends CellProcessorAdaptor {

	/**
	 * Constructs a new <tt>ParseDuration</tt> processor, which parses a String
	 * as a Duration.
	 */
	public ParseDuration() {
	}

	/**
	 * Constructs a new <tt>ParseDuration</tt> processor, which parses a String
	 * as a Duration, then calls the next processor in the chain.
	 *
	 * @param next the next processor in the chain
	 */
	public ParseDuration(final CellProcessor next) {
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
		final Duration result;
		try {
			result = Duration.parse((String) value);
		}
		catch(DateTimeParseException e) {
			throw new SuperCsvCellProcessorException(SuperCsvMessages.getMessage("org.supercsv.exception.cellprocessor.jdk8.InvalidDuration.message"), context, this, e);
		}
		return next.execute(result, context);
	}

}
