package org.supercsv.io.declarative.provider;

import java.lang.annotation.Annotation;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvReflectionException;
import org.supercsv.util.Form;

abstract class AbstractCellProcessorProvider<T extends CellProcessor, A extends Annotation> implements
	CellProcessorProvider<A> {
	private Class<T> cellProcessorClass;
	private Class<A> annotationClass;
	
	public AbstractCellProcessorProvider(Class<T> cellProcessorClass, Class<A> annotationClass) {
		this.cellProcessorClass = cellProcessorClass;
		this.annotationClass = annotationClass;
	}
	
	public CellProcessor create(Annotation annotation) {
		try {
			return cellProcessorClass.newInstance();
		}
		catch(InstantiationException e) {
			throw new SuperCsvReflectionException(Form.at("Cannot instantiate cellProcessor with type '{}'",
				cellProcessorClass.getName()), e);
		}
		catch(IllegalAccessException e) {
			throw new SuperCsvReflectionException(Form.at("Cannot access cellProcessor with type '{}'",
				cellProcessorClass.getName()), e);
		}
	}
	
	public Class<A> getType() {
		return annotationClass;
	}
	
}
