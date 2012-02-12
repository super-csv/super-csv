package org.supercsv.cellprocessor.constraint;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.DoubleCellProcessor;
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * Converts the input data to a Double and ensures that number is within a specified numeric range (inclusive). If the
 * data has no upper bound (or lower bound), you should use either of <code>MIN</code> or <code>MAX</code> constants
 * provided in the class.
 * 
 * @author Kasper B. Graversen
 * @author James Bassett
 */
public class DMinMax extends CellProcessorAdaptor {
	
	/** Maximum value for a Double */
	public static final double MAXD = Double.MAX_VALUE;
	
	/** Minimum value for a Double */
	public static final double MIND = Double.MIN_VALUE;
	
	/** Maximum value for a Short */
	public static final double MAXS = Short.MAX_VALUE;
	
	/** Minimum value for a Short */
	public static final double MINS = Short.MIN_VALUE;
	
	/** Maximum value for a Character */
	public static final double MAXC = Character.MAX_VALUE;
	
	/** Minimum value for a Character */
	public static final double MINC = Character.MIN_VALUE;
	
	/** 255 */
	public static final int MAX8bit = 255;
	
	/** -128 */
	public static final int MIN8bit = -128; // TODO is this really correct?
	
	private final double min;
	
	private final double max;
	
	/**
	 * Constructs a new <tt>DMinMax</tt> processor, which converts the input to a Double and ensures the value is
	 * between the supplied min and max values.
	 * 
	 * @param min
	 *            the minimum value (inclusive)
	 * @param max
	 *            the maximum value (inclusive)
	 * @throws IllegalArgumentException
	 *             if max < min
	 */
	public DMinMax(final double min, final double max) {
		super();
		checkPreconditions(min, max);
		this.min = min;
		this.max = max;
	}
	
	/**
	 * Constructs a new <tt>DMinMax</tt> processor, which converts the input to a Double, ensures the value is between
	 * the supplied min and max values, then calls the next processor in the chain.
	 * 
	 * @param min
	 *            the minimum value (inclusive)
	 * @param max
	 *            the maximum value (inclusive)
	 * @param next
	 *            the next processor in the chain
	 * @throws IllegalArgumentException
	 *             if max < min
	 * @throws NullPointerException
	 *             if next is null
	 */
	public DMinMax(final double min, final double max, final DoubleCellProcessor next) {
		super(next);
		checkPreconditions(min, max);
		this.min = min;
		this.max = max;
	}
	
	/**
	 * Checks the preconditions for creating a new DMinMax processor.
	 * 
	 * @param min
	 *            the minimum value (inclusive)
	 * @param max
	 *            the maximum value (inclusive)
	 * @throws IllegalArgumentException
	 *             if max < min
	 */
	private static void checkPreconditions(final double min, final double max) {
		if( max < min ) {
			throw new IllegalArgumentException(String.format("max (%f) should not be < min (%f)", max, min));
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws NullInputException
	 *             if value is null
	 * @throws SuperCSVException
	 *             if value can't be parsed as a Double, or doesn't lie between min and max (inclusive)
	 */
	public Object execute(final Object value, final CSVContext context) {
		validateInputNotNull(value, context);
		
		final Double result;
		if( value instanceof Double ) {
			result = (Double) value;
		} else {
			try {
				result = Double.parseDouble(value.toString());
			}
			catch(final NumberFormatException e) {
				throw new SuperCSVException(String.format("'%s' could not be parsed as a Double", value), context, this,
					e);
			}
		}
		
		if( result < min || result > max ) {
			throw new SuperCSVException(String.format(
				"%f does not lie between the min (%f) and max (%f) values (inclusive)", result, min, max), context, this);
		}
		
		return next.execute(result, context);
	}
	
}
