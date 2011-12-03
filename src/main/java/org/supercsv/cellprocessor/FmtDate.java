package org.supercsv.cellprocessor;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.supercsv.cellprocessor.ift.DateCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.ClassCastInputCSVException;
import org.supercsv.util.CSVContext;

/**
 * Converts a date into a formatted string using the {@link SimpleDateFormat} class. If you want to convert from a
 * String to a Date, use the {@link ParseDate} processor.
 * <p>
 * Some example date formats you can use are:<br>
 * <code>"dd/MM/yyyy"</code> (formats a date as "25/12/2011")<br>
 * <code>"dd-MMM-yy"</code> (formats a date as "25-Dec-11")<br>
 * <code>"yyyy.MM.dd.HH.mm.ss"</code> (formats a date as "2011.12.25.08.36.33"<br>
 * <code>"E, dd MMM yyyy HH:mm:ss Z"</code> (formats a date as "Tue, 25 Dec 2011 08:36:33 -0500")<br>
 * 
 * @since 1.50
 * @author Dominique De Vito
 * @author James Bassett
 */
public class FmtDate extends CellProcessorAdaptor implements DateCellProcessor {
	
	protected String dateFormat;
	
	/**
	 * Constructs a new <tt>FmtDate</tt> processor, which converts a date into a formatted string using
	 * SimpleDateFormat.
	 * 
	 * @param dateFormat
	 *            the date format String (see {@link SimpleDateFormat})
	 */
	public FmtDate(final String dateFormat) {
		super();
		this.dateFormat = dateFormat;
	}
	
	/**
	 * Constructs a new <tt>FmtDate</tt> processor, which converts a date into a formatted string using
	 * SimpleDateFormat, then calls the next processor in the chain.
	 * 
	 * @param dateFormat
	 *            the date format String (see {@link SimpleDateFormat})
	 * @param next
	 *            the next processor in the chain
	 */
	public FmtDate(final String dateFormat, final StringCellProcessor next) {
		super(next);
		this.dateFormat = dateFormat;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object execute(final Object value, final CSVContext context) {
		validateInputNotNull(value, context, this);
		if( !(value instanceof Date) ) {
			throw new ClassCastInputCSVException("the value '" + value + "' is not of type Date", context, this);
		}
		
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		final String result = formatter.format((Date) value);
		return next.execute(result, context);
	}
}
