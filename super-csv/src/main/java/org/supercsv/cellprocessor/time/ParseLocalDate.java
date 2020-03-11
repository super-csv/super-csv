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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 * Converts a String to a LocalDate.
 *
 * @author James Bassett
 * @since 2.4.0
 */
public class ParseLocalDate extends AbstractTemporalAccessorParsingProcessor<LocalDate> {

	/**
	 * Constructs a new <code>ParseLocalDate</code> processor,
	 * which parses a String recognised by {@link LocalDate#parse(CharSequence)}
	 * as a LocalDate.
	 */
	public ParseLocalDate() {
	}

	/**
	 * Constructs a new <code>ParseLocalDate</code> processor,
	 * which parses a String recognised by {@link LocalDate#parse(CharSequence)}
	 * as a LocalDate, then calls the next processor in the chain.
	 *
	 * @param next the next processor in the chain
	 * @throws NullPointerException if next is null
	 */
	public ParseLocalDate(final CellProcessor next) {
		super(next);
	}

	/**
	 * Constructs a new <code>ParseLocalDate</code> processor, which parses a String
	 * as a LocalDate using the supplied formatter.
	 *
	 * @param formatter the formatter used for parsing
	 * @throws NullPointerException if formatter is null
	 */
	public ParseLocalDate(final DateTimeFormatter formatter) {
		super(formatter);
	}

	/**
	 * Constructs a new <code>ParseLocalDate</code> processor, which parses a String
	 * as a LocalDate using the supplied formatter, then calls the next
	 * processor in the chain.
	 *
	 * @param formatter the formatter used for parsing
	 * @param next      the next processor in the chain
	 * @throws NullPointerException if formatter or next is null
	 */
	public ParseLocalDate(final DateTimeFormatter formatter, final CellProcessor next) {
		super(formatter, next);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected LocalDate parse(final String string) {
		return LocalDate.parse(string);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected LocalDate parse(final String string, final DateTimeFormatter formatter) {
		return LocalDate.parse(string, formatter);
	}
}
