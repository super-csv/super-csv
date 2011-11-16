package org.supercsv.cellprocessor.constraint;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.LongCellProcessor;
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
public class LMinMax extends CellProcessorAdaptor {

/**
 * Long.MAX_VALUE
 */
public static final long MAXL = Long.MAX_VALUE;
/**
 * Long.MIN_VALUE
 */
public static final long MINL = Long.MIN_VALUE;
/**
 * Integer.MAX_VALUE
 */
public static final int MAX = Integer.MAX_VALUE;
/**
 * Integer.MIN_VALUE
 */
public static final int MIN = Integer.MIN_VALUE;
/**
 * Short.MAX_VALUE
 */
public static final short MAXS = Short.MAX_VALUE;
/** Short.MIN_VALUE */
public static final short MINS = Short.MIN_VALUE;
/** Character.MAX_VALUE */
public static final int MAXC = Character.MAX_VALUE;
/** Character.MIN_VALUE */
public static final int MINC = Character.MIN_VALUE;
/** 255 */
public static final int MAX8bit = 255;
/** -128 */
public static final int MIN8bit = -128;

protected long min, max;

public LMinMax(final long min, final long max) {
	super();
	init(min, max);
}

public LMinMax(final long min, final long max, final LongCellProcessor next) {
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
	final Long result;
	if( value instanceof Long ) {
		result = (Long) value;
	}
	else {
		try {
			result = Long.parseLong(value.toString());
		}
		catch(final NumberFormatException e) {
			throw new SuperCSVException("Parsing error", context, this, e);
		}
	}
	
	if( !(result >= min && result <= max) ) {
		throw new SuperCSVException("Entry \"" + value + "\" on line " + context.lineNumber + " column "
			+ context.columnNumber + " is not within the numerical range " + min + "-" + max, context, this);
	}
	
	return next.execute(result, context);
}

private void init(final long _min, final long _max) {
	if( _max < _min ) {
		throw new SuperCSVException("max < min in the arguments " + _min + " " + _max, this);
	}
	
	this.min = _min;
	this.max = _max;
}
}
