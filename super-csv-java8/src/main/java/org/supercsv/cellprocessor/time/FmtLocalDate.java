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
import java.time.format.DateTimeFormatterBuilder;

import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 * Converts a LocalDate to a String.
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
public class FmtLocalDate extends AbstractTemporalAccessorFormattingProcessor {
	/**
	 * Constructs a new <tt>FmtLocalDate</tt> processor, which formats a
	 * LocalDate as a String, with the same output as {@link LocalDate#toString()}
	 */
	public FmtLocalDate() {
		super();
	}

	/**
	 * Constructs a new <tt>FmtLocalDate</tt> processor, which formats a
	 * LocalDate as a String, then calls the next processor in the chain.
	 *
	 * @param next next processor in the chain
	 * @throws NullPointerException if next is null
	 * @see FmtLocalDate()
	 */
	public FmtLocalDate(final CellProcessor next) {
		super(next);
	}

	/**
	 * Constructs a new <tt>FmtLocalDate</tt> processor, which formats a
	 * LocalDate as a String using the supplied formatter.
	 *
	 * @param formatter the formatter to use
	 * @throws NullPointerException if formatter is null
	 */
	public FmtLocalDate(final DateTimeFormatter formatter) {
		super(formatter);
	}

	/**
	 * Constructs a new <tt>FmtLocalDate</tt> processor, which formats a
	 * LocalDate as a String using the supplied formatter, then calls the next
	 * processor in the chain.
	 *
	 * @param formatter the formatter to use
	 * @param next      the next processor in the chain
	 * @throws NullPointerException if formatter or next is null
	 */
	public FmtLocalDate(final DateTimeFormatter formatter, final CellProcessor next) {
		super(formatter, next);
	}

	@Override
	protected Class<LocalDate> getType() {
		return LocalDate.class;
	}
}
