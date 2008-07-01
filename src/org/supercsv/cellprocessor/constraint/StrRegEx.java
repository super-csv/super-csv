package org.supercsv.cellprocessor.constraint;

import java.util.HashMap;
import java.util.regex.Pattern;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * This constraint ensures that the input data 
 * matches the given regular expression.
 * 
 * @author Dominique De Vito
 * @since 1.50
 */
public class StrRegEx extends CellProcessorAdaptor implements StringCellProcessor {

private final String regex;
private final Pattern regexPattern;

private static final HashMap<String, String> regexMessages = new HashMap<String, String>();

public StrRegEx(final String regex) {
	super();
	handleArguments(regex); 
	this.regexPattern = Pattern.compile(regex);
	this.regex = regex;
}

public StrRegEx(final String regex, final StringCellProcessor next) {
	super(next);
	handleArguments(regex); 
	this.regexPattern = Pattern.compile(regex);
	this.regex = regex;
}

private void handleArguments(final String regex)  {
	if( regex == null ) { throw new NullInputException("the regular expression cannot be null", this); }
	if( regex.equals("") ) { throw new SuperCSVException(
		"the regular expression  cannot be \"\" as this has no effect", this); }
}
/**
 * {@inheritDoc}
 */
@Override
public Object execute(final Object value, final CSVContext context) {
	if( value == null ) { throw new NullInputException("Input cannot be null on line " + context.lineNumber
		+ " at column " + context.columnNumber, context, this); }
	
	boolean found = regexPattern.matcher((String) value).find();
	if (!found) {
		String msg = regexMessages.get(regex);
		if (msg == null) {
		throw new SuperCSVException(
			"Entry \"" + value + "\" does not respect the regular expression '" + regex + "'", context, this);
		} else {
			throw new SuperCSVException(
				"Entry \"" + value + "\" does not respect the constraint '" + msg + "' " +
						"(defined by the regular expression '" + regex + "')", context, this);			
		}
	}
	return next.execute(value, context);
}


/**
 * Register a message detailing in plain language
 * the constraint representing a regular expression.
 * For example, the regular expression \d{0,6}(\.\d{0,3})?
 * could be associated with the message "up to 6 digits 
 * into the natural part, and up to 3 digit into the decimal part".
 * @param regex
 * @param message
 */
public static void registerMessage(String regex, String message) {
	regexMessages.put(regex, message);
}


}
