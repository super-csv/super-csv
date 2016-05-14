package org.supercsv.io.declarative.provider;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.declarative.annotation.Truncate;

public class TruncateCellProcessorProvider implements CellProcessorProvider<Truncate> {
	
	public CellProcessor create(Truncate annotation) {
		return new org.supercsv.cellprocessor.Truncate(annotation.maxSize(), annotation.suffix());
	}
	
	public Class<Truncate> getType() {
		return Truncate.class;
	}
	
}
