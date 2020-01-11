package org.supercsv.io;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.PojoUtils;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CsvPojoReader extends AbstractCsvReader implements ICsvPojoReader {
	private final List<Object> processedColumns = new ArrayList();

	public CsvPojoReader(Reader reader, CsvPreference preferences) {
		super(reader, preferences);
	}

	public CsvPojoReader(ITokenizer tokenizer, CsvPreference preferences) {
		super(tokenizer, preferences);
	}

	public <T> T read(Class<T> clazz, String... nameMapping) throws IOException {
		return readIntoPojo(newTClass(clazz), nameMapping, null);
	}

	public <T> T read(T pojo, String... nameMapping) throws IOException {
		return readIntoPojo(pojo, nameMapping, null);
	}

	public <T> T read(Class<T> clazz, String[] nameMapping, CellProcessor... processors) throws IOException {
		return readIntoPojo(newTClass(clazz), nameMapping, processors);
	}

	public <T> T read(T pojo, String[] nameMapping, CellProcessor... processors) throws IOException {
		return readIntoPojo(pojo, nameMapping, processors);
	}

	private <T> T newTClass(Class<T> clazz) {
		T pojo = null;
		try {
			pojo = clazz.newInstance();
		} catch (IllegalAccessException e) {
		} catch (InstantiationException e) {
		} finally {
			return pojo;
		}
	}

	private <T> T readIntoPojo(final T pojo, final String[] nameMapping, final CellProcessor[] processors) throws IOException {
		if (!readRow()) {
			return null;
		}
		if (nameMapping.length != length()) {
			throw new IllegalArgumentException();
		}
		if (processors == null) {
			processedColumns.clear();
			processedColumns.addAll(getColumns());
		} else {
			executeProcessors(processedColumns, processors);
		}
		return populatePojo(pojo, nameMapping);
	}

	private <T> T populatePojo(final T pojo, String[] nameMapping) {
		Field[] fields = PojoUtils.getAllFields(pojo.getClass());
		try {
			for (int i = 0; i < nameMapping.length; i++) {
				final Object fieldValue = processedColumns.get(i);
				Field field = PojoUtils.searchFieldByName(fields, nameMapping[i]);
				field.setAccessible(true);
				field.set(pojo, fieldValue);
			}
		} catch (IllegalAccessException e) {
		} finally {
			return pojo;
		}
	}
}
