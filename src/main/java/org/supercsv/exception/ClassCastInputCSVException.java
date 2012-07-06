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
 * This exception is thrown by <tt>CellProcessor</tt>s when receiving a value with a type different than the one
 * expected.
 * 
 * @since 1.50
 * @author Dominique De Vito
 */
public class ClassCastInputCSVException extends SuperCSVException {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs a new <tt>ClassCastInputCSVException</tt> with the supplied message, context and nested exception.
	 * 
	 * @param msg
	 *            the error message
	 * @param context
	 *            the CSV context
	 * @param t
	 *            the nested exception
	 */
	public ClassCastInputCSVException(String msg, CsvContext context, Throwable t) {
		super(msg, context, t);
	}
	
	/**
	 * Constructs a new <tt>ClassCastInputCSVException</tt> with the supplied message and context.
	 * 
	 * @param msg
	 *            the error message
	 * @param context
	 *            the CSV context
	 */
	public ClassCastInputCSVException(String msg, CsvContext context) {
		super(msg, context);
	}
	
	/**
	 * Constructs a new <tt>ClassCastInputCSVException</tt> with the supplied message, context and the offending
	 * processor.
	 * 
	 * @param msg
	 *            the error message
	 * @param context
	 *            the CSV context
	 * @param processor
	 *            the offending processor
	 */
	public ClassCastInputCSVException(String msg, CsvContext context, CellProcessor processor) {
		super(msg, context, processor);
	}
	
	/**
	 * Constructs a new <tt>ClassCastInputCSVException</tt> with the supplied message.
	 * 
	 * @param msg
	 *            the error message
	 */
	public ClassCastInputCSVException(String msg) {
		super(msg);
	}
	
	/**
	 * Constructs a new <tt>ClassCastInputCSVException</tt> with the received value, expected class, context and the
	 * offending processor.
	 * 
	 * @param receivedValue
	 *            the received value
	 * @param expectedClass
	 *            the expected class
	 * @param context
	 *            the CSV context
	 * @param processor
	 *            the offending processor
	 * @throws NullPointerException
	 *             if expectedClass is null
	 */
	public ClassCastInputCSVException(Object receivedValue, Class<?> expectedClass, CsvContext context,
		CellProcessor processor) {
		super(getDefaultMessage(receivedValue, expectedClass), context, processor);
	}
	
	/**
	 * Assembles a default error message.
	 * 
	 * @param receivedValue
	 *            the received value
	 * @param expectedClass
	 *            the expected class
	 * @return the assembled error message
	 * @throws NullPointerException
	 *             if expectedClass is null
	 */
	private static String getDefaultMessage(Object receivedValue, Class<?> expectedClass) {
		if( expectedClass == null ) {
			throw new NullPointerException("expectedClass should not be null");
		}
		String expectedClassName = expectedClass.getName();
		String actualClassName = (receivedValue != null) ? receivedValue.getClass().getName() : "null";
		return String.format("the input value should be of type %s but is %s", expectedClassName, actualClassName);
	}
}
