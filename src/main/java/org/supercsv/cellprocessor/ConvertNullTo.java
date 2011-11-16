package org.supercsv.cellprocessor;

import org.supercsv.cellprocessor.ift.BoolCellProcessor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.ift.DateCellProcessor;
import org.supercsv.cellprocessor.ift.DoubleCellProcessor;
import org.supercsv.cellprocessor.ift.LongCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.util.CSVContext;

/**
 * This processor returns a specified default value, should the value given be <tt>null</tt>. This is handy when writing
 * partially filled beans, maps and arrays, as for each column a default value can be specified.
 * 
 * @since 1.20
 * @author Kasper B. Graversen
 */
public class ConvertNullTo extends CellProcessorAdaptor implements DateCellProcessor, DoubleCellProcessor,
	LongCellProcessor, StringCellProcessor, BoolCellProcessor {
	
	Object returnValue = "";
	
	/**
	 * To have the string <tt>""</tt> return when a null is encountered, use this class as <code>
	 * new ConvertNullTo("\"\"");
	 * </code>
	 * 
	 * @param returnValue
	 *            the value to return in case the input is <tt>null</tt>.
	 */
	public ConvertNullTo(final Object returnValue) {
		super();
		this.returnValue = returnValue;
	}
	
	/**
	 * Constructor To have the string <tt>""</tt> return when a null is encountered, use this class as <code>
	 * new ConvertNullTo("\"\"");
	 * </code>
	 * <p>
	 * If you need further processing of the value in case the value is not null, you can link the processor with other
	 * processors such as <code>
	 * new ConvertNullTo("\"\"", new Trim(3));
	 * </code>
	 * 
	 * @param returnValue
	 *            the value to return in case the input is <tt>null</tt>.
	 * @param next
	 *            Chained cell processor.
	 */
	public ConvertNullTo(final Object returnValue, final CellProcessor next) {
		super(next);
		this.returnValue = returnValue;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object execute(final Object value, final CSVContext context) {
		if( value == null ) {
			return returnValue;
		}
		
		return next.execute(value, context);
	}
}
