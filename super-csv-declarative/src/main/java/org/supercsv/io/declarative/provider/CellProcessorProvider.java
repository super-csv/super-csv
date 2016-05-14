package org.supercsv.io.declarative.provider;

import java.lang.annotation.Annotation;

import org.supercsv.cellprocessor.ift.CellProcessor;

public interface CellProcessorProvider<T extends Annotation> {
	CellProcessor create(T annotation);
	
	Class<T> getType();
}
