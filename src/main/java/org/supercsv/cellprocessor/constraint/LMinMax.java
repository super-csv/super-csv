package org.supercsv.cellprocessor.constraint;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.LongCellProcessor;
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * Converts the input data to a Long and and ensures the value is between the supplied min and max values (inclusive).
 * If the data has no upper or lower bound, you should use either of <code>MIN</code> or <code>MAX</code> constants
 * provided in the class.
 * 
 * @author Kasper B. Graversen
 * @author James Bassett
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
	
	private final long min;
	
	private final long max;
	
	/**
	 * Constructs a new <tt>LMinMax</tt> processor, which converts the input data to a Long and and ensures the value is
	 * between the supplied min and max values.
	 * 
	 * @param min
	 *            the minimum value (inclusive)
	 * @param max
	 *            the maximum value (inclusive)
	 * @throws IllegalArgumentException
	 *             if max < min
	 */
	public LMinMax(final long min, final long max) {
		super();
		checkPreconditions(min, max);
		this.min = min;
		this.max = max;
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
	 * @throws NullPointerException
	 *             if next is null
	 * @throws IllegalArgumentException
	 *             if max < min
	 */
	public LMinMax(final long min, final long max, final LongCellProcessor next) {
		super(next);
		checkPreconditions(min, max);
		this.min = min;
		this.max = max;
	}
	
	/**
	 * Checks the preconditions for creating a new LMinMax processor.
	 * 
	 * @param min
	 *            the minimum value (inclusive)
	 * @param max
	 *            the maximum value (inclusive)
	 * @throws IllegalArgumentException
	 *             if max < min
	 */
	private static void checkPreconditions(final long min, final long max) {
		if( max < min ) {
			throw new IllegalArgumentException(String.format("max (%d) should not be < min (%d)", max, min));
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws NullInputException
	 *             if value is null
	 * @throws SuperCSVException
	 *             if value can't be parsed as a Long, or doesn't lie between min and max (inclusive)
	 */
	public Object execute(final Object value, final CSVContext context) {
		validateInputNotNull(value, context);
		
		final Long result;
		if( value instanceof Long ) {
			result = (Long) value;
		} else {
			try {
				result = Long.parseLong(value.toString());
			}
			catch(final NumberFormatException e) {
				throw new SuperCSVException(String.format("'%s' could not be parsed as a Long", value), context, this, e);
			}
		}
		
		if( result < min || result > max ) {
			throw new SuperCSVException(String.format(
				"%d does not lie between the min (%d) and max (%d) values (inclusive)", result, min, max), context, this);
		}
		
		return next.execute(result, context);
	}
	
}
