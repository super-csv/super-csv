package org.supercsv.cellprocessor.constraint;

import java.util.HashSet;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.BoolCellProcessor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.ift.DateCellProcessor;
import org.supercsv.cellprocessor.ift.DoubleCellProcessor;
import org.supercsv.cellprocessor.ift.LongCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * Deprecated due to bad naming. Use {@link RequireHashCode} instead
 * 
 * @author Kasper B. Graversen
 */
@Deprecated
public class Required extends CellProcessorAdaptor implements BoolCellProcessor, DateCellProcessor,
	DoubleCellProcessor, LongCellProcessor, StringCellProcessor {
protected HashSet<Integer> requiredHashCodes = new HashSet<Integer>();

public Required(final int... requiredHashcodes) {
	super();
	addValues(requiredHashcodes);
}

public Required(final int requiredHashcode, final CellProcessor next) {
	this(new int[] { requiredHashcode }, next);
}

public Required(final int[] requiredHashcodes, final CellProcessor next) {
	super(next);
	addValues(requiredHashcodes);
}

protected void addValues(final int... requiredHashcodes) throws SuperCSVException {
	for( final int hash : requiredHashcodes ) {
		if( requiredHashCodes.contains(hash) ) { throw new SuperCSVException("Cannot accept two identical hash codes",
			this); }
		requiredHashCodes.add(hash);
	}
}

/**
 * {@inheritDoc}
 * 
 * @throws SuperCSVException
 *             upon receiving a string of an un-accepted length
 * @throws ClassCastException
 *             is the parameter value cannot be cast to a String
 * @return the argument value if the value is unique
 */
@Override
public Object execute(final Object value, final CSVContext context) throws SuperCSVException, ClassCastException {
	if( value == null ) { throw new NullInputException("Input cannot be null on line " + context.lineNumber + " at column " + context.columnNumber, context, this); }
	// check for required hash
	if( !requiredHashCodes.contains(value.hashCode()) ) {
		// create string of required hash'es for error msg
		final StringBuilder sb = new StringBuilder();
		for( final int hash : requiredHashCodes ) {
			sb.append(hash + ", ");
		}
		sb.deleteCharAt(sb.length() - 1); // delete last comma
		
		throw new SuperCSVException("Entry \"" + value + "\" on line " + context.lineNumber + " column "
			+ context.columnNumber + " has hashcode " + value.hashCode()
			+ " which is not one of the required hash codes: " + sb.toString(), context, this);
	}
	
	return next.execute(value, context);
}
}
