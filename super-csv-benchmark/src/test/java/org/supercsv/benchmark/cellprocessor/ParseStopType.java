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

import org.supercsv.benchmark.model.StopType;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

/**
 * Cell processor to parse a stop type.
 * 
 * @author James Bassett
 */
public class ParseStopType extends CellProcessorAdaptor {
	
	/**
	 * Constructs a new ParseStopType processor.
	 */
	public ParseStopType() {
		super();
	}
	
	/**
	 * Constructs a new ParseStopType processor.
	 */
	public ParseStopType(CellProcessor next) {
		super(next);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object execute(Object value, CsvContext context) {
		
		validateInputNotNull(value, context);
		
		for( StopType stopType : StopType.values() ) {
			if( stopType.name().equals(value.toString()) ) {
				return next.execute(stopType, context);
			}
		}
		
		throw new SuperCsvCellProcessorException(String.format("Unknown StopType: %s", value), context, this);
	}
	
}
