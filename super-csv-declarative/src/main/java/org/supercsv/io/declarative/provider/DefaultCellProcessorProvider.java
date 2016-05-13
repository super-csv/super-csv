package org.supercsv.io.declarative.provider;

import java.lang.annotation.Annotation;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvReflectionException;
import org.supercsv.util.Form;

public class DefaultCellProcessorProvider<T extends CellProcessor> implements CellProcessorProvider<Annotation> {
	private Class<T> cellProcessorClass;
	
	public DefaultCellProcessorProvider(Class<T> cellProcessorClass) {
		this.cellProcessorClass = cellProcessorClass;
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
	
	public Class<Annotation> getType() {
		return Annotation.class;
	}
	
}
