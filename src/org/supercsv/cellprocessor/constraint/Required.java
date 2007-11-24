package org.supercsv.cellprocessor.constraint;

import java.util.HashMap;

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
 * This processor converts the input to a string, and enforces a requirement that the input's hash function returns any
 * of given hashcode (specified as one or one in a set of hash codes). Lookup time is O(1). This constraint is a very
 * efficient way of ensuring constant expressions are present in certain columns of the CSV file, such as "BOSS",
 * "EMPLOYEE", or when a column denote an action to be taken for the input line such as "D" (delete), "I" (insert), ...
 * 
 * @author Kasper B. Graversen
 */
public class Required extends CellProcessorAdaptor implements BoolCellProcessor, DateCellProcessor,
		DoubleCellProcessor, LongCellProcessor, StringCellProcessor {
	protected HashMap<Integer, Object>	requireds	= new HashMap<Integer, Object>();

	public Required(final int requiredHashcode, final CellProcessor next) {
		this(new int[] { requiredHashcode });
	}

	public Required(final int... requiredHashcodes) {
		super();
		addValues(requiredHashcodes);
	}

	public Required(final int[] requiredHashcodes, final CellProcessor next) {
		super(next);
		addValues(requiredHashcodes);
	}

	protected void addValues(final int[] requiredHashcodes) throws SuperCSVException {
		for(final int hash : requiredHashcodes) {
			if(requireds.containsKey(hash)) {
				throw new SuperCSVException("Cannot accept two identical hash codes");
			}
			requireds.put(hash, null);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws SuperCSVException
	 *             upon receiving a string of an un-accepted length
	 * @throws ClassCastException
	 *             is the parameter value cannot be cast to a String
	 * @return the argument value if the value is unique
	 */
	@Override
	public Object execute(final Object value, final CSVContext context) throws SuperCSVException, ClassCastException {
		// check for required hash
		if(!requireds.containsKey(value.hashCode())) {
			// create string of required hash'es for error msg
			final StringBuilder sb = new StringBuilder();
			for(final int hash : requireds.keySet()) {
				sb.append(hash + ", ");
			}
			sb.deleteCharAt(sb.length() - 1); // delete last comma

			throw new SuperCSVException("Entry \"" + value + "\" on line " + context.lineNumber + " column "
					+ context.columnNumber + " has hashcode " + value.hashCode()
					+ " which is not one of the required hash codes: " + sb.toString());
		}

		return next.execute(value, context);
	}
}
