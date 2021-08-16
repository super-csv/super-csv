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
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

/**
 * Converts a String to a Joda Period.
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
 * By default, converts from Strings in the ISO8601 duration format.
 * <p>
 * For example, "PT6H3M7S" represents 6 hours, 3 minutes, 7 seconds.
 * <p>
 * 
 * @since 2.3.0
 * @author James Bassett
 */
public class ParsePeriod extends CellProcessorAdaptor implements StringCellProcessor {

	private final PeriodFormatter formatter;

	/**
	 * Constructs a new <tt>ParsePeriod</tt> processor, which parses a String as
	 * a Joda Period.
	 */
	public ParsePeriod() {
		this.formatter = null;
	}

	/**
	 * Constructs a new <tt>ParsePeriod</tt> processor, which parses a String as
	 * a Joda Period, then calls the next processor in the chain.
	 * 
	 * @param next
	 *            the next processor in the chain
	 * @throws NullPointerException
	 *             if next is null
	 */
	public ParsePeriod(final CellProcessor next) {
		super(next);
		this.formatter = null;
	}

	/**
	 * Constructs a new <tt>ParsePeriod</tt> processor, which parses a String as
	 * a Joda Period using the supplied formatter.
	 * 
	 * @param formatter
	 *            the formatter used for parsing
	 * @throws NullPointerException
	 *             if formatter is null
	 */
	public ParsePeriod(final PeriodFormatter formatter) {
		checkPreconditions(formatter);
		this.formatter = formatter;
	}

	/**
	 * Constructs a new <tt>ParsePeriod</tt> processor, which parses a String as
	 * a Joda Period using the supplied formatter, then calls the next processor
	 * in the chain.
	 * 
	 * @param formatter
	 *            the formatter used for parsing
	 * @param next
	 *            the next processor in the chain
	 * @throws NullPointerException
	 *             if formatter or next is null
	 */
	public ParsePeriod(final PeriodFormatter formatter, final CellProcessor next) {
		super(next);
		checkPreconditions(formatter);
		this.formatter = formatter;
	}

	/**
	 * Checks the preconditions for creating a new ParsePeriod processor.
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
	 *             if value is null or is not a String
	 */
	public Object execute(final Object value, final CsvContext context) {
		validateInputNotNull(value, context);
		if (!(value instanceof String)) {
			throw new SuperCsvCellProcessorException(String.class, value,
					context, this);
		}

		final String string = (String) value;
		final Period result;

		try {
			if (formatter != null) {
				result = Period.parse(string, formatter);
			} else {
				result = Period.parse(string);
			}
		} catch (IllegalArgumentException e) {
			throw new SuperCsvCellProcessorException(
					"Failed to parse value as a Period", context, this, e);
		}

		return next.execute(result, context);
	}

}
