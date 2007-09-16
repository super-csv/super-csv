package org.supercsv.io;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCSVException;

/**
 * Supertype for readers reading into objects/beans.
 * 
 * @author Kasper B. Graversen
 */
public interface ICsvBeanReader extends ICsvReader {

	/**
	 * Read a line and populate an instance of the specified type. Since no processing of any fields
	 * 
	 * @param clazz
	 *            a class reference to the class to instantiate and populate
	 * @param nameMapping
	 *            an array describing the property name of the line read. The position of the property array corresponds
	 *            to the column in the csv file. null denote the column in the csv file is ignored.
	 * @return an object or null if EOF *
	 * @since 1.0
	 */
	public <T> T read(Class<T> clazz, String[] nameMapping) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException,
			NoSuchMethodException;

	/**
	 * Read a line of a csv file and populate a bean with the data. Before population the data is processed by cell
	 * processors.
	 * 
	 * @param nameMapping
	 *            an array describing the property name of the line read. The position of the property array corresponds
	 *            to the column in the csv file. null denote the column in the csv file.
	 * @param processors
	 *            an array of CellProcessor. The position in the array must match the position of the nameMapping. Null
	 *            entries in the array denotes columns that should not be processed.
	 * @return an object or null if EOF
	 * @since 1.0
	 */
	public <T> T read(Class<T> clazz, String[] nameMapping, CellProcessor[] processors) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException,
			InvocationTargetException, NoSuchMethodException, SuperCSVException;
}
