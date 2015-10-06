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
package org.supercsv.cellprocessor;

import java.sql.Time;
import java.util.Date;
import org.supercsv.cellprocessor.ift.DateCellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

/**
 * Converts a Date to a Time.
 * 
 * @author Pietro Aragona
 * @since 2.4.0
 */
public class ParseTime extends CellProcessorAdaptor implements DateCellProcessor {
	
	/**
	 * Constructs a new <tt>ParseTime</tt> processor, which converts a Date to a Time.
	 */
	public ParseTime() {
		super();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws SuperCsvCellProcessorException
	 *             if value is null, isn't a Long or String, or can't be parsed as a Long
	 */
	public Object execute(Object value, CsvContext context) {
		validateInputNotNull(value, context);
		
		if( !(value instanceof Date) ) {
			throw new SuperCsvCellProcessorException(Date.class, value, context, this);
		}
		final Time result = new Time(((Date) value).getTime());
		return next.execute(result, context);
		
	}
	
}
