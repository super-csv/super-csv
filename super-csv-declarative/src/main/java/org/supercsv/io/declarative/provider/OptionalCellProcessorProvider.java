package org.supercsv.io.declarative.provider;

import org.supercsv.cellprocessor.Optional;

public class OptionalCellProcessorProvider extends
	AbstractCellProcessorProvider<Optional, org.supercsv.io.declarative.annotation.Optional> {
	
	public OptionalCellProcessorProvider() {
		super(Optional.class, org.supercsv.io.declarative.annotation.Optional.class);
	}
	
}
