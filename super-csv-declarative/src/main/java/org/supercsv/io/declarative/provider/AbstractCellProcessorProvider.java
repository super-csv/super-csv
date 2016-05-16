/*
 * Copyright 2007 Kasper B. Graversen
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.supercsv.io.declarative.provider;

import java.lang.annotation.Annotation;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvReflectionException;
import org.supercsv.util.Form;

/**
 * Convenience-base class for simple CellProcessor-annotation providers without parameters
 * 
 * @since 2.5
 * @author Dominik Schlosser
 */
abstract class AbstractCellProcessorProvider<T extends CellProcessor, A extends Annotation> implements
	CellProcessorProvider<A> {
	private Class<T> cellProcessorClass;
	private Class<A> annotationClass;
	
	/**
	 * @param cellProcessorClass
	 *            the class of the cell processor to create
	 * @param annotationClass
	 *            the class of the cell processor annotation
	 */
	public AbstractCellProcessorProvider(Class<T> cellProcessorClass, Class<A> annotationClass) {
		this.cellProcessorClass = cellProcessorClass;
		this.annotationClass = annotationClass;
	}
	
	/**
	 * {@inheritDoc}
	 */
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
	
	/**
	 * {@inheritDoc}
	 */
	public Class<A> getType() {
		return annotationClass;
	}
	
}
