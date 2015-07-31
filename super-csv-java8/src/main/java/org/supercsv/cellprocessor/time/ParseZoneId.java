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
import java.time.ZoneId;
import java.util.Map;
import java.util.Objects;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

/**
 * Converts a String to a ZoneId.
 *
 * @author Ludovico Fischer
 * @since 2.4.0
 */
public class ParseZoneId extends CellProcessorAdaptor {

	private final Map<String, String> aliasMap;

	/**
	 * Constructs a new <tt>ParseZoneId</tt> processor, which parses a
	 * String recognized by {@link ZoneId#of(String)} as a ZoneId.
	 */
	public ParseZoneId() {
		this.aliasMap = null;
	}

	/**
	 * Constructs a new <tt>ParseZoneId</tt> processor, which parses a
	 * String as a ZoneId, then calls the next processor in the
	 * chain.
	 *
	 * @param next the next processor in the chain
	 * @see ParseZoneId()
	 */
	public ParseZoneId(final CellProcessor next) {
		super(next);
		this.aliasMap = null;
	}

	/**
	 * Constructs a new <tt>ParseZoneId</tt> processor, which parses a
	 * String as a ZoneId using the supplied Zone ID mappings.
	 *
	 * @param aliasMap a Map from custom zone IDs to canonical representations
	 * @see ZoneId#of(String, Map)
	 */
	public ParseZoneId(final Map<String, String> aliasMap) {
		Objects.requireNonNull(aliasMap);
		this.aliasMap = aliasMap;
	}

	/**
	 * Constructs a new <tt>ParseZoneId</tt> processor, which parses a
	 * String as a ZoneId using the supplied Zone ID mappings, then calls the next processor in the
	 * chain.
	 *
	 * @param aliasMap a Map from custom zone IDs to canonical representations
	 * @param next     the next processor in the chain
	 * @see ZoneId#of(String, Map)
	 */
	public ParseZoneId(final Map<String, String> aliasMap, final CellProcessor next) {
		super(next);
		Objects.requireNonNull(aliasMap);
		this.aliasMap = aliasMap;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws SuperCsvCellProcessorException if value is null or is not a String
	 */
	public Object execute(final Object value, final CsvContext context) {
		validateInputNotNull(value, context);
		if( !(value instanceof String) ) {
			throw new SuperCsvCellProcessorException(String.class, value, context, this);
		}
		final ZoneId result;
		try {
			if( aliasMap != null ) {
				result = ZoneId.of((String) value, aliasMap);
			} else {
				result = ZoneId.of((String) value);
			}
		}
		catch(DateTimeException e) {
			throw new SuperCsvCellProcessorException("Failed to parse value as a ZoneId", context, this, e);
		}
		return next.execute(result, context);

	}

}
