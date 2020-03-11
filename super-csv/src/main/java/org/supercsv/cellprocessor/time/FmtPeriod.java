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

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

/**
 * Converts a Period to a String.
 * Converts to a String in the ISO 8601 format,
 * in the same way as {@link Period#toString()}.
 * For example, "P6Y3M7D" represents 6 years, 3 months, 7 days.
 *
 * @author Ludovico Fischer
 * @since 2.4.0
 */
public class FmtPeriod extends CellProcessorAdaptor {

	/**
	 * Constructs a new <code>FmtPeriod</code> processor, which formats a
	 * Period as a String.
	 */
	public FmtPeriod() {
		super();
	}

	/**
	 * Constructs a new <code>FmtPeriod</code> processor, which formats a
	 * Period as a String, then calls the next processor in the chain.
	 *
	 * @param next the next processor in the chain
	 * @throws NullPointerException if formatter or next is null
	 */
	public FmtPeriod(final CellProcessor next) {
		super(next);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws SuperCsvCellProcessorException if value is null or not a Period
	 */
	public Object execute(final Object value, final CsvContext context) {
		validateInputNotNull(value, context);
		if( !(value instanceof Period) ) {
			throw new SuperCsvCellProcessorException(Period.class, value, context, this);
		}
		final Period period = (Period) value;
		final String result = period.toString();

		return next.execute(result, context);
	}

}
