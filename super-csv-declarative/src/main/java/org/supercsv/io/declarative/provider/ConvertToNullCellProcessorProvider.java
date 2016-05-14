package org.supercsv.io.declarative.provider;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.declarative.annotation.ConvertNullTo;

public class ConvertToNullCellProcessorProvider implements CellProcessorProvider<ConvertNullTo> {
	
	public CellProcessor create(ConvertNullTo annotation) {
		return new org.supercsv.cellprocessor.ConvertNullTo(annotation.value());
	}
	
	public Class<ConvertNullTo> getType() {
		return ConvertNullTo.class;
	}
	
}
