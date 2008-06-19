package org.supercsv.cellprocessor.constraint;


import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.ift.DateCellProcessor;
import org.supercsv.cellprocessor.ift.DoubleCellProcessor;
import org.supercsv.cellprocessor.ift.LongCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * This constraint ensures that all input data are equals,
 * eventually to a given value.
 * 
 * @author Dominique De Vito
 * @since 1.50
 */
public class Equals extends CellProcessorAdaptor
implements LongCellProcessor, DoubleCellProcessor, StringCellProcessor,
DateCellProcessor{

	private static final Object UNKNOWN = new Object();

	private Object constantValue;
	private boolean isGivenValue;
	
	public Equals() {
		super();
		constantValue = UNKNOWN;
		isGivenValue = false;
	}
	
	public Equals(Object constantValue) {
		super();
		this.constantValue = constantValue;
		isGivenValue = true;
	}

	public Equals(CellProcessor next) {
		super(next);
		constantValue = UNKNOWN;
		isGivenValue = false;
	}

	public Equals(Object constantValue, CellProcessor next) {
		super(next);
		this.constantValue = constantValue;
		isGivenValue = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object execute(Object value, CSVContext context) {
		if (constantValue == UNKNOWN) {
			constantValue = value;
		} else {
			if (!equals(constantValue, value)) {
				if (isGivenValue) {
					throw new SuperCSVException("Entry \"" + value + "\" is not equals " +
							"to the given value \"" + constantValue + "\"", context, this);
				} else {
					throw new SuperCSVException("Entry \"" + value + "\" is not equals " +
						"to the other previous value(s) being \"" + constantValue + "\"", context, this);
				}
			}
		}
		return next.execute(value, context);
	}
	
	private static boolean equals(Object o1, Object o2) {
		return (o1 == null) ? (o2 == null) : o1.equals(o2);
	}

}
