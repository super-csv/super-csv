package org.supercsv.cellprocessor.constraint;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.ClassCastInputCSVException;
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * This processor checks if the input is 'null' or an empty string, 
 * and raises an exception in that case or does nothing
 * in the other case.
 * 
 * @author Dominique De Vito (ddv36a78@yahoo.fr)
 */
public class StrNotNullOrEmpty extends CellProcessorAdaptor implements StringCellProcessor {

public StrNotNullOrEmpty() {
	super();
}

public StrNotNullOrEmpty(final CellProcessor next) {
	super(next);
}
 
/**
 * {@inheritDoc}
 * 
 * @throws SuperCSVException
 *             upon receiving a 'null' value or an empty string.
 * @return the argument value transformed by next processors
 */
@Override
public Object execute(final Object value, final CSVContext context) {
	if( value == null ) {
		throw new NullInputException("Input cannot be null", context, this);
	}
	if (value instanceof String) {
		String svalue = (String) value;
		if (svalue.length() == 0) {
			throw new SuperCSVException("unexpected empty string", context);
		}
	} else {
		throw new ClassCastInputCSVException(value, String.class, context);
	}
	
	return next.execute(value, context);
}
}
