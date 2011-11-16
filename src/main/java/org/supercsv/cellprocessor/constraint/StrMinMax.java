package org.supercsv.cellprocessor.constraint;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.NullObjectPattern;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * This constraint ensures that the input data has a string length between <code>MIN</code> or <code>MAX</code>
 * (both inclusive). Should the input be anything different from a String, it will be converted to a string using the
 * input's <code>toString()</code> method.
 * 
 * @author Kasper B. Graversen
 */
public class StrMinMax extends CellProcessorAdaptor implements StringCellProcessor {
protected long min, max;

public StrMinMax(final long min, final long max) {
	super(NullObjectPattern.INSTANCE);
	if( max < min ) { throw new SuperCSVException("max < min in the arguments " + min + " " + max, this); }
	if( min < 0 ) { throw new SuperCSVException("min length must be >= 0, is " + min, this); }
	
	this.min = min;
	this.max = max;
}

public StrMinMax(final long min, final long max, final CellProcessor next) {
	super(next);
	if( max < min ) { throw new SuperCSVException("max < min in the arguments " + min + " " + max, this); }
	this.min = min;
	this.max = max;
}

/**
 * {@inheritDoc}
 */
@Override
public Object execute(final Object value, final CSVContext context) throws NumberFormatException {
	if( value == null ) { throw new NullInputException("Input cannot be null on line " + context.lineNumber + " at column " + context.columnNumber, context, this); }
	final String sval = value.toString(); // cast
	if( sval.length() < min || sval.length() > max ) { throw new SuperCSVException("Entry \"" + value + "\" on line "
		+ context.lineNumber + " column " + context.columnNumber + " is not within the string sizes " + min + " - "
		+ max, context, this); }
	
	return next.execute(sval, context);
}
}
