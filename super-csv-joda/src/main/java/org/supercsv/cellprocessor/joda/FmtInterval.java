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
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

/**
 * Converts a Joda Interval to a String in ISO8601 interval format.
 * 
 * @since 2.3.0
 * @author James Bassett
 */
public class FmtInterval extends CellProcessorAdaptor {

	/**
	 * Constructs a new <code>FmtInterval</code> processor, which formats a Joda
	 * Interval as a String.
	 */
	public FmtInterval() {
	}

	/**
	 * Constructs a new <code>FmtInterval</code> processor, which formats a Joda
	 * Interval as a String, then calls the next processor in the chain.
	 * 
	 * @param next
	 *            next processor in the chain
	 * @throws NullPointerException
	 *             if next is null
	 */
	public FmtInterval(final CellProcessor next) {
		super(next);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws SuperCsvCellProcessorException
	 *             if value is null or not a Interval
	 */
	public Object execute(final Object value, final CsvContext context) {
		validateInputNotNull(value, context);
		if (!(value instanceof Interval)) {
			throw new SuperCsvCellProcessorException(Interval.class, value,
					context, this);
		}
		final Interval interval = (Interval) value;
		final String result = interval.toString();
		return next.execute(result, context);
	}

}
