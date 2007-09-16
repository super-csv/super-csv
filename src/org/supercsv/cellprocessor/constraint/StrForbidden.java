/**
 *
 */
package org.supercsv.cellprocessor.constraint;

import java.util.List;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * Convert to string and ensure the input string is not present in a set of specified strings. Such constraint may be
 * handy when reading/writting e.g. filenames and wanting to ensure no filename contains e.g. ":", "/", ...
 * 
 * @author Kasper B. Graversen
 */
public class StrForbidden extends CellProcessorAdaptor implements StringCellProcessor {

	String[] forbiddenStrings;

	public StrForbidden(final List<String> forbiddenStrings) {
		this(forbiddenStrings.toArray(new String[0]));
	}

	public StrForbidden(final List<String> forbiddenStrings, final CellProcessor next) {
		this(forbiddenStrings.toArray(new String[0]), next);
	}

	public StrForbidden(final String forbiddenString) {
		this(new String[] { forbiddenString });
	}

	public StrForbidden(final String forbiddenString, final CellProcessor next) {
		this(new String[] { forbiddenString }, next);
	}

	public StrForbidden(final String[] forbiddenStrings) {
		super();
		this.forbiddenStrings = forbiddenStrings.clone();
	}

	public StrForbidden(final String[] forbiddenStrings, final CellProcessor next) {
		super(next);
		this.forbiddenStrings = forbiddenStrings.clone();
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

		// check for forbidden strings
		for(final String forbidden : forbiddenStrings) {
			if(sval.indexOf(forbidden) != -1)
				throw new SuperCSVException("Entry \"" + value + "\" on line " + context.lineNumber + " column " + context.columnNumber + " contains the forbidden char \"" + forbidden + "\"");
		}

		return next.execute(value, context);
	}
}