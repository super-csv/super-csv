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
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.exception.SuperCsvConstraintViolationException;
import org.supercsv.mock.IdentityTransform;
import org.supercsv.mock.PersonBean;

/**
 * Tests the UniqueHashCode constraint.
 * 
 * @author James Bassett
 */
public class UniqueHashCodeTest {
	
	private CellProcessor processor;
	private CellProcessor processorChain;
	
	/**
	 * Sets up the processors for the test using all constructor combinations.
	 */
	@Before
	public void setUp() {
		processor = new UniqueHashCode();
		processorChain = new UniqueHashCode(new IdentityTransform());
	}
	
	/**
	 * Tests unchained/chained execution with a String inputs that have different hash codes.
	 */
	@Test
	public void testValidStringInput() {
		for( String input : Arrays.asList("a", "b", "c", "d", "A", "B", "C", "D") ) {
			assertEquals(input, processor.execute(input, ANONYMOUS_CSVCONTEXT));
			assertEquals(input, processorChain.execute(input, ANONYMOUS_CSVCONTEXT));
		}
		
	}
	
	/**
	 * Tests unchained/chained execution with a Object inputs that have different hash codes.
	 */
	@Test
	public void testValidObjectInput() {
		
		PersonBean p1 = new PersonBean();
		p1.setFirstName("Neo");
		PersonBean p2 = new PersonBean();
		p2.setFirstName("Trinity");
		PersonBean p3 = new PersonBean();
		p3.setFirstName("Morpheus");
		PersonBean p4 = new PersonBean();
		p4.setFirstName("Switch");
		
		for( PersonBean input : Arrays.asList(p1, p2, p3, p4) ) {
			assertEquals(input, processor.execute(input, ANONYMOUS_CSVCONTEXT));
			assertEquals(input, processorChain.execute(input, ANONYMOUS_CSVCONTEXT));
		}
		
	}
	
	/**
	 * Tests execution with String inputs that have the same hash code (should throw an Exception).
	 */
	@Test(expected = SuperCsvConstraintViolationException.class)
	public void testInvalidStringInput() {
		assertEquals("a", processor.execute("a", ANONYMOUS_CSVCONTEXT));
		assertEquals("b", processor.execute("b", ANONYMOUS_CSVCONTEXT));
		assertEquals("c", processor.execute("c", ANONYMOUS_CSVCONTEXT));
		processor.execute("b", ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with Object inputs that have the same hash code (should throw an Exception).
	 */
	@Test(expected = SuperCsvConstraintViolationException.class)
	public void testInvalidObjectInput() {
		
		PersonBean p1 = new PersonBean();
		p1.setFirstName("Neo");
		PersonBean p2 = new PersonBean();
		p2.setFirstName("Trinity");
		PersonBean p3 = new PersonBean();
		p3.setFirstName("Morpheus");
		PersonBean p4 = new PersonBean();
		p4.setFirstName("Neo"); // invalid! there's only one 'one'!
		
		assertEquals(p1, processor.execute(p1, ANONYMOUS_CSVCONTEXT));
		assertEquals(p2, processor.execute(p2, ANONYMOUS_CSVCONTEXT));
		assertEquals(p3, processor.execute(p3, ANONYMOUS_CSVCONTEXT));
		processor.execute(p4, ANONYMOUS_CSVCONTEXT);
		
	}
	
	/**
	 * Tests execution with a null input (should throw an Exception).
	 */
	@Test(expected = SuperCsvCellProcessorException.class)
	public void testWithNull() {
		processor.execute(null, ANONYMOUS_CSVCONTEXT);
	}
	
}
