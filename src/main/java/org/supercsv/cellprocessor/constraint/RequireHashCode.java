package org.supercsv.cellprocessor.constraint;

import java.util.HashSet;

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
 * This processor converts the input to a String, and ensures that the input's hash function matches any of a given set
 * of hashcodes. Lookup time is O(1).
 * <p>
 * This constraint is a very efficient way of ensuring constant expressions are present in certain columns of the CSV
 * file, such as "BOSS", "EMPLOYEE", or when a column denotes an action to be taken for the input line such as "D"
 * (delete), "I" (insert), ...
 * <p>
 * 
 * @since 1.50
 * @author Kasper B. Graversen
 */
public class RequireHashCode extends CellProcessorAdaptor implements BoolCellProcessor, DateCellProcessor,
	DoubleCellProcessor, LongCellProcessor, StringCellProcessor {
	
	protected HashSet<Integer> requiredHashCodes = new HashSet<Integer>();
	
	/**
	 * Constructs a new <tt>RequireHashCode</tt> processor, which converts the input to a String, and ensures that the
	 * input's hash function matches any of a given set of hashcodes.
	 * 
	 * @param requiredHashcodes
	 *            one or more hashcodes
	 */
	public RequireHashCode(final int... requiredHashcodes) {
		super();
		addValues(requiredHashcodes);
	}
	
	/**
	 * Constructs a new <tt>RequireHashCode</tt> processor, which converts the input to a String, ensures that the
	 * input's hash function matches the supplied hashcode, then calls the next processor in the chain.
	 * 
	 * @param requiredHashcode
	 *            the required hashcode
	 * @param next
	 *            the next processor in the chain
	 */
	public RequireHashCode(final int requiredHashcode, final CellProcessor next) {
		this(new int[] { requiredHashcode }, next);
	}
	
	/**
	 * Constructs a new <tt>RequireHashCode</tt> processor, which converts the input to a String, ensures that the
	 * input's hash function matches any of a given set of hashcodes, then calls the next processor in the chain.
	 * 
	 * @param requiredHashcodes
	 *            one or more hashcodes
	 * @param next
	 *            the next processor in the chain
	 */
	public RequireHashCode(final int[] requiredHashcodes, final CellProcessor next) {
		super(next);
		addValues(requiredHashcodes);
	}
	
	/**
	 * Ensures that there are no duplicate hashcodes supplied.
	 * 
	 * @param requiredHashcodes
	 *            the supplied hashcodes
	 */
	protected void addValues(final int... requiredHashcodes) {
		for( final int hash : requiredHashcodes ) {
			if( !requiredHashCodes.add(hash) ) {
				throw new SuperCSVException("Cannot accept two identical hash codes", this);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object execute(final Object value, final CSVContext context) {
		validateInputNotNull(value, context, this);
		// check for required hash
		if( !requiredHashCodes.contains(value.hashCode()) ) {
			// create string of required hashes for error msg
			final StringBuilder sb = new StringBuilder();
			for( final int hash : requiredHashCodes ) {
				sb.append(hash + ", ");
			}
			sb.deleteCharAt(sb.length() - 1); // delete last comma
			
			throw new SuperCSVException("Entry \"" + value + "\" on line " + context.lineNumber + " column "
				+ context.columnNumber + " has hashcode " + value.hashCode()
				+ " which is not one of the required hash codes: " + sb.toString(), context, this);
		}
		
		return next.execute(value, context);
	}
	
}
