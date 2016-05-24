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

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.i18n.SuperCsvMessages;
import org.supercsv.util.CsvContext;

/**
 * Converts a String to an Enum. Patch originally supplied by Adrian Ber.
 * 
 * @author James Bassett
 * @since 2.2.0
 */
public class ParseEnum extends CellProcessorAdaptor implements StringCellProcessor {
	
	private final Class<? extends Enum<?>> enumClass;
	
	private final boolean ignoreCase;
	
	/**
	 * Constructs a new <tt>ParseEnum</tt> processor, which converts a String to a Enum.
	 * 
	 * @param enumClass
	 *            the enum class to convert to
	 * @param <T>
	 *            the Enum type
	 * @throws NullPointerException
	 *             if enumClass is null
	 */
	public <T extends Enum<?>> ParseEnum(final Class<T> enumClass) {
		super();
		checkPreconditions(enumClass);
		this.enumClass = enumClass;
		this.ignoreCase = false;
	}
	
	/**
	 * Constructs a new <tt>ParseEnum</tt> processor, which converts a String to a Enum, ignoring the case of the input
	 * (or not) depending on the supplied flag.
	 * 
	 * @param enumClass
	 *            the enum class to convert to
	 * @param ignoreCase
	 *            whether to ignore the case of the input
	 * @param <T>
	 *            the Enum type
	 * @throws NullPointerException
	 *             if enumClass is null
	 */
	public <T extends Enum<?>> ParseEnum(final Class<T> enumClass, final boolean ignoreCase) {
		super();
		checkPreconditions(enumClass);
		this.enumClass = enumClass;
		this.ignoreCase = ignoreCase;
	}
	
	/**
	 * Constructs a new <tt>ParseEnum</tt> processor, which converts a String to a Enum then calls the next processor in
	 * the chain.
	 * 
	 * @param enumClass
	 *            the enum class to convert to
	 * @param next
	 *            the next processor in the chain
	 * @param <T>
	 *            the Enum type
	 * @throws NullPointerException
	 *             if enumClass or next is null
	 */
	public <T extends Enum<?>> ParseEnum(final Class<T> enumClass, final CellProcessor next) {
		super(next);
		checkPreconditions(enumClass);
		this.enumClass = enumClass;
		this.ignoreCase = false;
	}
	
	/**
	 * Constructs a new <tt>ParseEnum</tt> processor, which converts a String to a Enum, ignoring the case of the input
	 * (or not) depending on the supplied flag, then calls the next processor in the chain.
	 * 
	 * @param enumClass
	 *            the enum class to convert to
	 * @param ignoreCase
	 *            whether to ignore the case of the input
	 * @param next
	 *            the next processor in the chain
	 * @param <T>
	 *            the Enum type
	 * @throws NullPointerException
	 *             if enumClass or next is null
	 */
	public <T extends Enum<?>> ParseEnum(final Class<T> enumClass, final boolean ignoreCase, final CellProcessor next) {
		super(next);
		checkPreconditions(enumClass);
		this.enumClass = enumClass;
		this.ignoreCase = ignoreCase;
	}
	
	/**
	 * Checks the preconditions for creating a new ParseEnum processor.
	 * 
	 * @param enumClass
	 *            the enum class
	 * @throws NullPointerException
	 *             if enumClass is null
	 */
	private static void checkPreconditions(final Class<?> enumClass) {
		if( enumClass == null ) {
			throw new NullPointerException("enumClass should not be null");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws SuperCsvCellProcessorException
	 *             if value is null or can't be parsed as an Enum
	 */
	public Object execute(final Object value, final CsvContext context) {
		validateInputNotNull(value, context);
		
		final String inputString = value.toString();
		
		for( final Enum<?> enumConstant : enumClass.getEnumConstants() ) {
			String constantName = enumConstant.name();
			if( ignoreCase ? constantName.equalsIgnoreCase(inputString) : constantName.equals(inputString) ) {
				return enumConstant;
			}
		}
		
		throw new SuperCsvCellProcessorException(SuperCsvMessages.getMessage("org.supercsv.exception.cellprocessor.InvalidEnumValue.message", value,
			enumClass.getName()), context, this);
		
	}
}
