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

import org.joda.time.Duration;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

/**
 * Converts a String to a Joda Duration.
 * 
 * The String should be in the ISO8601 duration format including only seconds
 * and milliseconds.
 * <p>
 * For example, "PT72.345S" represents 1 minute, 12 seconds and 345
 * milliseconds.
 * <p>
 * 
 * @since 2.3.0
 * @author James Bassett
 */
public class ParseDuration extends CellProcessorAdaptor {

	/**
	 * Constructs a new <tt>ParseDuration</tt> processor, which parses a String
	 * as a Joda Duration.
	 */
	public ParseDuration() {
	}

	/**
	 * Constructs a new <tt>ParseDuration</tt> processor, which parses a String
	 * as a Joda Duration, then calls the next processor in the chain.
	 * 
	 * @param next
	 *            the next processor in the chain
	 */
	public ParseDuration(final CellProcessor next) {
		super(next);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws SuperCsvCellProcessorException
	 *             if value is null or is not a String
	 */
	public Object execute(final Object value, final CsvContext context) {
		validateInputNotNull(value, context);
		if (!(value instanceof String)) {
			throw new SuperCsvCellProcessorException(String.class, value,
					context, this);
		}
		final Duration result;
		try {
			result = Duration.parse((String) value);
		} catch (IllegalArgumentException e) {
			throw new SuperCsvCellProcessorException(
					"Failed to parse value as a Duration", context, this, e);
		}
		return next.execute(result, context);
	}

}
