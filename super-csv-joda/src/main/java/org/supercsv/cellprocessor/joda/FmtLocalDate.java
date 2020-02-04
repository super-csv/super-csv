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

import java.util.Locale;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.ISODateTimeFormat;
import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 * Converts a Joda LocalDate to a String.
 * 
 * <p>
 * For constructors using DateTimeFormatter, refer to the following Joda
 * classes:
 * <ul>
 * <li>{@link DateTimeFormat} - formats by pattern and style</li>
 * <li>{@link ISODateTimeFormat} - ISO8601 formats</li>
 * <li>{@link DateTimeFormatterBuilder} - complex formats created via method
 * calls</li>
 * </ul>
 * <p>
 * For constructors using date format Strings, refer to {@link DateTimeFormat}
 * for example formats.
 * 
 * @since 2.3.0
 * @author James Bassett
 */
public class FmtLocalDate extends AbstractJodaFormattingProcessor<LocalDate> {

	private static final Class<LocalDate> JODA_CLASS = LocalDate.class;

	/**
	 * Constructs a new <code>FmtLocalDate</code> processor, which formats a Joda
	 * LocalDate as a String.
	 */
	public FmtLocalDate() {
		super(JODA_CLASS);
	}

	/**
	 * Constructs a new <code>FmtLocalDate</code> processor, which formats a Joda
	 * LocalDate as a String, then calls the next processor in the chain.
	 * 
	 * @param next
	 *            next processor in the chain
	 * @throws NullPointerException
	 *             if next is null
	 */
	public FmtLocalDate(final CellProcessor next) {
		super(JODA_CLASS, next);
	}

	/**
	 * Constructs a new <code>FmtLocalDate</code> processor, which formats a Joda
	 * LocalDate as a String using the supplied formatter.
	 * 
	 * @param formatter
	 *            the formatter to use
	 * @throws NullPointerException
	 *             if formatter is null
	 */
	public FmtLocalDate(final DateTimeFormatter formatter) {
		super(JODA_CLASS, formatter);
	}

	/**
	 * Constructs a new <code>FmtLocalDate</code> processor, which formats a Joda
	 * LocalDate as a String using the supplied formatter, then calls the next
	 * processor in the chain.
	 * 
	 * @param formatter
	 *            the formatter to use
	 * @param next
	 *            the next processor in the chain
	 * @throws NullPointerException
	 *             if formatter or next is null
	 */
	public FmtLocalDate(final DateTimeFormatter formatter,
			final CellProcessor next) {
		super(JODA_CLASS, formatter, next);
	}

	/**
	 * Constructs a new <code>FmtLocalDate</code> processor, which formats a Joda
	 * LocalDate as a String using the supplied pattern and the default locale.
	 * 
	 * @param pattern
	 *            the pattern to use
	 * @throws NullPointerException
	 *             if pattern is null
	 */
	public FmtLocalDate(final String pattern) {
		super(JODA_CLASS, pattern);
	}

	/**
	 * Constructs a new <code>FmtLocalDate</code> processor, which formats a Joda
	 * LocalDate as a String using the supplied pattern and the default locale,
	 * then calls the next processor in the chain.
	 * 
	 * @param pattern
	 *            the pattern to use
	 * @param next
	 *            the next processor in the chain
	 * @throws NullPointerException
	 *             if pattern or next is null
	 */
	public FmtLocalDate(final String pattern, final CellProcessor next) {
		super(JODA_CLASS, pattern, next);
	}

	/**
	 * Constructs a new <code>FmtLocalDate</code> processor, which formats a Joda
	 * LocalDate as a String using the supplied pattern and the locale.
	 * 
	 * @param pattern
	 *            the pattern to use
	 * @param locale
	 *            the locale to use (default used if <code>null</code>)
	 * @throws NullPointerException
	 *             if pattern is null
	 */
	public FmtLocalDate(final String pattern, final Locale locale) {
		super(JODA_CLASS, pattern, locale);
	}

	/**
	 * Constructs a new <code>FmtLocalDate</code> processor, which formats a Joda
	 * LocalDate as a String using the supplied pattern and the locale, then
	 * calls the next processor in the chain.
	 * 
	 * @param pattern
	 *            the pattern to use
	 * @param locale
	 *            the locale to use (default used if <code>null</code>)
	 * @param next
	 *            the next processor in the chain
	 * @throws NullPointerException
	 *             if pattern or next is null
	 */
	public FmtLocalDate(final String pattern, final Locale locale,
			final CellProcessor next) {
		super(JODA_CLASS, pattern, locale, next);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String format(final LocalDate jodaType,
			final DateTimeFormatter formatter) {
		return jodaType.toString(formatter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String format(final LocalDate jodaType, final String pattern,
			final Locale locale) {
		return jodaType.toString(pattern, locale);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String format(final LocalDate jodaType) {
		return jodaType.toString();
	}

}
