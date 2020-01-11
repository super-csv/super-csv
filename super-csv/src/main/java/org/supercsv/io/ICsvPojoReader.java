package org.supercsv.io;

import org.supercsv.cellprocessor.ift.CellProcessor;

import java.io.IOException;

public interface ICsvPojoReader extends ICsvReader {

	<T> T read(Class<T> clazz, String... nameMapping) throws IOException;

	<T> T read(T pojo, String... nameMapping) throws IOException;

	<T> T read(Class<T> clazz, String[] nameMapping, CellProcessor... processors) throws IOException;

	<T> T read(T pojo, String[] nameMapping, CellProcessor... processors) throws IOException;

}
