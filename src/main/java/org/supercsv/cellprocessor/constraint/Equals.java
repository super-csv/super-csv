package org.supercsv.cellprocessor.constraint;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.ift.DateCellProcessor;
import org.supercsv.cellprocessor.ift.DoubleCellProcessor;
import org.supercsv.cellprocessor.ift.LongCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * This constraint ensures that all input data is equal (to each other, or to a supplied constant value).
 * 
 * @author Dominique De Vito
 * @since 1.50
 */
public class Equals extends CellProcessorAdaptor implements LongCellProcessor, DoubleCellProcessor,
	StringCellProcessor, DateCellProcessor {
	
	private static final Object UNKNOWN = new Object();
	
	private Object constantValue;
	private boolean isGivenValue;
	
	/**
	 * Constructs a new <tt>Equals</tt> processor, which ensures all input data is equal.
	 */
	public Equals() {
		super();
		constantValue = UNKNOWN;
		isGivenValue = false;
	}
	
	/**
	 * Constructs a new <tt>Equals</tt> processor, which ensures all input data is equal to the supplied constant value.
	 * 
	 * @param constantValue
	 *            the constant value that all input must equal
	 */
	public Equals(Object constantValue) {
		super();
		this.constantValue = constantValue;
		isGivenValue = true;
	}
	
	/**
	 * Constructs a new <tt>Equals</tt> processor, which ensures all input data is equal, then calls the the next
	 * processor in the chain.
	 * 
	 * @param next
	 *            the next processor in the chain
	 */
	public Equals(CellProcessor next) {
		super(next);
		constantValue = UNKNOWN;
		isGivenValue = false;
	}
	
	/**
	 * Constructs a new <tt>Equals</tt> processor, which ensures all input data is equal to the supplied constant value,
	 * then calls the the next processor in the chain.
	 * 
	 * @param next
	 *            the next processor in the chain
	 * @param constantValue
	 *            the constant value that all input must equal
	 * @param next
	 *            the next processor in the chain
	 */
	public Equals(Object constantValue, CellProcessor next) {
		super(next);
		this.constantValue = constantValue;
		isGivenValue = true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object execute(Object value, CSVContext context) {
		if( UNKNOWN.equals(constantValue) ) {
			constantValue = value;
		} else {
			if( !equals(constantValue, value) ) {
				if( isGivenValue ) {
					throw new SuperCSVException("Entry \"" + value + "\" is not equal "
						+ "to the supplied constant value \"" + constantValue + "\"", context, this);
				} else {
					throw new SuperCSVException("Entry \"" + value + "\" is not equal "
						+ "to the other previous value(s) of \"" + constantValue + "\"", context, this);
				}
			}
		}
		return next.execute(value, context);
	}
	
	/**
	 * Returns true if both objects are null or equal, otherwise false.
	 * 
	 * @param o1
	 *            the first object
	 * @param o2
	 *            the second object
	 * @return true if both objects are null or equal, otherwise false
	 */
	private static boolean equals(Object o1, Object o2) {
		return (o1 == null) ? (o2 == null) : o1.equals(o2);
	}
	
}
