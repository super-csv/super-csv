package org.supercsv.cellprocessor;

import org.supercsv.cellprocessor.ift.BoolCellProcessor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.ift.DateCellProcessor;
import org.supercsv.cellprocessor.ift.DoubleCellProcessor;
import org.supercsv.cellprocessor.ift.LongCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.util.CSVContext;

/**
 * This processor returns a specified default value if the input is <tt>null</tt>. This is handy when writing partially
 * filled beans, maps and arrays, as for each column a default value can be specified.
 * <p>
 * To return the String <tt>""</tt> when a null is encountered use <code>
 * new ConvertNullTo("\"\"");
 * </code>
 * <p>
 * If you need further processing of the value in case the value is not <tt>null</tt>, you can link the processor with
 * other processors such as <code>
 * new ConvertNullTo("\"\"", new Trim(3))
 * </code>
 * 
 * @since 1.20
 * @author Kasper B. Graversen
 */
public class ConvertNullTo extends CellProcessorAdaptor implements DateCellProcessor, DoubleCellProcessor,
	LongCellProcessor, StringCellProcessor, BoolCellProcessor {
	
	Object returnValue = "";
	
	/**
	 * Constructs a new <tt>ConvertNullTo</tt> processor, which returns a specified default value if the input is
	 * <tt>null</tt>.
	 * 
	 * @param returnValue
	 *            the value to return if the input is <tt>null</tt>
	 */
	public ConvertNullTo(final Object returnValue) {
		super();
		this.returnValue = returnValue;
	}
	
	/**
	 * Constructs a new <tt>ConvertNullTo</tt> processor, which returns a specified default value if the input is
	 * <tt>null</tt>. If the input is not <tt>null</tt>, then the next processor is executed. 
	 * 
	 * @param returnValue
	 *            the value to return if the input is <tt>null</tt>
	 * @param next
	 *            the next <tt>CellProcessor</tt> in the chain
	 */
	public ConvertNullTo(final Object returnValue, final CellProcessor next) {
		super(next);
		this.returnValue = returnValue;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object execute(final Object value, final CSVContext context) {
		if( value == null ) {
			return returnValue;
		}
		
		return next.execute(value, context);
	}
}
