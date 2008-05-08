package org.supercsv.cellprocessor.constraint;

import java.util.HashSet;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * This processor requires that the input string has a given length (either chosen from one or out of a set of lengths).
 * The length constraints must all be > 0 or an exception is thrown Lookup time is O(1).
 * 
 * @author Kasper B. Graversen
 * @author Dominique De Vito
 */
public class Strlen extends CellProcessorAdaptor implements StringCellProcessor {
/** Set of all accepted lengths */
protected HashSet<Integer> requiredLengths = new HashSet<Integer>();

public Strlen(final int... requiredLengths) {
	super();
	addValues(requiredLengths);
}

public Strlen(final int requiredLength, final CellProcessor next) {
	this(new int[] { requiredLength }, next);
}

public Strlen(final int[] requiredLengths, final CellProcessor next) {
	super(next);
	addValues(requiredLengths);
}

/** Ensure we only memorize valid lengths */
protected void addValues(final int... requiredLengths) throws SuperCSVException {
	for( final int length : requiredLengths ) {
		if( length < 0 ) { throw new SuperCSVException("Cannot accept length below 0", this); }
		this.requiredLengths.add(length);
	}
}

/**
 * {@inheritDoc}
 * 
 * @throws SuperCSVException
 *             upon receiving a string of an unaccepted length
 * @throws ClassCastException
 *             is the parameter value cannot be cast to a String
 * @return the argument value if the value is unique
 */
@Override
public Object execute(final Object value, final CSVContext context) throws SuperCSVException, ClassCastException {
	if( value == null ) { throw new NullInputException("Input cannot be null on line " + context.lineNumber
		+ " at column " + context.columnNumber, context, this); }
	final String sval = (value == null) ? null : value.toString(); // cast
	final int slength = (sval == null) ? 0 : sval.length();
	
	// check for required lengths
	if( !requiredLengths.contains(slength) ) { throw new SuperCSVException("Entry \"" + value + "\" is not of any of "
		+ "the required lengths " + printRequiredLengths(), context, this); }
	
	return next.execute(value, context);
}

private String printRequiredLengths() {
	final StringBuilder sb = new StringBuilder();
	String currentSeparator = "";
	String separator = ", ";
	for( final int length : requiredLengths ) {
		sb.append(currentSeparator);
		sb.append(length);
		currentSeparator = separator;
	}
	return sb.toString();
}
}
