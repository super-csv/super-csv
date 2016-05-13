package org.supercsv.io.declarative;

import java.io.IOException;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.supercsv.cellprocessor.Transient;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvReflectionException;
import org.supercsv.io.AbstractCsvReader;
import org.supercsv.io.ITokenizer;
import org.supercsv.io.declarative.provider.CellProcessorProvider;
import org.supercsv.io.declarative.provider.ChainableCellProcessor;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.BeanInterfaceProxy;
import org.supercsv.util.Form;

public class DeclarativeBeanReader extends AbstractCsvReader {
	
	private final List<Object> processedColumns = new ArrayList<Object>();
	
	public DeclarativeBeanReader(final Reader reader, final CsvPreference preferences) {
		super(reader, preferences);
	}
	
	public DeclarativeBeanReader(final ITokenizer tokenizer, final CsvPreference preferences) {
		super(tokenizer, preferences);
	}
	
	public <T> T read(final Class<T> clazz) throws IOException {
		if( clazz == null ) {
			throw new IllegalArgumentException("clazz should not be null");
		}
		
		Field[] fields = clazz.getDeclaredFields();
		return readIntoBean(instantiateBean(clazz), fields, getCellProcessors(clazz, fields));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private <T> List<CellProcessor> getCellProcessors(Class<T> clazz, Field[] fields) {
		List<CellProcessor> result = new ArrayList<CellProcessor>();
		for( Field field : fields ) {
			List<Annotation> annotations = Arrays.asList(field.getAnnotations());
			Collections.reverse(annotations);
			
			CellProcessor currentProcessor = null;
			for( Annotation annotation : annotations ) {
				CellProcessorMarker cellProcessorMarker = annotation.getClass()
					.getAnnotation(CellProcessorMarker.class);
				if( cellProcessorMarker != null ) {
					CellProcessorProvider provider = instantiateBean(cellProcessorMarker.provider());
					if( !provider.getType().isAssignableFrom(annotation.getClass()) ) {
						throw new SuperCsvReflectionException(
							Form.at(
								"Provider declared in annotation of type '{}' cannot be used since accepted annotation-type is not compatible",
								annotation.getClass().getName()));
					}
					CellProcessor processor = provider.create(annotation);
					
					if( currentProcessor == null ) {
						currentProcessor = processor;
					} else {
						currentProcessor = new ChainableCellProcessor(currentProcessor, processor);
					}
				}
			}
			
			if( currentProcessor != null ) {
				result.add(currentProcessor);
			}
		}
		
		return result;
	}
	
	private static <T> T instantiateBean(final Class<T> clazz) {
		final T bean;
		if( clazz.isInterface() ) {
			bean = BeanInterfaceProxy.createProxy(clazz);
		} else {
			try {
				bean = clazz.newInstance();
			}
			catch(InstantiationException e) {
				throw new SuperCsvReflectionException(String.format(
					"error instantiating bean, check that %s has a default no-args constructor", clazz.getName()), e);
			}
			catch(IllegalAccessException e) {
				throw new SuperCsvReflectionException("error instantiating bean", e);
			}
		}
		
		return bean;
	}
	
	private <T> T populateBean(final T resultBean, Field[] fields) {
		for( int i = 0; i < fields.length; i++ ) {
			final Object fieldValue = processedColumns.get(i);
			
			if( fields[i] == null || fieldValue == null ) {
				continue;
			}
			
			try {
				fields[i].setAccessible(true);
				fields[i].set(resultBean, fieldValue);
			}
			catch(IllegalAccessException e) {
				throw new SuperCsvReflectionException(Form.at("Cannot set value on field '{}'", fields[i].getName()), e);
			}
			
		}
		
		return resultBean;
	}
	
	private <T> T readIntoBean(final T bean, Field[] fields, final List<CellProcessor> processors) throws IOException {
		
		if( readRow() ) {
			if( processors.size() < length() ) {
				for( int i = processors.size(); i < length(); i++ ) {
					processors.add(new Transient());
				}
			}
			
			executeProcessors(processedColumns, processors.toArray(new CellProcessor[processors.size()]));
			
			return populateBean(bean, fields);
		}
		
		return null; // EOF
	}
}
