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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 * Converts a String to a LocalDateTime.
 * For constructors using DateTimeFormatter, refer to the following
 * classes:
 * <ul>
 * <li>{@link DateTimeFormatter} - formats by pattern and style</li>
 * <li>{@link DateTimeFormatter} - ISO 8601 formats</li>
 * <li>{@link DateTimeFormatterBuilder} - complex formats created via method
 * calls</li>
 * </ul>
 *
 * @author Ludovico Fischer
 * @since 2.4.0
 */
public class ParseLocalDateTime extends AbstractTemporalAccessorParsingProcessor<LocalDateTime> {

	/**
	 * Constructs a new <tt>ParseLocalDateTime</tt> processor, which parses a
	 * String as a LocalDateTime, using {@link LocalDateTime#parse(CharSequence)}.
	 */
	public ParseLocalDateTime() {
	}

	/**
	 * Constructs a new <tt>ParseLocalDateTime</tt> processor, which parses a
	 * String as a LocalDateTime, then calls the next processor in the
	 * chain.
	 *
	 * @param next the next processor in the chain
	 * @throws NullPointerException if next is null
	 */
	public ParseLocalDateTime(final CellProcessor next) {
		super(next);
	}

	/**
	 * Constructs a new <tt>ParseLocalDateTime</tt> processor, which parses a
	 * String as a LocalDateTime using the supplied formatter.
	 *
	 * @param formatter the formatter used for parsing
	 * @throws NullPointerException if formatter is null
	 */
	public ParseLocalDateTime(final DateTimeFormatter formatter) {
		super(formatter);
	}

	/**
	 * Constructs a new <tt>ParseLocalDateTime</tt> processor, which parses a
	 * String as a LocalDateTime using the supplied formatter, then calls
	 * the next processor in the chain.
	 *
	 * @param formatter the formatter used for parsing
	 * @param next      the next processor in the chain
	 * @throws NullPointerException if formatter or next is null
	 */
	public ParseLocalDateTime(final DateTimeFormatter formatter, final CellProcessor next) {
		super(formatter, next);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected LocalDateTime parse(final String string) {
		return LocalDateTime.parse(string);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected LocalDateTime parse(final String string, final DateTimeFormatter formatter) {
		return LocalDateTime.parse(string, formatter);
	}
}
