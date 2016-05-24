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

import java.time.DateTimeException;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Objects;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.i18n.SuperCsvMessages;
import org.supercsv.util.CsvContext;

/**
 * Abstract base class for cell processors converting {@link TemporalAccessor} types to Strings.
 *
 * @author Ludovico Fischer
 * @since 2.4.0
 */
public abstract class AbstractTemporalAccessorFormattingProcessor<T extends TemporalAccessor>
	extends CellProcessorAdaptor {

	private final DateTimeFormatter formatter;

	/**
	 * Constructs a new <tt>AbstractTemporalAccessorFormattingProcessor</tt> processor,
	 * which formats the type as a String.
	 */
	public AbstractTemporalAccessorFormattingProcessor() {
		this.formatter = null;

	}

	/**
	 * Constructs a new <tt>AbstractTemporalAccessorFormattingProcessor</tt> processor,
	 * which formats the type as a String, then calls the next processor in
	 * the chain.
	 *
	 * @param next next processor in the chain
	 * @throws NullPointerException if temporalAccessor or next is null
	 */
	public AbstractTemporalAccessorFormattingProcessor(final CellProcessor next) {
		super(next);
		this.formatter = null;

	}

	/**
	 * Constructs a new <tt>AbstractTemporalAccessorFormattingProcessor</tt> processor,
	 * which formats the type as a String using the supplied formatter.
	 *
	 * @param formatter the formatter to use
	 * @throws NullPointerException if temporalAccessor or formatter is null
	 */
	public AbstractTemporalAccessorFormattingProcessor(final DateTimeFormatter formatter) {
		checkPreconditions(formatter);
		this.formatter = formatter;
	}

	/**
	 * Constructs a new <tt>AbstractTemporalAccessorFormattingProcessor</tt> processor,
	 * which formats the type as a String using the supplied formatter,
	 * then calls the next processor in the chain.
	 *
	 * @param formatter the formatter to use
	 * @param next      the next processor in the chain
	 * @throws NullPointerException if temporalAccessor, formatter or next is null
	 */
	public AbstractTemporalAccessorFormattingProcessor(final DateTimeFormatter formatter, final CellProcessor next) {
		super(next);
		checkPreconditions(formatter);
		this.formatter = formatter;
	}

	/**
	 * Checks the preconditions for creating a new
	 * AbstractTemporalAccessorFormattingProcessor processor.
	 *
	 * @param formatter the formatter
	 * @throws NullPointerException if temporalAccessor or formatter is null
	 */
	private static void checkPreconditions(final DateTimeFormatter formatter) {
		Objects.requireNonNull(formatter, "formatter should not be null");
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws SuperCsvCellProcessorException if value is null, not the correct type, or can't be formatted
	 */
	public Object execute(final Object value, final CsvContext context) {
		validateInputNotNull(value, context);
		final Class<T> ourType = getType();
		if( !(value.getClass().equals(ourType)) ) {
			throw new SuperCsvCellProcessorException(ourType, value, context, this);
		}
		final TemporalAccessor timeType = ourType.cast(value);
		try {
			if( formatter != null ) {
				return formatter.format(timeType);
			} else {
				return timeType.toString();
			}
		}
		catch(DateTimeException | IllegalArgumentException e) {
			throw new SuperCsvCellProcessorException(
				SuperCsvMessages.getMessage("org.supercsv.exception.cellprocessor.jdk8.InvalidFormat.message", ourType.getSimpleName()), context, this, e);
		}
	}

	/**
	 * @return the type formatted by this subclass
	 */
	protected abstract Class<T> getType();

}
