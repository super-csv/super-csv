package org.supercsv.cellprocessor;

import org.supercsv.cellprocessor.ift.LongCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * Convert a string to an integer
 * 
 * @author Kasper B. Graversen
 */
public class ParseInt extends CellProcessorAdaptor  implements StringCellProcessor {

public ParseInt() {
	super();
}

public ParseInt(final LongCellProcessor next) {
	super(next);
}

/**
 * {@inheritDoc}
 */
@Override
public Object execute(final Object value, final CSVContext context) throws SuperCSVException {
	if( value == null ) { throw new NullInputException("Input cannot be null on line " + context.lineNumber + " at column " + context.columnNumber, context, this); }
	final Integer result;
	if( value instanceof Integer ) {
		result = (Integer) value;
	} else if( value instanceof String ) {
		try {
			result = new Integer((String) value);
		}
		catch(final NumberFormatException e) {
			throw new SuperCSVException("Parser error", context, this, e);
		}
	} else {
		throw new SuperCSVException("Can't convert \"" + value
			+ "\" to integer. Input is not of type Integer nor type String but of type " + value.getClass().getName(),
			context, this);
	}
	
	return next.execute(result, context);
}
}
