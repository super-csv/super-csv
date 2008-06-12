package org.supercsv.cellprocessor;

import org.supercsv.cellprocessor.ift.DoubleCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * Convert a string to a char. If the string has a length > 1, then the first character is
 * 
 * @since 1.10
 * @author Kasper B. Graversen
 */
public class ParseChar extends CellProcessorAdaptor  implements StringCellProcessor {

public ParseChar() {
	super();
}

public ParseChar(final DoubleCellProcessor next) {
	super(next);
}

/**
 * {@inheritDoc}
 */
@Override
public Object execute(final Object value, final CSVContext context) {
	if( value == null ) { throw new NullInputException("Input cannot be null on line " + context.lineNumber + " at column " + context.columnNumber, context, this); }
	final Character result;
	if( value instanceof Character ) {
		result = (Character) value;
	} else if( value instanceof String ) {
		final String tmp = (String) value;
		if( tmp.length() == 1 ) {
			result = Character.valueOf(tmp.charAt(0));
		} else {
			throw new SuperCSVException("Can't convert \"" + value
				+ "\" to a char. It must have a length of 1 to be a valid char.", context, this);
		}
	} else {
		throw new SuperCSVException("Can't convert \"" + value
			+ "\" to char. Input is not of type Character nor type String, but of type " + value.getClass().getName(),
			context, this);
	}
	
	return next.execute(result, context);
}
}
