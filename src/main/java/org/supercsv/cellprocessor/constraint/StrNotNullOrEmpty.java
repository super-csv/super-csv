package org.supercsv.cellprocessor.constraint;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.ClassCastInputCSVException;
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * This processor checks if the input is 'null' or an empty string, and raises an exception in that case. In all other
 * cases, the next processor in the chain is invoked.
 * <p>
 * You should only use this processor, when a column must be non-null, but you do not need to apply any other processor
 * to the column.
 * <P>
 * If you apply other processors to the column, you can safely omit this processor as all other processors should do a
 * null-check on its input.
 * 
 * @since 1.50
 * @author Dominique De Vito
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
	if( value == null ) { throw new NullInputException("Input cannot be null on line " + context.lineNumber
		+ " at column " + context.columnNumber, context, this); }
	if( value instanceof String ) {
		String svalue = (String) value;
		if( svalue.length() == 0 ) { throw new SuperCSVException("unexpected empty string", context, this); }
	} else {
		throw new ClassCastInputCSVException(value, String.class, context, this);
	}
	
	return next.execute(value, context);
}
}
