package org.supercsv.cellprocessor;

import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * Convert a String to a BigDecimal. It uses the String constructor of BigDecimal (<tt>new BigDecimal("0.1")</tt>) as it
 * yields predictable results (see {@link BigDecimal}).
 * <p>
 * If the data uses a character other than "." as a decimal separator (France uses "," for example), then use the constructor
 * that accepts a <tt>DecimalFormatSymbols</tt> object, as it will convert the character to a "." before creating the
 * BigDecimal.
 * 
 * @since 1.30
 * @author Kasper B. Graversen
 */
public class ParseBigDecimal extends CellProcessorAdaptor implements StringCellProcessor {
	
	private DecimalFormatSymbols symbols;
	
	/**
	 * Constructs a new <tt>ParseBigDecimal</tt> processor, which converts a String to a BigDecimal.
	 */
	public ParseBigDecimal() {
		this((DecimalFormatSymbols) null);
	}
	
	/**
	 * Constructs a new <tt>ParseBigDecimal</tt> processor, which converts a String to a BigDecimal using the supplied
	 * <tt>DecimalFormatSymbols</tt> object to convert any decimal separator to a "." before creating the BigDecimal.
	 * 
	 * @param symbols
	 *            the decimal format symbols, containing the decimal separator
	 */
	public ParseBigDecimal(DecimalFormatSymbols symbols) {
		super();
		this.symbols = symbols;
	}
	
	/**
	 * Constructs a new <tt>ParseBigDecimal</tt> processor, which converts a String to a BigDecimal then calls the next
	 * processor in the chain.
	 * 
	 * @param next
	 *            the next processor in the chain
	 */
	public ParseBigDecimal(final CellProcessor next) {
		this(null, next);
	}
	
	/**
	 * Constructs a new <tt>ParseBigDecimal</tt> processor, which converts a String to a BigDecimal using the supplied
	 * <tt>DecimalFormatSymbols</tt> object to convert any decimal separator to a "." before creating the BigDecimal, then
	 * calls the next processor in the chain.
	 * 
	 * @param symbols
	 *            the decimal format symbols, containing the decimal separator
	 * @param next
	 *            the next processor in the chain
	 */
	public ParseBigDecimal(DecimalFormatSymbols symbols, final CellProcessor next) {
		super(next);
		this.symbols = symbols;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object execute(final Object value, final CSVContext context) {
		validateInputNotNull(value, context, this);
		
		final BigDecimal result;
		if( value instanceof String ) {
			try {
				if( symbols == null ) {
					result = new BigDecimal((String) value);
				} else {
					if( symbols.getDecimalSeparator() != '.' ) {
						// replace any decimal separator in the input with "."
						String s = (String) value;
						result = new BigDecimal(s.replace(symbols.getDecimalSeparator(), '.'));
					} else {
						result = new BigDecimal((String) value);
					}
				}
			}
			catch(final Exception e) {
				throw new SuperCSVException("Parser error", context, this, e);
			}
		} else {
			throw new SuperCSVException("Can't convert \"" + value
				+ "\" to a BigDecimal. Input is not of type String, but of type " + value.getClass().getName(),
				context, this);
		}
		
		return next.execute(result, context);
	}
}
