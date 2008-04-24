package org.supercsv.io;

import java.io.IOException;
import java.util.List;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCSVException;

/**
 * Supertype for all writers using lists
 * 
 * @author Kasper B. Graversen
 */
public interface ICsvListWriter extends ICsvWriter {
/**
 * Plain writing a list of strings. If the array is empty, an exception will be thrown to signal a possible error in the
 * user code
 * 
 * @since 1.0
 */
void write(List<? extends Object> content) throws IOException;

/**
 * writing a list of strings which can be processed. If the array is empty, an exception will be thrown to signal a
 * possible error in the user code
 * 
 * @since 1.0
 */
void write(List<? extends Object> content, CellProcessor[] processors) throws IOException;

/**
 * Plain writing a list of Objects. Each object will be converted to a string by calling the <code>toString()</code>
 * on it. If the array is empty, an exception will be thrown to signal a possible error in the user code
 * 
 * @since 1.0
 */
void write(Object... content) throws IOException, SuperCSVException;

/**
 * Plain writing a list of strings If the array is empty, an exception will be thrown to signal a possible error in the
 * user code
 * 
 * @since 1.0
 */
void write(String... content) throws IOException, SuperCSVException;

}
