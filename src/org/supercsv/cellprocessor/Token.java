/*
 * SuperCSV is Copyright 2007, Kasper B. Graversen Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package org.supercsv.cellprocessor;

import org.supercsv.cellprocessor.ift.BoolCellProcessor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.ift.DateCellProcessor;
import org.supercsv.cellprocessor.ift.DoubleCellProcessor;
import org.supercsv.cellprocessor.ift.LongCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.NullInputException;
import org.supercsv.util.CSVContext;

/**
 * This processor is used in the situations you want to be able to check for the presence of a meta data or "special
 * token". Such a token could be the string "[empty]" which could denote that a column is different from the empty
 * string "".
 * <p>
 * It can be used in conjunction with a ParseLong() processor, since the token would be discovered before the parser
 * attempt so parse the token as a number.
 * <p>
 * Comparison between column value and the <tt>token</tt> is based on the object's <tt>equals()</tt> method.
 * 
 * @since 1.02
 * @author Kasper B. Graversen
 */
public class Token extends CellProcessorAdaptor implements DateCellProcessor, DoubleCellProcessor, LongCellProcessor,
	StringCellProcessor, BoolCellProcessor {
final Object returnValue;
final Object token;

/**
 * Constructor To have the string <tt>"[empty]"</tt> represent the empty amount -1 you could use this class as <code>
 * new Token("[empty]", -1);
 * </code>
 * 
 * @param token
 *            the metadata identification
 * @param returnValue
 *            the data being returned in case the meta data is encountered
 */
public Token(final Object token, final Object returnValue) {
	super();
	this.token = token;
	this.returnValue = returnValue;
}

/**
 * Constructor
 * 
 * @param token
 *            the metadata identification
 * @param returnValue
 *            the data being returned in case the meta data is encountered
 */
public Token(final Object token, final Object returnValue, final CellProcessor next) {
	super(next);
	this.token = token;
	this.returnValue = returnValue;
}

/**
 * {@inheritDoc}
 */
@Override
public Object execute(final Object value, final CSVContext context) {
	if( value == null ) {
		throw new NullInputException("Input cannot be null on line " + context.lineNumber + " at column "
			+ context.columnNumber, context, this);
	}
	if( value.equals(token) ) {
		return returnValue;
	}
	
	return next.execute(value, context);
}
}
