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
package org.supercsv.io.declarative;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ParseEnum;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvReflectionException;
import org.supercsv.io.declarative.provider.CellProcessorProvider;
import org.supercsv.util.CsvContext;
import org.supercsv.util.Form;
import org.supercsv.util.ReflectionUtils;

/**
 * Extracts all cellprocessor from all fields of the provided class
 * 
 * @since 2.5
 * @author Dominik Schlosser
 */
class BeanCellProcessorExtractor {
	private Map<Class<?>, List<CellProcessor>> CELL_PROCESSOR_CACHE = new HashMap<Class<?>, List<CellProcessor>>();
	
	private Map<Class<?>, CellProcessor> defaultProcessors;
	
	/**
	 * Constructor without default processors
	 */
	public BeanCellProcessorExtractor() {
		this(new HashMap<Class<?>, CellProcessor>());
	}
	
	/**
	 * Constructor that gets a map with default processors
	 * 
	 * @param defaultProcessors
	 *            default processors which are used when no {@link org.supercsv.io.declarative.CellProcessor}
	 *            -annotations can be found on a field
	 */
	public BeanCellProcessorExtractor(Map<Class<?>, CellProcessor> defaultProcessors) {
		this.defaultProcessors = defaultProcessors;
	}
	
	/**
	 * Extracts all cell processors from all fields of the provided class, including all superclass-fields
	 * 
	 * @param the
	 *            class to extract processors from
	 * @return all found cell processors
	 */
	public <T> List<CellProcessor> getCellProcessors(Class<T> clazz) {
		if( CELL_PROCESSOR_CACHE.containsKey(clazz) ) {
			return CELL_PROCESSOR_CACHE.get(clazz);
		}
		
		List<CellProcessor> cellProcessors = new ArrayList<CellProcessor>();
		for( Field field : ReflectionUtils.getFields(clazz) ) {
			List<CellProcessor> allAnnotatedProcessors = extractCellProcessors(field);
			if( allAnnotatedProcessors.isEmpty() ) {
				cellProcessors.add(mapFieldToDefaultProcessor(field));
			} else {
				CellProcessor chainedProcessor = new Transient();
				
				for( CellProcessor processor : allAnnotatedProcessors ) {
					chainedProcessor = new ChainableCellProcessor(chainedProcessor, processor);
				}
				
				cellProcessors.add(chainedProcessor);
			}
		}
		
		CELL_PROCESSOR_CACHE.put(clazz, cellProcessors);
		
		return cellProcessors;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<CellProcessor> extractCellProcessors(Field field) {
		List<CellProcessor> result = new ArrayList<CellProcessor>();
		List<Annotation> annotations = Arrays.asList(field.getAnnotations());
		Collections.reverse(annotations);
		
		for( Annotation annotation : annotations ) {
			org.supercsv.io.declarative.CellProcessor cellProcessorMarker = annotation.annotationType().getAnnotation(
				org.supercsv.io.declarative.CellProcessor.class);
			if( cellProcessorMarker != null ) {
				CellProcessorProvider provider = ReflectionUtils.instantiateBean(cellProcessorMarker.provider());
				if( !provider.getType().isAssignableFrom(annotation.getClass()) ) {
					throw new SuperCsvReflectionException(
						Form.at(
							"Provider declared in annotation of type '{}' cannot be used since accepted annotation-type is not compatible",
							annotation.getClass().getName()));
				}
				CellProcessor processor = provider.create(annotation);
				
				result.add(processor);
			}
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private CellProcessor mapFieldToDefaultProcessor(Field field) {
		if( field.getType().isEnum() ) {
			return new ParseEnum((Class<? extends Enum<?>>) field.getType());
		}
		
		CellProcessor cellProcessor = defaultProcessors.get(field.getType());
		if( cellProcessor == null ) {
			return new Transient();
		}
		
		return cellProcessor;
	}
	
	private static class Transient extends CellProcessorAdaptor {
		public <T> T execute(Object value, CsvContext context) {
			return next.execute(value, context);
		}
		
	}
}
