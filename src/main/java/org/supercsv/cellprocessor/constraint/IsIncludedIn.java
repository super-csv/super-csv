package org.supercsv.cellprocessor.constraint;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.BoolCellProcessor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.ift.DateCellProcessor;
import org.supercsv.cellprocessor.ift.DoubleCellProcessor;
import org.supercsv.cellprocessor.ift.LongCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * This processor ensures that the input value belongs to a specific set of given values.
 * 
 * @since 1.50
 * @author Dominique De Vito
 */
public class IsIncludedIn extends CellProcessorAdaptor implements BoolCellProcessor, DateCellProcessor,
	DoubleCellProcessor, LongCellProcessor, StringCellProcessor {
	protected Set<Object> possibleValues;
	
	/**
	 * Constructs a new <tt>IsIncludedIn</tt> processor, which ensures that the input value belongs to a specific set of
	 * given values.
	 * 
	 * @param possibleValues
	 *            the Set of values
	 */
	public IsIncludedIn(final Set<Object> possibleValues) {
		super();
		this.possibleValues = possibleValues;
	}
	
	/**
	 * Constructs a new <tt>IsIncludedIn</tt> processor, which ensures that the input value belongs to a specific set of
	 * given values, then calls the next processor in the chain.
	 * 
	 * @param possibleValues
	 *            the Set of values
	 * @param next
	 *            the next processor in the chain
	 */
	public IsIncludedIn(final Set<Object> possibleValues, final CellProcessor next) {
		super(next);
		this.possibleValues = possibleValues;
	}
	
	/**
	 * Constructs a new <tt>IsIncludedIn</tt> processor, which ensures that the input value belongs to a specific set of
	 * given values.
	 * 
	 * @param possibleValues
	 *            the array of values
	 */
	public IsIncludedIn(final Object[] possibleValues) {
		super();
		this.possibleValues = createSet(possibleValues);
	}
	
	/**
	 * Constructs a new <tt>IsIncludedIn</tt> processor, which ensures that the input value belongs to a specific set of
	 * given values, then calls the next processor in the chain.
	 * 
	 * @param possibleValues
	 *            the array of values
	 * @param next
	 *            the next processor in the chain
	 */
	public IsIncludedIn(final Object[] possibleValues, final CellProcessor next) {
		super(next);
		this.possibleValues = createSet(possibleValues);
	}
	
	/**
	 * Creates a Set from the array of values.
	 * 
	 * @param values
	 *            the array of values
	 * @return a Set containing the values
	 */
	private static Set<Object> createSet(Object[] values) {
		int size = (values == null) ? 0 : values.length;
		HashSet<Object> set = new HashSet<Object>(size);
		if( size > 0 ) {
			Collections.addAll(set, values);
		}
		return set;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object execute(final Object value, final CSVContext context) {
		validateInputNotNull(value, context, this);
		if( !possibleValues.contains(value) ) {
			throw new SuperCSVException("Entry \"" + value + "\" on line " + context.lineNumber + " column "
				+ context.columnNumber + " is not accepted as a possible value", context, this);
		}
		
		return next.execute(value, context);
	}
}
