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

import static org.junit.Assert.assertEquals;
import static org.supercsv.SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvConstraintViolationException;
import org.supercsv.mock.IdentityTransform;

/**
 * Tests the Unique constraint.
 * 
 * @author Kasper B. Graversen
 * @author James Bassett
 */
public class UniqueTest {
	
	private CellProcessor processor;
	private CellProcessor processorChain;
	
	/**
	 * Sets up the processors for the test using all constructor combinations.
	 */
	@Before
	public void setUp() {
		processor = new Unique();
		processorChain = new Unique(new IdentityTransform());
	}
	
	/**
	 * Tests unchained/chained execution with valid (unique) String input.
	 */
	@Test
	public void testValidStringInput() {
		for( String input : Arrays.asList("1", "2", "3", "4", "5") ) {
			assertEquals(input, processor.execute(input, ANONYMOUS_CSVCONTEXT));
			assertEquals(input, processorChain.execute(input, ANONYMOUS_CSVCONTEXT));
		}
		
	}
	
	/**
	 * Tests unchained/chained execution with valid (unique) Integer input.
	 */
	@Test
	public void testValidIntegerInput() {
		for( Integer input : Arrays.asList(1, 2, 3, 4, 5) ) {
			assertEquals(input, processor.execute(input, ANONYMOUS_CSVCONTEXT));
			assertEquals(input, processorChain.execute(input, ANONYMOUS_CSVCONTEXT));
		}
		
	}
	
	/**
	 * Tests invalid (non-unique) String input.
	 */
	@Test(expected = SuperCsvConstraintViolationException.class)
	public void testInvalidStringInput() {
		assertEquals("duplicate", processor.execute("duplicate", ANONYMOUS_CSVCONTEXT));
		processor.execute("duplicate", ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests invalid (non-unique) Integer input.
	 */
	@Test(expected = SuperCsvConstraintViolationException.class)
	public void testInvalidIntegerInput() {
		assertEquals(Integer.valueOf(99), processor.execute(Integer.valueOf(99), ANONYMOUS_CSVCONTEXT));
		processor.execute(Integer.valueOf(99), ANONYMOUS_CSVCONTEXT);
		
	}
}
