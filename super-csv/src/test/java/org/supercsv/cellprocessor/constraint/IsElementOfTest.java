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
package org.supercsv.cellprocessor.constraint;

import static org.junit.Assert.fail;
import static org.supercsv.SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.exception.SuperCsvConstraintViolationException;
import org.supercsv.mock.IdentityTransform;

/**
 * Tests the IsElementOf constraint.
 * 
 * @author James Bassett
 */
public class IsElementOfTest {
	
	private IsElementOf processor;
	private IsElementOf chainedProcessor;
	private List<Object> input = Arrays.asList(new Object[] { "one", 2, 3.0, true, null });
	
	/**
	 * Sets up the IsElementOf processors.
	 */
	@Before
	public void setUp() {
		processor = new IsElementOf(input);
		chainedProcessor = new IsElementOf(input, new IdentityTransform());
	}
	
	/**
	 * Tests the IsElementOf constraint.
	 */
	@Test
	public void testIsElementOf() {
		
		for( Object o : input ) {
			processor.execute(o, ANONYMOUS_CSVCONTEXT);
		}
		
		try {
			processor.execute("not an element", ANONYMOUS_CSVCONTEXT);
			fail("should have thrown SuperCsvConstraintViolationException");
		}
		catch(final SuperCsvConstraintViolationException e) {}
		
	}
	
	/**
	 * Tests the IsElementOf constraint when chained to another processor.
	 */
	@Test
	public void testChainedIsElementOf() {
		
		for( Object o : input ) {
			chainedProcessor.execute(o, ANONYMOUS_CSVCONTEXT);
		}
		
		try {
			chainedProcessor.execute("not an element", ANONYMOUS_CSVCONTEXT);
			fail("should have thrown SuperCsvConstraintViolationException");
		}
		catch(final SuperCsvConstraintViolationException e) {}
		
	}
	
	/**
	 * Tests the 1 arg constructor with a null collection.
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructorWithNullCollection() {
		new IsElementOf(null);
	}
	
	/**
	 * Tests the 2 arg constructor with a null collection.
	 */
	@Test(expected = NullPointerException.class)
	public void testChainedConstructorWithNullCollection() {
		new IsElementOf(null, new IdentityTransform());
	}
	
}
