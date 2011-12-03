package org.supercsv.cellprocessor.constraint;

import java.util.HashSet;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * Ensure that upon processing a CSV file (reading or writing), that values of the column are all unique. Comparison is
 * based upon each elements <tt>hashCode()</tt> method and lookup takes O(1).
 * <p>
 * Compared to {@link Unique} this processor is much more memory efficient as it only stores the set of encountered
 * hashcodes rather than storing references to all encountered objects. The tradeoff being possible false positives.
 * <p>
 * Prior to v1.50 this class was named <tt>Unique</tt> but has been renamed to clarify its inner workings.
 * 
 * @author Kasper B. Graversen
 * @author Dominique De Vito
 * @author James Bassett
 */
public class UniqueHashCode extends CellProcessorAdaptor {
	
	protected HashSet<Integer> uniqueSet = new HashSet<Integer>();
	
	/**
	 * Constructs a new <tt>UniqueHashCode</tt> processor, which ensures that all rows in a column are unique.
	 */
	public UniqueHashCode() {
		super();
	}
	
	/**
	 * Constructs a new <tt>UniqueHashCode</tt> processor, which ensures that all rows in a column are unique, then
	 * calls the next processor in the chain.
	 * 
	 * @param next
	 *            the next processor in the chain
	 */
	public UniqueHashCode(final CellProcessor next) {
		super(next);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object execute(final Object value, final CSVContext context) {
		validateInputNotNull(value, context, this);
		
		// check for uniqueness
		if( !uniqueSet.add(value.hashCode()) ) {
			throw new SuperCSVException("Duplicate entry \"" + value + "\" found with same hash code!", context, this);
		}
		
		return next.execute(value, context);
	}
}
