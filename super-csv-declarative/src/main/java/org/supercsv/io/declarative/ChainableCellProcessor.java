package org.supercsv.io.declarative;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CsvContext;

public class ChainableCellProcessor extends CellProcessorAdaptor {
	private CellProcessor adaptee;
	
	public ChainableCellProcessor(CellProcessor adaptee, CellProcessor next) {
		super(next);
		this.adaptee = adaptee;
	}
	
	public <T> T execute(Object value, CsvContext context) {
		return adaptee.execute(next.execute(value, context), context);
	}
}
