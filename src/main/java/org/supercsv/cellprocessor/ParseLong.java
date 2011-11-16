package org.supercsv.cellprocessor;

import org.supercsv.cellprocessor.ift.LongCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * Convert a string to a long
 * 
 * @author Kasper B. Graversen
 */
public class ParseLong extends CellProcessorAdaptor implements StringCellProcessor {

public ParseLong() {
	super();
}

public ParseLong(final LongCellProcessor next) {
	super(next);
}

/**
 * {@inheritDoc}
 */
@Override
public Object execute(final Object value, final CSVContext context) throws SuperCSVException {
	if( value == null ) {
		throw new NullInputException("Input cannot be null on line " + context.lineNumber + " at column "
			+ context.columnNumber, context, this);
	}
	final Long result;
	if( value instanceof Long ) {
		result = (Long) value;
	}
	else
		if( value instanceof String ) {
			try {
				result = Long.parseLong((String) value);
			}
			catch(final NumberFormatException e) {
				throw new SuperCSVException("Parser error", context, this, e);
			}
		}
		else {
			throw new SuperCSVException("Can't convert \"" + value
				+ "\" to long. Input is not of type Long nor type String but of type " + value.getClass().getName(),
				context, this);
		}
	
	return next.execute(result, context);
}
}
