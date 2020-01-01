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
 * Converts a Joda Duration to a String in the ISO8601 duration format including
 * only seconds and milliseconds.
 * <p>
 * For example, "PT72.345S" represents 1 minute, 12 seconds and 345
 * milliseconds.
 * <p>
 * 
 * @since 2.3.0
 * @author James Bassett
 */
public class FmtDuration extends CellProcessorAdaptor {

	/**
	 * Constructs a new <code>FmtDuration</code> processor, which formats a Joda
	 * Duration as a String.
	 */
	public FmtDuration() {
	}

	/**
	 * Constructs a new <code>FmtDuration</code> processor, which formats a Joda
	 * Duration as a String, then calls the next processor in the chain.
	 * 
	 * @param next
	 *            next processor in the chain
	 * @throws NullPointerException
	 *             if next is null
	 */
	public FmtDuration(final CellProcessor next) {
		super(next);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws SuperCsvCellProcessorException
	 *             if value is null or not a Duration
	 */
	public Object execute(final Object value, final CsvContext context) {
		validateInputNotNull(value, context);
		if (!(value instanceof Duration)) {
			throw new SuperCsvCellProcessorException(Duration.class, value,
					context, this);
		}
		final Duration duration = (Duration) value;
		final String result = duration.toString();
		return next.execute(result, context);
	}

}
