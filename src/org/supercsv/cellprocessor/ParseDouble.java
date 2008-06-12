package org.supercsv.cellprocessor;

import org.supercsv.cellprocessor.ift.DoubleCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * Convert a string to a double
 * 
 * @author Kasper B. Graversen
 */
public class ParseDouble extends CellProcessorAdaptor  implements StringCellProcessor {

public ParseDouble() {
	super();
}

public ParseDouble(final DoubleCellProcessor next) {
	super(next);
}

/**
 * {@inheritDoc}
 */
@Override
public Object execute(final Object value, final CSVContext context) throws SuperCSVException {
	if( value == null ) { throw new NullInputException("Input cannot be null on line " + context.lineNumber + " at column " + context.columnNumber, context, this); }
	
	final Double result;
	if( value instanceof Double ) {
		result = (Double) value;
	} else if( value instanceof String ) {
		try {
			result = new Double((String) value);
		}
		catch(final NumberFormatException e) {
			throw new SuperCSVException("Parser error", context, this, e);
		}
	} else {
		throw new SuperCSVException("Can't convert \"" + value
			+ "\" to double. Input is not of type Double nor type String, but of type " + value.getClass().getName(),
			context, this);
	}
	
	return next.execute(result, context);
}
}
