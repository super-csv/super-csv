package org.supercsv.io;

import java.io.IOException;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCSVReflectionException;

/**
 * Interface for all CSV writers writing to beans.
 * 
 * @author Kasper B. Graversen
 */
public interface ICsvBeanWriter extends ICsvWriter {
	
	/**
	 * Write an object as a line to the CSV file.
	 * 
	 * @param source
	 *            the object (bean instance) containing the values to write
	 * @param nameMapping
	 *            defines the fields of the class that must be written (can't be <tt>null</tt>)
	 * @since 1.0
	 */
	void write(Object source, String... nameMapping) throws IOException, SuperCSVReflectionException;
	
	/**
	 * Write an object as a line to the CSV file, performing some processing beforehand.
	 * 
	 * @param source
	 *            the object (bean instance) containing the values to write
	 * @param nameMapping
	 *            defines the fields of the class that must be written (can't be <tt>null</tt>)
	 * @param processors
	 *            array of processors changing the data before it is written
	 * @since 1.29
	 */
	void write(Object source, String[] nameMapping, CellProcessor[] processors) throws IOException,
		SuperCSVReflectionException;
	
}
