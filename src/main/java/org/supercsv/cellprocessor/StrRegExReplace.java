package org.supercsv.cellprocessor;

import java.util.regex.Pattern;

import org.supercsv.cellprocessor.ift.BoolCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * Replaces each substring of the input string that matches the given regular expression with the given replacement.
 * 
 * @author Dominique De Vito
 * @author James Bassett
 * @since 1.50
 * @deprecated The original {@link StrReplace} processor is more useful (it handles any type of input) and allows other
 *             <tt>StringCellProcessor</tt>s to be chained to it. <tt>StrReplace</tt> has been updated to use a compiled regex
 *             Pattern as is done in this class, and it should be used instead.
 */
public class StrRegExReplace extends CellProcessorAdaptor implements StringCellProcessor {
	
	private final Pattern regexPattern;
	private final String replacement;
	
	/**
	 * Constructs a new <tt>StrRegExReplace</tt> processor, which replaces each substring of the input that matches the
	 * regex with the supplied replacement.
	 * 
	 * @param regex
	 *            the regular expression to match
	 * @param replacement
	 *            the string to be substituted for each match
	 */
	public StrRegExReplace(final String regex, final String replacement) {
		super();
		validateArguments(regex, replacement);
		this.regexPattern = Pattern.compile(regex);
		this.replacement = replacement;
	}
	
	/**
	 * Constructs a new <tt>StrRegExReplace</tt> processor, which replaces each substring of the input that matches the
	 * regex with the supplied replacement, then calls the next processor in the chain.
	 * 
	 * @param regex
	 *            the regular expression to match
	 * @param replacement
	 *            the string to be substituted for each match
	 * @param next
	 *            the next processor in the chain
	 */
	public StrRegExReplace(final String regex, final String replacement, final BoolCellProcessor next) {
		super(next);
		validateArguments(regex, replacement);
		this.regexPattern = Pattern.compile(regex);
		this.replacement = replacement;
	}
	
	/**
	 * Validates that the arguments are correct.
	 * 
	 * @param regex
	 *            the supplied regular expression
	 * @param replacement
	 *            the supplied replacement text
	 */
	private void validateArguments(final String regex, final String replacement) {
		if( regex == null ) {
			throw new NullInputException("the regular expression cannot be null", this);
		}
		if( replacement == null ) {
			throw new NullInputException("the replacement string cannot be null", this);
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
		String result = regexPattern.matcher((String) value).replaceAll(replacement);
		return next.execute(result, context);
	}
}
