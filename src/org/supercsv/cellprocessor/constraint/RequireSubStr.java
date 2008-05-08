/**
 *
 */
package org.supercsv.cellprocessor.constraint;

import java.util.List;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * Convert to string and ensure the input string must contain any of the specified sub strings.
 * 
 * @since 1.10
 * @author Kasper B. Graversen
 */
public class RequireSubStr extends CellProcessorAdaptor implements StringCellProcessor {
String[] requiredSubStrings;

public RequireSubStr(final List<String> requiredSubStrings, final CellProcessor next) {
	this(requiredSubStrings.toArray(new String[0]), next);
}

public RequireSubStr(final String... requiredSubStrings) {
	super();
	this.requiredSubStrings = requiredSubStrings.clone();
}

public RequireSubStr(final String requiredSubStrings, final CellProcessor next) {
	this(new String[] { requiredSubStrings }, next);
}

public RequireSubStr(final String[] requiredSubStrings, final CellProcessor next) {
	super(next);
	this.requiredSubStrings = requiredSubStrings.clone();
}

/**
 * {@inheritDoc}
 * 
 * @throws ClassCastException
 *             is the parameter value cannot be cast to a String
 * @throws SuperCSVException
 *             if none of the substrings are found in the input
 * @return the argument value
 */
@Override
public Object execute(final Object value, final CSVContext context) throws SuperCSVException, ClassCastException {
	if( value == null ) { throw new NullInputException("Input cannot be null on line " + context.lineNumber + " at column " + context.columnNumber, context, this); }
	final String sval = value.toString(); // cast
	
	boolean found = false;
	for( final String required : requiredSubStrings ) {
		if( sval.indexOf(required) != -1 ) {
			found = true;
			break;
		}
	}
	if( found == false ) { throw new SuperCSVException("Entry \"" + value + "\" on line " + context.lineNumber
		+ " column " + context.columnNumber + " doesn't contain any of the required substrings", context, this); }
	
	return next.execute(value, context);
}
}
