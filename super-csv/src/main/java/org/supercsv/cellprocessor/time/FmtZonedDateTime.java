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

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 * Converts a ZonedDateTime to a String.
 * For constructors using DateTimeFormatter, refer to the following
 * classes:
 * <ul>
 * <li>{@link DateTimeFormatter} - formats by pattern and style</li>
 * <li>{@link DateTimeFormatterBuilder} - complex formats created via method
 * calls</li>
 * </ul>
 *
 * @author Ludovico Fischer
 * @since 2.4.0
 */
public class FmtZonedDateTime extends AbstractTemporalAccessorFormattingProcessor<ZonedDateTime> {

	/**
	 * Constructs a new <tt>FmtZonedDateTime</tt> processor, which formats a
	 * ZonedDateTime as a String.
	 */
	public FmtZonedDateTime() {
		super();
	}

	/**
	 * Constructs a new <tt>FmtZonedDateTime</tt> processor, which formats a
	 * ZonedDateTime as a String, then calls the next processor in the chain.
	 *
	 * @param next next processor in the chain
	 * @throws NullPointerException if next is null
	 */
	public FmtZonedDateTime(final CellProcessor next) {
		super(next);
	}

	/**
	 * Constructs a new <tt>FmtZonedDateTime</tt> processor, which formats a
	 * ZonedDateTime as a String using the supplied formatter.
	 *
	 * @param formatter the formatter to use
	 * @throws NullPointerException if formatter is null
	 */
	public FmtZonedDateTime(final DateTimeFormatter formatter) {
		super(formatter);
	}

	/**
	 * Constructs a new <tt>FmtZonedDateTime</tt> processor, which formats a
	 * ZonedDateTime as a String using the supplied formatter, then calls the next
	 * processor in the chain.
	 *
	 * @param formatter the formatter to use
	 * @param next      the next processor in the chain
	 * @throws NullPointerException if formatter or next is null
	 */
	public FmtZonedDateTime(final DateTimeFormatter formatter, final CellProcessor next) {
		super(formatter, next);
	}

	@Override
	protected Class<ZonedDateTime> getType() {
		return ZonedDateTime.class;
	}
}
