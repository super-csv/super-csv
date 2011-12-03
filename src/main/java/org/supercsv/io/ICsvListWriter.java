package org.supercsv.io;

import java.io.IOException;
import java.util.List;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCSVException;

/**
 * Interface for writers that write to a List.
 * 
 * @author Kasper B. Graversen
 */
public interface ICsvListWriter extends ICsvWriter {
	
	/**
	 * Writes a List of Objects. If the List is empty, an exception will be thrown to signal a possible error in the
	 * user code.
	 * 
	 * @since 1.0
	 */
	void write(List<? extends Object> content) throws IOException;
	
	/**
	 * Writes a List of Objects, performing any necessary processing beforehand. If the array is empty, an exception
	 * will be thrown to signal a possible error in the user code.
	 * 
	 * @since 1.0
	 */
	void write(List<? extends Object> content, CellProcessor[] processors) throws IOException;
	
	/**
	 * Writes a array of Objects. Each object will be converted to a string by calling the <code>toString()</code> on
	 * it. If the array is empty, an exception will be thrown to signal a possible error in the user code.
	 * 
	 * @since 1.0
	 */
	void write(Object... content) throws IOException, SuperCSVException;
	
	/**
	 * Writes an array of strings. If the array is empty, an exception will be thrown to signal a possible error in the
	 * user code.
	 * 
	 * @since 1.0
	 */
	void write(String... content) throws IOException, SuperCSVException;
	
}
