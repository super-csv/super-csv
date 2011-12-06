package org.supercsv.cellprocessor;

import static org.supercsv.util.Util.addAll;

import java.util.HashSet;
import java.util.Set;

import org.supercsv.cellprocessor.ift.BoolCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.ClassCastInputCSVException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * Converts a String to a Boolean.
 * <p>
 * The default values for true are: <tt>"true", "1", "y", "t"</tt>
 * <p>
 * The default values for false are: <tt>"false", "0", "n", "f"</tt>
 * <p>
 * The input is converted to lowercase before comparison against the true/false values (to handle all variations of case
 * in the input), so if you supply your own true/false values then ensure they are lowercase.
 * 
 * @author Kasper B. Graversen
 * @author Dominique De Vito
 * @author James Bassett
 * @since 1.0
 */
public class ParseBool extends CellProcessorAdaptor implements StringCellProcessor {
	
	private static final String[] DEFAULT_TRUE_VALUES = new String[] { "1", "true", "t", "y" };
	private static final String[] DEFAULT_FALSE_VALUES = new String[] { "0", "false", "f", "n" };
	
	private final Set<String> trueValues = new HashSet<String>();
	private final Set<String> falseValues = new HashSet<String>();
	
	/**
	 * Constructs a new <tt>ParseBool</tt> processor, which converts a String to a Boolean using the default values.
	 */
	public ParseBool() {
		this(DEFAULT_TRUE_VALUES, DEFAULT_FALSE_VALUES);
	}
	
	/**
	 * Constructs a new <tt>ParseBool</tt> processor, which converts a String to a Boolean using the default values,
	 * then calls the next processor in the chain.
	 * 
	 * @param next
	 *            the next processor in the chain
	 */
	public ParseBool(final BoolCellProcessor next) {
		this(DEFAULT_TRUE_VALUES, DEFAULT_FALSE_VALUES, next);
	}
	
	/**
	 * Constructs a new <tt>ParseBool</tt> processor, which converts a String to a Boolean using the supplied true/false
	 * values.
	 * 
	 * @param trueValue
	 *            the String which represents true
	 * @param falseValue
	 *            the String which represents false
	 */
	public ParseBool(final String trueValue, final String falseValue) {
		super();
		addAll(this.trueValues, trueValue);
		addAll(this.falseValues, falseValue);
	}
	
	/**
	 * Constructs a new <tt>ParseBool</tt> processor, which converts a String to a Boolean using the supplied true/false
	 * values.
	 * 
	 * @param trueValues
	 *            the array of Strings which represent true
	 * @param falseValues
	 *            the array of Strings which represent false
	 */
	public ParseBool(final String[] trueValues, final String[] falseValues) {
		super();
		addAll(this.trueValues, trueValues);
		addAll(this.falseValues, falseValues);
	}
	
	/**
	 * Constructs a new <tt>ParseBool</tt> processor, which converts a String to a Boolean using the supplied true/false
	 * values, then calls the next processor in the chain.
	 * 
	 * @param trueValue
	 *            the String which represents true
	 * @param falseValue
	 *            the String which represents false
	 * @param next
	 *            the next processor in the chain
	 */
	public ParseBool(final String trueValue, final String falseValue, final BoolCellProcessor next) {
		super(next);
		addAll(this.trueValues, trueValue);
		addAll(this.falseValues, falseValue);
	}
	
	/**
	 * Constructs a new <tt>ParseBool</tt> processor, which converts a String to a Boolean using the supplied true/false
	 * values, then calls the next processor in the chain.
	 * 
	 * @param trueValues
	 *            the array of Strings which represent true
	 * @param falseValues
	 *            the array of Strings which represent false
	 * @param next
	 *            the next processor in the chain
	 */
	public ParseBool(final String[] trueValues, final String[] falseValues, final BoolCellProcessor next) {
		super(next);
		addAll(this.trueValues, trueValues);
		addAll(this.falseValues, falseValues);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object execute(final Object value, final CSVContext context) {
		validateInputNotNull(value, context, this);
		
		if (!(value instanceof String)){
			throw new ClassCastInputCSVException("the value '" + value + "' is not of type String", context, this);
		}
		
		Boolean result;
		final String stringValue = ((String) value).toLowerCase();
		if( falseValues.contains(stringValue) ) {
			result = Boolean.FALSE;
		} else if( trueValues.contains(stringValue) ) {
			result = Boolean.TRUE;
		} else {
			throw new SuperCSVException("Cannot parse \"" + value + "\" to a boolean on line " + context.lineNumber
				+ " column " + context.columnNumber, context, this);
		}
		
		return next.execute(result, context);
	}
	
}
