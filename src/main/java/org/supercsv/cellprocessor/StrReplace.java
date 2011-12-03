package org.supercsv.cellprocessor;

import java.util.regex.Pattern;

import org.supercsv.cellprocessor.ift.BoolCellProcessor;
import org.supercsv.cellprocessor.ift.DateCellProcessor;
import org.supercsv.cellprocessor.ift.DoubleCellProcessor;
import org.supercsv.cellprocessor.ift.LongCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * Replaces each substring of the input string that matches the given regular expression with the given replacement. The
 * regular expression pattern is compiled once then reused for efficiency.
 * 
 * @author Kasper B. Graversen
 * @author Dominique De Vito
 * @author James Bassett
 */
public class StrReplace extends CellProcessorAdaptor implements BoolCellProcessor, DateCellProcessor,
	DoubleCellProcessor, LongCellProcessor, StringCellProcessor {
	
	private final Pattern regexPattern;
	private final String replacement;
	
	/**
	 * Constructs a new <tt>StrReplace</tt> processor, which replaces each substring of the input that matches the regex
	 * with the supplied replacement.
	 * 
	 * @param regex
	 *            the regular expression to match
	 * @param replacement
	 *            the string to be substituted for each match
	 */
	public StrReplace(final String regex, final String replacement) {
		super();
		validateArguments(regex, replacement);
		this.regexPattern = Pattern.compile(regex);
		this.replacement = replacement;
	}
	
	/**
	 * Constructs a new <tt>StrReplace</tt> processor, which replaces each substring of the input that matches the regex
	 * with the supplied replacement, then calls the next processor in the chain.
	 * 
	 * @param regex
	 *            the regular expression to match
	 * @param replacement
	 *            the string to be substituted for each match
	 * @param next
	 *            the next processor in the chain
	 */
	public StrReplace(final String regex, final String replacement, final StringCellProcessor next) {
		super(next);
		validateArguments(regex, replacement);
		this.regexPattern = Pattern.compile(regex);
		this.replacement = replacement;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object execute(final Object value, final CSVContext context) {
		validateInputNotNull(value, context, this);
		String result = regexPattern.matcher(value.toString()).replaceAll(replacement);
		return next.execute(result, context);
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
}
