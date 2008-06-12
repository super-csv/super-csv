package org.supercsv.cellprocessor;

import org.supercsv.cellprocessor.ift.BoolCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * Convert a string to a boolean. The strings "true", "false", "0", "1", "N", "Y", "F", "T" are the default accepted
 * strings. Other string values may be provided to represent true and false values.
 * 
 * @author Kasper B. Graversen
 * @author Dominique De Vito
 * @since 1.0
 */
public class ParseBool extends CellProcessorAdaptor implements StringCellProcessor {

private static String[] DEFAULT_TRUE_VALUES = new String[] { "1", "true", "t", "y" };

private static String[] DEFAULT_FALSE_VALUES = new String[] { "0", "false", "f", "n" };

private final String[] trueValues;
private final String[] falseValues;

public ParseBool() {
	this(DEFAULT_TRUE_VALUES, DEFAULT_FALSE_VALUES);
}

public ParseBool(final BoolCellProcessor next) {
	this(DEFAULT_TRUE_VALUES, DEFAULT_FALSE_VALUES, next);
}

public ParseBool(final String trueValue, final String falseValue) {
	super();
	this.trueValues = new String[] { trueValue };
	this.falseValues = new String[] { falseValue };
}

public ParseBool(final String[] trueValues, final String[] falseValues) {
	super();
	this.trueValues = trueValues;
	this.falseValues = falseValues;
}

public ParseBool(final String trueValue, final String falseValue, final BoolCellProcessor next) {
	super(next);
	this.trueValues = new String[] { trueValue };
	this.falseValues = new String[] { falseValue };
}

public ParseBool(final String[] trueValues, final String[] falseValues, final BoolCellProcessor next) {
	super(next);
	this.trueValues = trueValues;
	this.falseValues = falseValues;
}

/**
 * {@inheritDoc}
 * 
 * @throws SuperCSVException
 *             when the value is some value that cannot be translated into a boolean value
 */
@Override
public Object execute(final Object value, final CSVContext context) throws SuperCSVException {
	if( value == null ) { throw new NullInputException("Input cannot be null on line " + context.lineNumber
		+ " column " + context.columnNumber, context, this); }
	Boolean result;
	final String sval = ((String) value).toLowerCase();
	if( isFalseValue(sval) ) {
		result = Boolean.FALSE;
	} else if( isTrueValue(sval) ) {
		result = Boolean.TRUE;
	} else {
		throw new SuperCSVException("Cannot parse \"" + value + "\" to a boolean on line " + context.lineNumber
			+ " column " + context.columnNumber, context, this);
	}
	
	return next.execute(result, context);
}

private boolean isTrueValue(final String sval) {
	return indexOf(sval, trueValues) >= 0;
}

private boolean isFalseValue(final String sval) {
	return indexOf(sval, falseValues) >= 0;
}

private static int indexOf(final String sval, final String[] possibleMatches) {
	if( possibleMatches == null ) { return -1; }
	for( int i = 0; i < possibleMatches.length; i++ ) {
		if( sval.equals(possibleMatches[i]) ) { return i; }
	}
	return -1;
}
}
