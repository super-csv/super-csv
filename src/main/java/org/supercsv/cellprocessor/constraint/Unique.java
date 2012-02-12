package org.supercsv.cellprocessor.constraint;

import java.util.HashSet;
import java.util.Set;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * Ensure that upon processing a CSV file (reading or writing), that values of the column all are unique. Comparison is
 * based upon each elements <tt>equals()</tt> method of the objects and lookup takes O(1).
 * <P>
 * Compared to {@link UniqueHashCode} this processor potentially uses more memory, as it stores references to each
 * encountered object rather than just their hashcodes. On reading huge files this can be a real memory-hazard, however,
 * it ensures a true uniqueness check.
 * 
 * @since 1.50
 * @author Kasper B. Graversen
 * @author Dominique De Vito
 * @author James Bassett
 */
public class Unique extends CellProcessorAdaptor {
	
	private final Set<Object> encounteredElements = new HashSet<Object>();
	
	/**
	 * Constructs a new <tt>Unique</tt> processor, which ensures that all rows in a column are unique.
	 */
	public Unique() {
		super();
	}
	
	/**
	 * Constructs a new <tt>Unique</tt> processor, which ensures that all rows in a column are unique, then calls the
	 * next processor in the chain.
	 * 
	 * @param next
	 *            the next processor in the chain
	 * @throws NullPointerException
	 *             if next is null
	 */
	public Unique(final CellProcessor next) {
		super(next);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws NullInputException
	 *             if value is null
	 * @throws SuperCSVException
	 *             if a non-unique value is encountered
	 */
	public Object execute(final Object value, final CSVContext context) {
		validateInputNotNull(value, context);
		
		if( !encounteredElements.add(value) ) {
			throw new SuperCSVException(String.format("duplicate value '%s' encountered", value), context, this);
		}
		
		return next.execute(value, context);
	}
}
