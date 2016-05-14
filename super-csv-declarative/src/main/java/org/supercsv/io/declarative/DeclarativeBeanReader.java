package org.supercsv.io.declarative;

import java.io.IOException;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ParseBigDecimal;
import org.supercsv.cellprocessor.ParseBool;
import org.supercsv.cellprocessor.ParseChar;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ParseEnum;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvReflectionException;
import org.supercsv.io.AbstractCsvReader;
import org.supercsv.io.ITokenizer;
import org.supercsv.io.declarative.provider.CellProcessorProvider;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.BeanInterfaceProxy;
import org.supercsv.util.CsvContext;
import org.supercsv.util.Form;

public class DeclarativeBeanReader extends AbstractCsvReader {
	
	private final List<Object> processedColumns = new ArrayList<Object>();
	
	private static final Map<Class<?>, CellProcessor> DEFAULT_PROCESSORS = new HashMap<Class<?>, CellProcessor>();
	static {
		DEFAULT_PROCESSORS.put(BigDecimal.class, new ParseBigDecimal());
		DEFAULT_PROCESSORS.put(Boolean.class, new ParseBool());
		DEFAULT_PROCESSORS.put(boolean.class, new ParseBool());
		DEFAULT_PROCESSORS.put(Character.class, new ParseChar());
		DEFAULT_PROCESSORS.put(char.class, new ParseChar());
		DEFAULT_PROCESSORS.put(Double.class, new ParseDouble());
		DEFAULT_PROCESSORS.put(double.class, new ParseDouble());
		DEFAULT_PROCESSORS.put(Integer.class, new ParseInt());
		DEFAULT_PROCESSORS.put(int.class, new ParseInt());
		DEFAULT_PROCESSORS.put(Long.class, new ParseLong());
		DEFAULT_PROCESSORS.put(long.class, new ParseLong());
	}
	
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
		
		List<Field> fields = new ArrayList<Field>();
		extractFields(clazz, fields);
		
		return readIntoBean(instantiateBean(clazz), fields, getCellProcessors(clazz, fields));
	}
	
	private void extractFields(Class<?> clazz, List<Field> fields) {
		if( clazz.getSuperclass() != Object.class ) {
			extractFields(clazz.getSuperclass(), fields);
		}
		
		fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private <T> List<CellProcessor> getCellProcessors(Class<T> clazz, List<Field> fields) {
		List<CellProcessor> result = new ArrayList<CellProcessor>();
		for( Field field : fields ) {
			List<Annotation> annotations = Arrays.asList(field.getAnnotations());
			Collections.reverse(annotations);
			
			CellProcessor currentProcessor = mapFieldToDefaultProcessor(field);
			
			for( Annotation annotation : annotations ) {
				org.supercsv.io.declarative.CellProcessor cellProcessorMarker = annotation.annotationType()
					.getAnnotation(org.supercsv.io.declarative.CellProcessor.class);
				if( cellProcessorMarker != null ) {
					CellProcessorProvider provider = instantiateBean(cellProcessorMarker.provider());
					if( !provider.getType().isAssignableFrom(annotation.getClass()) ) {
						throw new SuperCsvReflectionException(
							Form.at(
								"Provider declared in annotation of type '{}' cannot be used since accepted annotation-type is not compatible",
								annotation.getClass().getName()));
					}
					CellProcessor processor = provider.create(annotation);
					
					currentProcessor = new ChainableCellProcessor(currentProcessor, processor);
				}
			}
			
			result.add(currentProcessor);
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private CellProcessor mapFieldToDefaultProcessor(Field field) {
		if( field.getType().isEnum() ) {
			return new ParseEnum((Class<? extends Enum<?>>) field.getType());
		}
		
		CellProcessor cellProcessor = DEFAULT_PROCESSORS.get(field.getType());
		if( cellProcessor == null ) {
			return new Transient();
		}
		
		return cellProcessor;
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
	
	private <T> T populateBean(final T resultBean, List<Field> fields) {
		for( int i = 0; i < fields.size(); i++ ) {
			final Object fieldValue = processedColumns.get(i);
			
			Field field = fields.get(i);
			if( field == null || fieldValue == null ) {
				continue;
			}
			
			try {
				field.setAccessible(true);
				field.set(resultBean, fieldValue);
			}
			catch(IllegalAccessException e) {
				throw new SuperCsvReflectionException(Form.at("Cannot set value on field '{}'", field.getName()), e);
			}
			
		}
		
		return resultBean;
	}
	
	private <T> T readIntoBean(final T bean, List<Field> fields, final List<CellProcessor> processors)
		throws IOException {
		
		if( readRow() ) {
			for( CellProcessor cellProcessor : processors ) {
				
				System.out.println(cellProcessor.getClass().getCanonicalName());
			}
			System.out.println("---");
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
	
	private static class Transient extends CellProcessorAdaptor {
		public <T> T execute(Object value, CsvContext context) {
			return next.execute(value, context);
		}
		
	}
}
