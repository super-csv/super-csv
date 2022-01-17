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

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

/**
 * Converts a Duration to a String.
 *
 * For example, "PT1M12.345S" represents 1 minute, 12 seconds and 345
 * milliseconds.
 *
 * @author Ludovico Fischer
 * @since 2.4.0
 */
public class FmtDuration extends CellProcessorAdaptor {

	/**
	 * Constructs a new <code>FmtDuration</code> processor, which formats a
	 * Duration as a String in the ISO 8601 duration format,
	 * in the same way as {@link Duration#toString()}
	 *
	 * @see Duration#toString()
	 */
	public FmtDuration() {
	}

	/**
	 * Constructs a new <code>FmtDuration</code> processor, which formats a
	 * Duration as a String, then calls the next processor in the chain.
	 *
	 * @param next next processor in the chain
	 * @throws NullPointerException if next is null
	 */
	public FmtDuration(final CellProcessor next) {
		super(next);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws SuperCsvCellProcessorException if value is null or not a Duration
	 */
	public Object execute(final Object value, final CsvContext context) {
		validateInputNotNull(value, context);
		if( !(value instanceof Duration) ) {
			throw new SuperCsvCellProcessorException(Duration.class, value, context, this);
		}
		final Duration duration = (Duration) value;
		final String result = duration.toString();
		return next.execute(result, context);
	}
}
