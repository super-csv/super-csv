package org.supercsv.cellprocessor.constraint;

import java.util.Collection;
import java.util.HashSet;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * This processor ensures that the input String has a length equal to any of the supplied lengths. The length
 * constraints must all be > 0 or an exception is thrown. Lookup time is O(1).
 * 
 * @author Kasper B. Graversen
 * @author Dominique De Vito
 */
public class Strlen extends CellProcessorAdaptor implements StringCellProcessor {
	
	/** Set of all accepted lengths */
	protected HashSet<Integer> requiredLengths = new HashSet<Integer>();
	
	/**
	 * Constructs a new <tt>Strlen</tt> processor, which ensures that the input String has a length equal to any of the
	 * supplied lengths.
	 * 
	 * @param requiredLengths
	 *            one or more required lengths
	 */
	public Strlen(final int... requiredLengths) {
		super();
		addValues(requiredLengths);
	}
	
	/**
	 * Constructs a new <tt>Strlen</tt> processor, which ensures that the input String has a length equal to the
	 * supplied length, then calls the next processor in the chain.
	 * 
	 * @param requiredLength
	 *            the required length
	 * @param next
	 *            the next processor in the chain
	 */
	public Strlen(final int requiredLength, final CellProcessor next) {
		this(new int[] { requiredLength }, next);
	}
	
	/**
	 * Constructs a new <tt>Strlen</tt> processor, which ensures that the input String has a length equal to any of the
	 * supplied lengths, then calls the next processor in the chain.
	 * 
	 * @param requiredLengths
	 *            one or more required lengths
	 * @param next
	 *            the next processor in the chain
	 */
	public Strlen(final int[] requiredLengths, final CellProcessor next) {
		super(next);
		addValues(requiredLengths);
	}
	
	/**
	 * Ensure the suppled lengths are non-negative.
	 * 
	 * @param requiredLengths
	 *            one or more required lengths
	 */
	protected void addValues(final int... requiredLengths) {
		for( final int length : requiredLengths ) {
			if( length < 0 ) {
				throw new SuperCSVException("Cannot accept length below 0", this);
			}
			this.requiredLengths.add(length);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object execute(final Object value, final CSVContext context) {
		validateInputNotNull(value, context, this);
		final String stringValue = (value == null) ? null : value.toString(); // cast
		final int stringLength = (stringValue == null) ? 0 : stringValue.length();
		
		// check for required lengths
		if( !requiredLengths.contains(stringLength) ) {
			throw new SuperCSVException("Entry \"" + value + "\" is not of any of the required lengths "
				+ printRequiredLengths(requiredLengths), context, this);
		}
		
		return next.execute(value, context);
	}
	
	/**
	 * Assembles the Collection of required lengths into a String.
	 * 
	 * @param requiredLengths
	 *            the required lengths
	 * @return the assembled String
	 */
	private static String printRequiredLengths(Collection<Integer> requiredLengths) {
		
		final StringBuilder sb = new StringBuilder();
		String currentSeparator = "";
		String separator = ", ";
		for( final int length : requiredLengths ) {
			sb.append(currentSeparator);
			sb.append(length);
			currentSeparator = separator;
		}
		return sb.toString();
	}
}
