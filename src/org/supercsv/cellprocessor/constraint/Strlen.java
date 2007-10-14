package org.supercsv.cellprocessor.constraint;

import java.util.HashMap;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * This processor requires that the input string has a given length (either chosen from one or out of a set of lengths).
 * The length constraints must all be > 0 or an exception is thrown Lookup time is O(1).
 * 
 * @author Kasper B. Graversen
 */
public class Strlen extends CellProcessorAdaptor implements StringCellProcessor {
	/** Map of all accepted lengths */
	protected HashMap<Integer, Object> requiredLengths = new HashMap<Integer, Object>();

	public Strlen(final int requiredLength) {
		this(new int[] { requiredLength });
	}

	public Strlen(final int requiredLength, final CellProcessor next) {
		this(new int[] { requiredLength });
	}

	public Strlen(final int[] requiredLengths) {
		super();
		addValues(requiredLengths);
	}

	public Strlen(final int[] requiredLengths, final CellProcessor next) {
		super(next);
		addValues(requiredLengths);
	}

	/** Ensure we only memorize valid lengths */
	protected void addValues(final int[] requiredLengths) throws SuperCSVException {
		for(final int length : requiredLengths) {
			if(length < 0) throw new SuperCSVException("Cannot accept length below 0");
			this.requiredLengths.put(length, null);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws SuperCSVException
	 *             upon receiving a string of an unaccepted length
	 * @throws ClassCastException
	 *             is the parameter value cannot be cast to a String
	 * @return the argument value if the value is unique
	 */
	@Override
	public Object execute(final Object value, final CSVContext context) throws SuperCSVException, ClassCastException {
		final String sval = value.toString(); // cast

		// check for required lengths
		if(!requiredLengths.containsKey(sval.length())) {

			// create string of required lengths
			final StringBuilder sb = new StringBuilder();
			for(final int length : requiredLengths.keySet())
				sb.append(length + ", ");
			sb.deleteCharAt(sb.length() - 2); // delete last comma

			throw new SuperCSVException("Entry \"" + value + "\" on line " + context.lineNumber + " column " + context.columnNumber + " is not of any of the required lengths " + sb.toString());
		}

		return next.execute(value, context);
	}
}