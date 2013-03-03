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
package org.supercsv.benchmark.cellprocessor;

import java.util.HashMap;
import java.util.Map;

import org.supercsv.benchmark.model.StopTypeAndName;
import org.supercsv.cellprocessor.HashMapper;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

/**
 * Cell processor to format a stop type and name for writing.
 * 
 * @author James Bassett
 */
public class FormatStopTypeAndName extends HashMapper {
	
	private static final Map<Object, Object> MAP = new HashMap<Object, Object>();
	static {
		MAP.put(StopTypeAndName.AIRPORT_STOP, "Airport, airport stop");
		MAP.put(StopTypeAndName.BUS_COACH_STOP, "Bus/coach stop");
		MAP.put(StopTypeAndName.FERRY, "Ferry");
		MAP.put(StopTypeAndName.METRO_TRAM, "Metro/tram" );
		MAP.put(StopTypeAndName.RAIL, "Rail");
		MAP.put(StopTypeAndName.TAXI_RANK, "Taxi rank");
	}
	
	/**
	 * Constructs a new FormatStopTypeAndName processor.
	 */
	public FormatStopTypeAndName() {
		super(MAP);
	}
	
	/**
	 * Constructs a new FormatStopTypeAndName processor.
	 */
	public FormatStopTypeAndName(CellProcessor next) {
		super(MAP, next);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object execute(Object value, CsvContext context) {
		
		validateInputNotNull(value, context);
		
		String stopTypeAndName = (String) MAP.get(value);
		if( stopTypeAndName == null ) {
			throw new SuperCsvCellProcessorException(String.format("Unknown StopTypeAndName: %s", value), context, this);
		}
		return next.execute(stopTypeAndName, context);
	}
	
}
