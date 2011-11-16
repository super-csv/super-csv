package org.supercsv.io;

import java.io.IOException;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCSVReflectionException;

/**
 * Supertype for all writers writing using beans
 * 
 * @author Kasper B. Graversen
 */
public interface ICsvBeanWriter extends ICsvWriter {
/**
 * Write an object
 * 
 * @param source
 *            at object (bean instance) whose values to extract
 * @param nameMapping
 *            defines the fields of the class that must be written. null values are not allowed *
 * @since 1.0
 */
public void write(Object source, String... nameMapping) throws IOException, SuperCSVReflectionException;

/**
 * Write an object
 * 
 * @param source
 *            at object (bean instance) whose values to extract
 * @param nameMapping
 *            defines the fields of the class that must be written. null values are not allowed
 * @param processor
 *            array of processors changing the data before it is written
 * @since 1.29
 */
public void write(Object source, String[] nameMapping, CellProcessor[] processor) throws IOException,
	SuperCSVReflectionException;

}
