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

import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Objects;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

/**
 * Converts a ZoneId to a String.
 * The format is the ID of the timezone, e.g.
 * ('Europe/Vienna'), as defined by {@link ZoneId#toString()}.
 *
 * @author Ludovico Fischer
 * @since 2.4.0
 */
public class FmtZoneId extends CellProcessorAdaptor {

	private final TextStyle textStyle;
	private final Locale locale;

	/**
	 * Constructs a new <tt>FmtZoneId</tt> processor, which formats a
	 * ZoneId as a String.
	 */
	public FmtZoneId() {
		this.textStyle = null;
		this.locale = null;
	}

	/**
	 * Constructs a new <tt>FmtZoneId</tt> processor, which formats a
	 * ZoneId as a String, then calls the next processor in the chain.
	 *
	 * @param next next processor in the chain
	 * @throws NullPointerException if next is null
	 */
	public FmtZoneId(final CellProcessor next) {
		super(next);
		this.textStyle = null;
		this.locale = null;
	}

	/**
	 * Constructs a new <tt>FmtZoneId</tt> processor, which formats a
	 * ZoneId as String, then calls the next processor in the chain.
	 *
	 * @param textStyle the TextStyle to use for formatting
	 * @param locale    the Locale to use for formatting
	 * @throws NullPointerException if either textStyle or locale is null
	 */
	public FmtZoneId(TextStyle textStyle, Locale locale) {
		Objects.requireNonNull(textStyle);
		Objects.requireNonNull(locale);
		this.textStyle = textStyle;
		this.locale = locale;
	}

	/**
	 * Constructs a new <tt>FmtZoneId</tt> processor, which formats a
	 * ZoneId as String, then calls the next processor in the chain.
	 *
	 * @param textStyle the TextStyle to use for formatting
	 * @param locale    the Locale to use for formatting
	 * @param next      next processor in the chain
	 * @throws NullPointerException if any argument is null
	 */
	public FmtZoneId(final TextStyle textStyle, final Locale locale, final CellProcessor next) {
		super(next);
		Objects.requireNonNull(textStyle);
		Objects.requireNonNull(locale);
		this.textStyle = textStyle;
		this.locale = locale;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws SuperCsvCellProcessorException if value is null or not a ZoneId
	 */
	public Object execute(final Object value, final CsvContext context) {
		validateInputNotNull(value, context);
		if( !(value instanceof ZoneId) ) {
			throw new SuperCsvCellProcessorException(ZoneId.class, value, context, this);
		}
		final ZoneId zoneId = (ZoneId) value;
		final String result;
		if (textStyle != null && locale != null) {
			result = zoneId.getDisplayName(textStyle, locale);
		} else {
			result = zoneId.toString();
		}
		return next.execute(result, context);
	}

}
