package org.supercsv.cellprocessor.constraint;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.DoubleCellProcessor;
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * Converts the input data to a long and ensure that number is within a specified numeric range. If the data has no
 * upper bound (or lower bound), you should use either of <code>MIN</code> or <code>MAX</code> constants provided in
 * the class.
 * 
 * @author Kasper B. Graversen
 */
public class DMinMax extends CellProcessorAdaptor {
public static final double MAXD = Double.MAX_VALUE;
public static final double MIND = Double.MIN_VALUE;
public static final double MAXS = Short.MAX_VALUE;
public static final double MINS = Short.MIN_VALUE;
public static final double MAXC = Character.MAX_VALUE;
public static final double MINC = Character.MIN_VALUE;
/** 255 */
public static final int MAX8bit = 255;
/** -128 */
public static final int MIN8bit = -128;

protected double min, max;

public DMinMax(final double min, final double max) {
	super();
	init(min, max);
}

public DMinMax(final double min, final double max, final DoubleCellProcessor next) {
	super(next);
	init(min, max);
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
	
	final Double result;
	if( value instanceof Double ) {
		result = (Double) value;
	}
	else {
		try {
			result = Double.parseDouble(value.toString());
		}
		catch(final NumberFormatException e) {
			throw new SuperCSVException("Parser error", context, this, e);
		}
	}
	
	if( !(result >= min && result <= max) ) {
		throw new SuperCSVException("Entry \"" + value + "\" on line " + context.lineNumber + " column "
			+ context.columnNumber + " is not within the numerical range " + min + "-" + max, context, this);
	}
	return next.execute(result, context);
}

private void init(final double min, final double max) {
	if( max < min ) {
		throw new SuperCSVException("max < min in the arguments " + min + " " + max, this);
	}
	
	this.min = min;
	this.max = max;
}
}
