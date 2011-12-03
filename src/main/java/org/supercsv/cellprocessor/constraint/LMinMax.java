package org.supercsv.cellprocessor.constraint;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.LongCellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * Converts the input data to a Long and and ensures the value is between the supplied min and max values. If the data
 * has no upper or lower bound, you should use either of <code>MIN</code> or <code>MAX</code> constants provided in the
 * class.
 * 
 * @author Kasper B. Graversen
 */
public class LMinMax extends CellProcessorAdaptor {
	
	/** Maximum value for a Long */
	public static final long MAXL = Long.MAX_VALUE;
	
	/** Minimum value for a Long */
	public static final long MINL = Long.MIN_VALUE;

	/** Maximum value for an Integer */
	public static final int MAX = Integer.MAX_VALUE;

	/** Minimum value for an Integer */
	public static final int MIN = Integer.MIN_VALUE;

	/** Maximum value for a Short */
	public static final short MAXS = Short.MAX_VALUE;

	/** Minimum value for a Short */
	public static final short MINS = Short.MIN_VALUE;

	/** Maximum value for a Character */
	public static final int MAXC = Character.MAX_VALUE;
	
	/** Minimum value for a Character */
	public static final int MINC = Character.MIN_VALUE;

	/** 255 */
	public static final int MAX8bit = 255;
	
	/** -128 */
	public static final int MIN8bit = -128;
	
	protected long min, max;
	
	/**
	 * Constructs a new <tt>LMinMax</tt> processor, which converts the input data to a Long and and ensures the value is
	 * between the supplied min and max values.
	 * 
	 * @param min
	 *            the minimum value (inclusive)
	 * @param max
	 *            the maximum value (inclusive)
	 */
	public LMinMax(final long min, final long max) {
		super();
		init(min, max);
	}
	
	/**
	 * Constructs a new <tt>LMinMax</tt> processor, which converts the input data to a Long and and ensures the value is
	 * between the supplied min and max values, then calls the next processor in the chain.
	 * 
	 * @param min
	 *            the minimum value (inclusive)
	 * @param max
	 *            the maximum value (inclusive)
	 * @param next
	 *            the next processor in the chain
	 */
	public LMinMax(final long min, final long max, final LongCellProcessor next) {
		super(next);
		init(min, max);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object execute(final Object value, final CSVContext context) throws SuperCSVException {
		validateInputNotNull(value, context, this);
		final Long result;
		if( value instanceof Long ) {
			result = (Long) value;
		} else {
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
	
	/**
	 * Ensures the arguments to the constructor are valid, and sets the instance variables.
	 * 
	 * @param min
	 *            the minimum value
	 * @param max
	 *            the maximum value
	 */
	private void init(final long min, final long max) {
		if( max < min ) {
			throw new SuperCSVException("max < min in the arguments " + min + " " + max, this);
		}
		
		this.min = min;
		this.max = max;
	}
}
