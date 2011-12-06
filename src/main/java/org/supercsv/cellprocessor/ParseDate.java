package org.supercsv.cellprocessor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.supercsv.cellprocessor.ift.DateCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.ClassCastInputCSVException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * Converts a String to a Date using the {@link SimpleDateFormat} class. If you want to convert from a Date to a String,
 * use the {@link FmtDate} processor.
 * <p>
 * Some example date formats you can use are:<br>
 * <code>"dd/MM/yyyy"</code> (parses a date formatted as "25/12/2011")<br>
 * <code>"dd-MMM-yy"</code> (parses a date formatted as "25-Dec-11")<br>
 * <code>"yyyy.MM.dd.HH.mm.ss"</code> (parses a date formatted as "2011.12.25.08.36.33"<br>
 * <code>"E, dd MMM yyyy HH:mm:ss Z"</code> (parses a date formatted as "Tue, 25 Dec 2011 08:36:33 -0500")<br>
 * 
 * @author Kasper B. Graversen
 * @author James Bassett
 */
public class ParseDate extends CellProcessorAdaptor implements StringCellProcessor {
	
	protected final String dateFormat;
	
	/**
	 * Constructs a new <tt>ParseDate</tt> processor which converts a String to a Date using the supplied date format.
	 * 
	 * @param dateFormat
	 *            the date format to use
	 */
	public ParseDate(final String dateFormat) {
		super();
		this.dateFormat = dateFormat;
	}
	
	/**
	 * Constructs a new <tt>ParseDate</tt> processor which converts a String to a Date using the supplied date format,
	 * then calls the next processor in the chain.
	 * 
	 * @param dateFormat
	 *            the date format to use
	 * @param next
	 *            the next processor in the chain
	 */
	public ParseDate(final String dateFormat, final DateCellProcessor next) {
		super(next);
		this.dateFormat = dateFormat;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object execute(final Object value, final CSVContext context) {
		validateInputNotNull(value, context, this);
		
		if (!(value instanceof String)){
			throw new ClassCastInputCSVException("the value '" + value + "' is not of type String", context, this);
		}
		
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
			formatter.setLenient(false);
			final Date result = formatter.parse((String) value);
			return next.execute(result, context);
		}
		catch(final ParseException e) {
			throw new SuperCSVException("Problems parsing '" + value + "' as a date", context, this, e);
		}
	}
}
