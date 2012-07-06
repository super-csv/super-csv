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
 * Exception thrown when a mandatory input to a <tt>CellProcessor</tt>, reader or writer is <tt>null</tt>.
 * 
 * @since 1.50
 * @author Kasper B. Graversen
 * @author Dominique de Vito
 */
public class NullInputException extends SuperCSVException {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs a new <tt>NullInputException</tt>.
	 * 
	 * @param msg
	 *            the exception message
	 */
	public NullInputException(final String msg) {
		super(msg);
	}
	
	/**
	 * Constructs a new <tt>NullInputException</tt>.
	 * 
	 * @param msg
	 *            the exception message
	 * @param offendingProcessor
	 *            the processor requiring the input to be supplied
	 * @param t
	 *            the nested exception
	 */
	public NullInputException(final String msg, final CellProcessor offendingProcessor, final Throwable t) {
		super(msg, null, offendingProcessor, t);
	}
	
	/**
	 * Constructs a new <tt>NullInputException</tt>.
	 * 
	 * @param msg
	 *            the exception message
	 * @param context
	 *            the current CSV context
	 * @param t
	 *            the nested exception
	 */
	public NullInputException(final String msg, final CsvContext context, final Throwable t) {
		super(msg, context, null, t);
	}
	
	/**
	 * Constructs a new <tt>NullInputException</tt>.
	 * 
	 * @param msg
	 *            the exception message
	 * @param context
	 *            the current CSV context
	 * @param offendingProcessor
	 *            the processor requiring the input to be supplied
	 */
	public NullInputException(final String msg, final CsvContext context, final CellProcessor offendingProcessor) {
		super(msg, context, offendingProcessor);
	}
	
	/**
	 * Constructs a new <tt>NullInputException</tt>.
	 * 
	 * @param msg
	 *            the exception message
	 * @param offendingProcessor
	 *            the processor requiring the input to be supplied
	 */
	public NullInputException(final String msg, final CellProcessor offendingProcessor) {
		super(msg, offendingProcessor);
	}
	
}
