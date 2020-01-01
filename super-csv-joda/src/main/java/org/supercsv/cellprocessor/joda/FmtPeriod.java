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

import org.joda.time.Period;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

/**
 * Converts a Joda Period to a String.
 * 
 * <p>
 * For constructors using PeriodFormatter, refer to the following Joda classes:
 * <ul>
 * <li>{@link PeriodFormat} - formats by pattern and style</li>
 * <li>{@link ISOPeriodFormat} - ISO8601 formats</li>
 * <li>{@link PeriodFormatterBuilder} - complex formats created via method calls
 * </li>
 * </ul>
 * <p>
 * By default, converts to a String in the ISO8601 duration format.
 * <p>
 * For example, "PT6H3M7S" represents 6 hours, 3 minutes, 7 seconds.
 * <p>
 * 
 * @since 2.3.0
 * @author James Bassett
 */
public class FmtPeriod extends CellProcessorAdaptor {

	private final PeriodFormatter formatter;

	/**
	 * Constructs a new <code>FmtPeriod</code> processor, which formats a Joda
	 * Period as a String.
	 */
	public FmtPeriod() {
		formatter = null;
	}

	/**
	 * Constructs a new <code>FmtPeriod</code> processor, which formats a Joda
	 * Period as a String, then calls the next processor in the chain.
	 * 
	 * @param next
	 *            the next processor in the chain
	 * @throws NullPointerException
	 *             if formatter or next is null
	 */
	public FmtPeriod(final CellProcessor next) {
		super(next);
		this.formatter = null;
	}

	/**
	 * Constructs a new <code>FmtPeriod</code> processor, which formats a Joda
	 * Period as a String using the supplied formatter.
	 * 
	 * @param formatter
	 *            the formatter to use
	 * @throws NullPointerException
	 *             if formatter is null
	 */
	public FmtPeriod(final PeriodFormatter formatter) {
		checkPreconditions(formatter);
		this.formatter = formatter;
	}

	/**
	 * Constructs a new <code>FmtPeriod</code> processor, which formats a Joda
	 * Period as a String using the supplied formatter, then calls the next
	 * processor in the chain.
	 * 
	 * @param formatter
	 *            the formatter to use
	 * @param next
	 *            the next processor in the chain
	 * @throws NullPointerException
	 *             if formatter or next is null
	 */
	public FmtPeriod(final PeriodFormatter formatter, final CellProcessor next) {
		super(next);
		checkPreconditions(formatter);
		this.formatter = formatter;
	}

	/**
	 * Checks the preconditions for creating a new FmtPeriod processor.
	 * 
	 * @param formatter
	 *            the formatter
	 * @throws NullPointerException
	 *             if formatter is null
	 */
	private static void checkPreconditions(final PeriodFormatter formatter) {
		if (formatter == null) {
			throw new NullPointerException("formatter should not be null");
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws SuperCsvCellProcessorException
	 *             if value is null or not a Period
	 */
	public Object execute(final Object value, final CsvContext context) {
		validateInputNotNull(value, context);
		if (!(value instanceof Period)) {
			throw new SuperCsvCellProcessorException(Period.class, value,
					context, this);
		}
		final Period period = (Period) value;
		final String result;
		if (formatter != null) {
			result = period.toString(formatter);
		} else {
			result = period.toString();
		}

		return next.execute(result, context);
	}

}
