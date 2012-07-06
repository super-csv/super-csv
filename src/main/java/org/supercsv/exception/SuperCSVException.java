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
package org.supercsv.exception;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CsvContext;

/**
 * Generic SuperCSV Exception class. It contains any additional relevant information including the CSV context (line
 * number, column number and raw line) and the CellProcessor executing when the exception occurred.
 * 
 * @author Kasper B. Graversen
 */
public class SuperCSVException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	private CsvContext csvContext;
	
	private CellProcessor offendingProcessor;
	
	/**
	 * Constructs a new <tt>SuperCSVException</tt>.
	 * 
	 * @param msg
	 *            the exception message
	 */
	public SuperCSVException(final String msg) {
		super(msg);
	}
	
	/**
	 * Constructs a new <tt>SuperCSVException</tt>.
	 * 
	 * @param msg
	 *            the exception message
	 * @param context
	 *            the CSV context
	 */
	public SuperCSVException(final String msg, final CsvContext context) {
		super(msg);
		this.csvContext = context;
	}
	
	/**
	 * Constructs a new <tt>SuperCSVException</tt>.
	 * 
	 * @param msg
	 *            the exception message
	 * @param context
	 *            the CSV context
	 * @param t
	 *            the nested exception
	 */
	public SuperCSVException(final String msg, final CsvContext context, final Throwable t) {
		super(msg, t);
		this.csvContext = context;
	}
	
	/**
	 * Constructs a new <tt>SuperCSVException</tt>.
	 * 
	 * @param msg
	 *            the exception message
	 * @param processor
	 *            the offending processor
	 */
	public SuperCSVException(final String msg, final CellProcessor processor) {
		super(msg);
		this.offendingProcessor = processor;
	}
	
	/**
	 * Constructs a new <tt>SuperCSVException</tt>.
	 * 
	 * @param msg
	 *            the exception message
	 * @param context
	 *            the CSV context
	 * @param processor
	 *            the offending processor
	 */
	public SuperCSVException(final String msg, final CsvContext context, final CellProcessor processor) {
		super(msg);
		this.csvContext = context;
		this.offendingProcessor = processor;
	}
	
	/**
	 * Constructs a new <tt>SuperCSVException</tt>.
	 * 
	 * @param msg
	 *            the exception message
	 * @param context
	 *            the CSV context
	 * @param processor
	 *            the offending processor
	 * @param t
	 *            the nested exception
	 */
	public SuperCSVException(final String msg, final CsvContext context, final CellProcessor processor,
		final Throwable t) {
		super(msg, t);
		this.csvContext = context;
		this.offendingProcessor = processor;
	}
	
	/**
	 * The context may be null when exceptions are thrown before or after processing, such as in cell
	 * offendingProcessor's <code>init()</code> methods.
	 * 
	 * @return the current CSV context, or <tt>null</tt>
	 */
	public CsvContext getCsvContext() {
		return csvContext;
	}
	
	/**
	 * Think twice before invoking this...
	 * 
	 * @param csvContext
	 *            the new context
	 */
	public void setCsvContext(final CsvContext csvContext) {
		this.csvContext = csvContext;
	}
	
	/**
	 * Returns the processor executing when the exception occurred.
	 * 
	 * @return the processor executing when the exception occurred
	 */
	public CellProcessor getOffendingProcessor() {
		return offendingProcessor;
	}
	
	/**
	 * Returns the String representation of this exception.
	 */
	@Override
	public String toString() {
		return String.format("%s: %s%noffending processor=%s%ncontext=%s", getClass().getName(), getMessage(),
			offendingProcessor, csvContext);
	}
}
