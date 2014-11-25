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
package org.supercsv.cellprocessor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.supercsv.cellprocessor.ift.BoolCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

/**
 * Converts a String to a Boolean.
 * <p>
 * The default values for true are: <tt>"true", "1", "y", "t"</tt>
 * <p>
 * The default values for false are: <tt>"false", "0", "n", "f"</tt>
 * <p>
 * By default (unless the <tt>ignoreCase</tt> parameter is supplied on the constructor) this processor will ignore the
 * case of the value, i.e. "true", "TRUE", and "True" will all be converted to <tt>true</tt> (likewise for
 * <tt>false</tt>).
 * <p>
 * Prior to version 2.2.1, this processor always ignored case, so it was necessary to ensure that all supplied
 * true/false values were lowercase, as the input was converted to lowercase before comparison against the true/false
 * values (to handle all variations of case in the input). This is no longer required (just use the <tt>ignoreCase</tt>
 * parameter).
 * 
 * @author Kasper B. Graversen
 * @author Dominique De Vito
 * @author James Bassett
 * @since 1.0
 */
public class ParseBool extends CellProcessorAdaptor implements StringCellProcessor {
	
	private static final String[] DEFAULT_TRUE_VALUES = new String[] { "1", "true", "t", "y" };
	private static final String[] DEFAULT_FALSE_VALUES = new String[] { "0", "false", "f", "n" };
	
	private final Set<String> trueValues = new HashSet<String>();
	private final Set<String> falseValues = new HashSet<String>();
	
	private final boolean ignoreCase;
	
	/**
	 * Constructs a new <tt>ParseBool</tt> processor, which converts a String to a Boolean using the default values
	 * (ignoring case).
	 */
	public ParseBool() {
		this(DEFAULT_TRUE_VALUES, DEFAULT_FALSE_VALUES);
	}
	
	/**
	 * Constructs a new <tt>ParseBool</tt> processor, which converts a String to a Boolean using the default values
	 * (ignoring case if desired).
	 * 
	 * @since 2.2.1
	 * @param ignoreCase
	 *            whether to ignore the case when comparing against the true/false values
	 */
	public ParseBool(final boolean ignoreCase) {
		this(DEFAULT_TRUE_VALUES, DEFAULT_FALSE_VALUES, ignoreCase);
	}
	
	/**
	 * Constructs a new <tt>ParseBool</tt> processor, which converts a String to a Boolean using the default values
	 * (ignoring case), then calls the next processor in the chain.
	 * 
	 * @param next
	 *            the next processor in the chain
	 * @throws NullPointerException
	 *             if next is null
	 */
	public ParseBool(final BoolCellProcessor next) {
		this(DEFAULT_TRUE_VALUES, DEFAULT_FALSE_VALUES, next);
	}
	
	/**
	 * Constructs a new <tt>ParseBool</tt> processor, which converts a String to a Boolean using the default values
	 * (ignoring case if desired), then calls the next processor in the chain.
	 * 
	 * @since 2.2.1
	 * @param ignoreCase
	 *            whether to ignore the case when comparing against the true/false values
	 * @param next
	 *            the next processor in the chain
	 * @throws NullPointerException
	 *             if next is null
	 */
	public ParseBool(final boolean ignoreCase, final BoolCellProcessor next) {
		this(DEFAULT_TRUE_VALUES, DEFAULT_FALSE_VALUES, ignoreCase, next);
	}
	
	/**
	 * Constructs a new <tt>ParseBool</tt> processor, which converts a String to a Boolean using the supplied true/false
	 * values (ignoring case).
	 * 
	 * @param trueValue
	 *            the String which represents true
	 * @param falseValue
	 *            the String which represents false
	 * @throws NullPointerException
	 *             if trueValue or falseValue is null
	 */
	public ParseBool(final String trueValue, final String falseValue) {
		this(trueValue, falseValue, true);
	}
	
	/**
	 * Constructs a new <tt>ParseBool</tt> processor, which converts a String to a Boolean using the supplied true/false
	 * values (ignoring case if desired).
	 * 
	 * @since 2.2.1
	 * @param trueValue
	 *            the String which represents true
	 * @param falseValue
	 *            the String which represents false
	 * @param ignoreCase
	 *            whether to ignore the case when comparing against the true/false values
	 * @throws NullPointerException
	 *             if trueValue or falseValue is null
	 */
	public ParseBool(final String trueValue, final String falseValue, final boolean ignoreCase) {
		super();
		checkPreconditions(trueValue, falseValue);
		trueValues.add(trueValue);
		falseValues.add(falseValue);
		this.ignoreCase = ignoreCase;
	}
	
	/**
	 * Constructs a new <tt>ParseBool</tt> processor, which converts a String to a Boolean using the supplied true/false
	 * values (ignoring case).
	 * 
	 * @param trueValues
	 *            the array of Strings which represent true
	 * @param falseValues
	 *            the array of Strings which represent false
	 * @throws IllegalArgumentException
	 *             if trueValues or falseValues is empty
	 * @throws NullPointerException
	 *             if trueValues or falseValues is null
	 */
	public ParseBool(final String[] trueValues, final String[] falseValues) {
		this(trueValues, falseValues, true);
	}
	
	/**
	 * Constructs a new <tt>ParseBool</tt> processor, which converts a String to a Boolean using the supplied true/false
	 * values (ignoring case if desired).
	 * 
	 * @since 2.2.1
	 * @param trueValues
	 *            the array of Strings which represent true
	 * @param falseValues
	 *            the array of Strings which represent false
	 * @param ignoreCase
	 *            whether to ignore the case when comparing against the true/false values
	 * @throws IllegalArgumentException
	 *             if trueValues or falseValues is empty
	 * @throws NullPointerException
	 *             if trueValues or falseValues is null
	 */
	public ParseBool(final String[] trueValues, final String[] falseValues, final boolean ignoreCase) {
		super();
		checkPreconditions(trueValues, falseValues);
		Collections.addAll(this.trueValues, trueValues);
		Collections.addAll(this.falseValues, falseValues);
		this.ignoreCase = ignoreCase;
	}
	
	/**
	 * Constructs a new <tt>ParseBool</tt> processor, which converts a String to a Boolean using the supplied true/false
	 * values (ignoring case), then calls the next processor in the chain.
	 * 
	 * @param trueValue
	 *            the String which represents true
	 * @param falseValue
	 *            the String which represents false
	 * @param next
	 *            the next processor in the chain
	 * @throws NullPointerException
	 *             if trueValue, falseValue or next is null
	 */
	public ParseBool(final String trueValue, final String falseValue, final BoolCellProcessor next) {
		this(trueValue, falseValue, true, next);
	}
	
	/**
	 * Constructs a new <tt>ParseBool</tt> processor, which converts a String to a Boolean using the supplied true/false
	 * values (ignoring case if desired), then calls the next processor in the chain.
	 * 
	 * @since 2.2.1
	 * @param trueValue
	 *            the String which represents true
	 * @param falseValue
	 *            the String which represents false
	 * @param ignoreCase
	 *            whether to ignore the case when comparing against the true/false values
	 * @param next
	 *            the next processor in the chain
	 * @throws NullPointerException
	 *             if trueValue, falseValue or next is null
	 */
	public ParseBool(final String trueValue, final String falseValue, final boolean ignoreCase,
		final BoolCellProcessor next) {
		super(next);
		checkPreconditions(trueValue, falseValue);
		trueValues.add(trueValue);
		falseValues.add(falseValue);
		this.ignoreCase = ignoreCase;
	}
	
	/**
	 * Constructs a new <tt>ParseBool</tt> processor, which converts a String to a Boolean using the supplied true/false
	 * values (ignoring case), then calls the next processor in the chain.
	 * 
	 * @param trueValues
	 *            the array of Strings which represent true
	 * @param falseValues
	 *            the array of Strings which represent false
	 * @param next
	 *            the next processor in the chain
	 * @throws IllegalArgumentException
	 *             if trueValues or falseValues is empty
	 * @throws NullPointerException
	 *             if trueValues, falseValues, or next is null
	 */
	public ParseBool(final String[] trueValues, final String[] falseValues, final BoolCellProcessor next) {
		this(trueValues, falseValues, true, next);
	}
	
	/**
	 * Constructs a new <tt>ParseBool</tt> processor, which converts a String to a Boolean using the supplied true/false
	 * values (ignoring case if desired), then calls the next processor in the chain.
	 * 
	 * @since 2.2.1
	 * @param trueValues
	 *            the array of Strings which represent true
	 * @param falseValues
	 *            the array of Strings which represent false
	 * @param ignoreCase
	 *            whether to ignore the case when comparing against the true/false values
	 * @param next
	 *            the next processor in the chain
	 * @throws IllegalArgumentException
	 *             if trueValues or falseValues is empty
	 * @throws NullPointerException
	 *             if trueValues, falseValues, or next is null
	 */
	public ParseBool(final String[] trueValues, final String[] falseValues, final boolean ignoreCase,
		final BoolCellProcessor next) {
		super(next);
		checkPreconditions(trueValues, falseValues);
		Collections.addAll(this.trueValues, trueValues);
		Collections.addAll(this.falseValues, falseValues);
		this.ignoreCase = ignoreCase;
	}
	
	/**
	 * Checks the preconditions for constructing a new ParseBool processor.
	 * 
	 * @param trueValue
	 *            the String which represents true
	 * @param falseValue
	 *            the String which represents false
	 * @throws NullPointerException
	 *             if trueValue or falseValue is null
	 */
	private static void checkPreconditions(final String trueValue, final String falseValue) {
		if( trueValue == null ) {
			throw new NullPointerException("trueValue should not be null");
		}
		if( falseValue == null ) {
			throw new NullPointerException("falseValue should not be null");
		}
	}
	
	/**
	 * Checks the preconditions for constructing a new ParseBool processor.
	 * 
	 * @param trueValues
	 *            the array of Strings which represent true
	 * @param falseValues
	 *            the array of Strings which represent false
	 * @throws IllegalArgumentException
	 *             if trueValues or falseValues is empty
	 * @throws NullPointerException
	 *             if trueValues or falseValues is null
	 */
	private static void checkPreconditions(final String[] trueValues, final String[] falseValues) {
		
		if( trueValues == null ) {
			throw new NullPointerException("trueValues should not be null");
		} else if( trueValues.length == 0 ) {
			throw new IllegalArgumentException("trueValues should not be empty");
		}
		
		if( falseValues == null ) {
			throw new NullPointerException("falseValues should not be null");
		} else if( falseValues.length == 0 ) {
			throw new IllegalArgumentException("falseValues should not be empty");
		}
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws SuperCsvCellProcessorException
	 *             if value is null, not a String, or can't be parsed to a Boolean
	 */
	public Object execute(final Object value, final CsvContext context) {
		validateInputNotNull(value, context);
		
		if( !(value instanceof String) ) {
			throw new SuperCsvCellProcessorException(String.class, value, context, this);
		}
		
		final String stringValue = (String) value;
		final Boolean result;
		if( contains(trueValues, stringValue, ignoreCase) ) {
			result = Boolean.TRUE;
		} else if( contains(falseValues, stringValue, ignoreCase) ) {
			result = Boolean.FALSE;
		} else {
			throw new SuperCsvCellProcessorException(String.format("'%s' could not be parsed as a Boolean", value),
				context, this);
		}
		
		return next.execute(result, context);
	}
	
	/**
	 * Returns true if the set contains the value, otherwise false.
	 * 
	 * @param set
	 *            the set
	 * @param value
	 *            the value to find
	 * @param ignoreCase
	 *            whether to ignore case
	 * @return true if the set contains the value, otherwise false
	 */
	private static boolean contains(Set<String> set, String value, boolean ignoreCase) {
		if( ignoreCase ) {
			for( String element : set ) {
				if( element.equalsIgnoreCase(value) ) {
					return true;
				}
			}
			return false;
			
		} else {
			return set.contains(value);
		}
	}
	
}
