package org.supercsv.cellprocessor.constraint;

import java.util.HashMap;
import java.util.regex.Pattern;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * This constraint ensures that the input data matches the given regular expression.
 * 
 * @author Dominique De Vito
 * @since 1.50
 */
public class StrRegEx extends CellProcessorAdaptor implements StringCellProcessor {
	
	private final String regex;
	private final Pattern regexPattern;
	
	private static final HashMap<String, String> REGEX_MSGS = new HashMap<String, String>();
	
	/**
	 * Constructs a new <tt>StrRegEx</tt> processor, which ensures that the input data matches the given regular
	 * expression.
	 * 
	 * @param regex
	 *            the regular expression to match
	 */
	public StrRegEx(final String regex) {
		super();
		validateArguments(regex);
		this.regexPattern = Pattern.compile(regex);
		this.regex = regex;
	}
	
	/**
	 * Constructs a new <tt>StrRegEx</tt> processor, which ensures that the input data matches the given regular
	 * expression, then calls the next processor in the chain.
	 * 
	 * @param regex
	 *            the regular expression to match
	 * @param next
	 *            the next processor in the chain
	 */
	public StrRegEx(final String regex, final StringCellProcessor next) {
		super(next);
		validateArguments(regex);
		this.regexPattern = Pattern.compile(regex);
		this.regex = regex;
	}
	
	/**
	 * Validates that the supplied arguments are correct.
	 * 
	 * @param regex
	 *            the supplied regex
	 */
	private void validateArguments(final String regex) {
		if( regex == null ) {
			throw new NullInputException("the regular expression cannot be null", this);
		}
		if( regex.equals("") ) {
			throw new SuperCSVException("the regular expression  cannot be \"\" as this has no effect", this);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object execute(final Object value, final CSVContext context) {
		validateInputNotNull(value, context, this);
		
		boolean found = regexPattern.matcher((String) value).find();
		if( !found ) {
			String msg = REGEX_MSGS.get(regex);
			if( msg == null ) {
				throw new SuperCSVException("Entry \"" + value + "\" does not respect the regular expression '" + regex
					+ "'", context, this);
			} else {
				throw new SuperCSVException("Entry \"" + value + "\" does not respect the constraint '" + msg + "' "
					+ "(defined by the regular expression '" + regex + "')", context, this);
			}
		}
		return next.execute(value, context);
	}
	
	/**
	 * Register a message detailing in plain language the constraint representing a regular expression. For example, the
	 * regular expression \d{0,6}(\.\d{0,3})? could be associated with the message
	 * "up to 6 digits whole digits, followed by up to 3 fractional digits".
	 * 
	 * @param regex
	 *            the regular expression
	 * @param message
	 *            the message to associate with the regex
	 */
	public static void registerMessage(String regex, String message) {
		REGEX_MSGS.put(regex, message);
	}
	
}
