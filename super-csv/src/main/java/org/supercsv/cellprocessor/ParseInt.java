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

import org.supercsv.cellprocessor.ift.LongCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

/**
 * Converts a String to an Integer.
 * 
 * @author Kasper B. Graversen
 */
public class ParseInt extends CellProcessorAdaptor implements StringCellProcessor {
	
	/**
	 * Constructs a new <tt>ParseInt</tt> processor, which converts a String to an Integer.
	 */
	public ParseInt() {
		super();
	}
	
	/**
	 * Constructs a new <tt>ParseInt</tt> processor, which converts a String to an Integer, then calls the next
	 * processor in the chain.
	 * 
	 * @param next
	 *            the next processor in the chain
	 * @throws NullPointerException
	 *             if next is null
	 */
	public ParseInt(final LongCellProcessor next) {
		super(next);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws SuperCsvCellProcessorException
	 *             if value is null, isn't an Integer or String, or can't be parsed as an Integer
	 */
	public Object execute(final Object value, final CsvContext context) {
		validateInputNotNull(value, context);
		
		final Integer result;
		if( value instanceof Integer ) {
			result = (Integer) value;
		} else if( value instanceof String ) {
			try {
				result = Integer.valueOf((String) value);
			}
			catch(final NumberFormatException e) {
				throw new SuperCsvCellProcessorException(
					String.format("'%s' could not be parsed as an Integer", value), context, this, e);
			}
		} else {
			final String actualClassName = value.getClass().getName();
			throw new SuperCsvCellProcessorException(String.format(
				"the input value should be of type Integer or String but is of type %s", actualClassName), context,
				this);
		}
		
		return next.execute(result, context);
	}
}
