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
package org.supercsv.example;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

/**
 * An example of a custom cell processor.
 */
public class ParseDay extends CellProcessorAdaptor {
	
	public ParseDay() {
		super();
	}
	
	public ParseDay(CellProcessor next) {
		// this constructor allows other processors to be chained after ParseDay
		super(next);
	}
	
	public Object execute(Object value, CsvContext context) {
		
		validateInputNotNull(value, context); // throws an Exception if the input is null
		
		for( Day day : Day.values() ) {
			if( day.name().equalsIgnoreCase(value.toString()) ) {
				// passes the Day enum to the next processor in the chain
				return next.execute(day, context);
			}
		}
		
		throw new SuperCsvCellProcessorException(String.format("Could not parse '%s' as a day", value), context, this);
	}
}
