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

import org.joda.time.format.DateTimeFormatter;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

/**
 * Abstract base class for cell processors converting Strings to Joda types.
 * 
 * @since 2.3.0
 * @author James Bassett
 * @param <T>
 *            the Joda type that the processor returns
 */
public abstract class AbstractJodaParsingProcessor<T> extends
		CellProcessorAdaptor implements StringCellProcessor {

	private final DateTimeFormatter formatter;

	/**
	 * Constructs a new <code>AbstractJodaParsingProcessor</code> processor, which
	 * parses a String as a Joda type.
	 */
	public AbstractJodaParsingProcessor() {
		this.formatter = null;
	}

	/**
	 * Constructs a new <code>AbstractJodaParsingProcessor</code> processor, which
	 * parses a String as a Joda type, then calls the next processor in the
	 * chain.
	 * 
	 * @param next
	 *            the next processor in the chain
	 * @throws NullPointerException
	 *             if next is null
	 */
	public AbstractJodaParsingProcessor(final CellProcessor next) {
		super(next);
		this.formatter = null;
	}

	/**
	 * Constructs a new <code>AbstractJodaParsingProcessor</code> processor, which
	 * parses a String as a Joda type using the supplied formatter.
	 * 
	 * @param formatter
	 *            the formatter used for parsing
	 * @throws NullPointerException
	 *             if formatter is null
	 */
	public AbstractJodaParsingProcessor(final DateTimeFormatter formatter) {
		checkPreconditions(formatter);
		this.formatter = formatter;
	}

	/**
	 * Constructs a new <code>AbstractJodaParsingProcessor</code> processor, which
	 * parses a String as a Joda type using the supplied formatter, then calls
	 * the next processor in the chain.
	 * 
	 * @param formatter
	 *            the formatter used for parsing
	 * @param next
	 *            the next processor in the chain
	 * @throws NullPointerException
	 *             if formatter or next is null
	 */
	public AbstractJodaParsingProcessor(final DateTimeFormatter formatter,
			final CellProcessor next) {
		super(next);
		checkPreconditions(formatter);
		this.formatter = formatter;
	}

	/**
	 * Checks the preconditions for creating a new AbstractJodaParsingProcessor
	 * processor.
	 * 
	 * @param formatter
	 *            the formatter
	 * @throws NullPointerException
	 *             if formatter is null
	 */
	private static void checkPreconditions(final DateTimeFormatter formatter) {
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
		final T result;
		try {
			if (formatter != null) {
				result = parse(string, formatter);
			} else {
				result = parse(string);
			}
		} catch (IllegalArgumentException e) {
			throw new SuperCsvCellProcessorException("Failed to parse value",
					context, this, e);
		}

		return next.execute(result, context);
	}

	/**
	 * Parses the String into the appropriate Joda type.
	 * 
	 * @param string
	 *            the string to parse
	 * @return the Joda type
	 * @throws IllegalArgumentException
	 *             if the string can't be parsed
	 */
	protected abstract T parse(final String string);

	/**
	 * Parses the String into the appropriate Joda type, using the supplied
	 * formatter.
	 * 
	 * @param string
	 *            the string to parse
	 * @param formatter
	 *            the formatter to use
	 * @return the Joda type
	 * @throws IllegalArgumentException
	 *             if the string can't be parsed
	 */
	protected abstract T parse(final String string,
			final DateTimeFormatter formatter);

}
