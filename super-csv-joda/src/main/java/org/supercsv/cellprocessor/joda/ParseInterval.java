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

import org.joda.time.Interval;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

/**
 * Converts a String (in ISO8601 interval format) to a Joda Interval.
 * 
 * @since 2.3.0
 * @author James Bassett
 */
public class ParseInterval extends CellProcessorAdaptor implements StringCellProcessor {

	/**
	 * Constructs a new <tt>ParseInterval</tt> processor, which parses a String
	 * as a Joda Interval.
	 */
	public ParseInterval() {
	}

	/**
	 * Constructs a new <tt>ParseInterval</tt> processor, which parses a String
	 * as a Joda Interval, then calls the next processor in the chain.
	 * 
	 * @param next
	 *            the next processor in the chain
	 */
	public ParseInterval(final CellProcessor next) {
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
		final Interval result;
		try {
			result = Interval.parse((String) value);
		} catch (IllegalArgumentException e) {
			throw new SuperCsvCellProcessorException(
					"Failed to parse value as an Interval", context, this, e);
		}
		return next.execute(result, context);
	}

}
