package org.supercsv.cellprocessor.constraint;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * This constraint ensures that the input data has a string length between the supplied min and max values (both
 * inclusive). Should the input be anything different from a String, it will be converted to a string using the input's
 * <code>toString()</code> method.
 * 
 * @author Kasper B. Graversen
 */
public class StrMinMax extends CellProcessorAdaptor implements StringCellProcessor {
	protected long min, max;
	
	/**
	 * Constructs a new <tt>StrMinMax</tt> processor, which ensures that the input data has a string length between the
	 * supplied min and max values (both inclusive).
	 * 
	 * @param min
	 *            the minimum String length
	 * @param max
	 *            the maximum String length
	 */
	public StrMinMax(final long min, final long max) {
		super();
		if( max < min ) {
			throw new SuperCSVException("max < min in the arguments " + min + " " + max, this);
		}
		if( min < 0 ) {
			throw new SuperCSVException("min length must be >= 0, is " + min, this);
		}
		
		this.min = min;
		this.max = max;
	}
	
	/**
	 * Constructs a new <tt>StrMinMax</tt> processor, which ensures that the input data has a string length between the
	 * supplied min and max values (both inclusive), then calls the next processor in the chain.
	 * 
	 * @param min
	 *            the minimum String length
	 * @param max
	 *            the maximum String length
	 * @param next
	 *            the next processor in the chain
	 */
	public StrMinMax(final long min, final long max, final CellProcessor next) {
		super(next);
		if( max < min ) {
			throw new SuperCSVException("max < min in the arguments " + min + " " + max, this);
		}
		this.min = min;
		this.max = max;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object execute(final Object value, final CSVContext context) {
		validateInputNotNull(value, context, this);
		final String sval = value.toString(); // cast
		if( sval.length() < min || sval.length() > max ) {
			throw new SuperCSVException("Entry \"" + value + "\" on line " + context.lineNumber + " column "
				+ context.columnNumber + " is not within the string sizes " + min + " - " + max, context, this);
		}
		
		return next.execute(sval, context);
	}
}
