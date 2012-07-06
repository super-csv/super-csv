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

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.mock.IdentityTransform;

/**
 * Tests the IsIncludedIn constraint.
 * 
 * @author Dominique De Vito
 * @author James Bassett
 */
public class IsIncludedInTest {
	
	private static final Set<Object> VALUE_SET = new HashSet<Object>();
	
	private static final Integer ONE = 1;
	private static final String TWO = "Two";
	private static final Double THREE = 3.0;
	
	static {
		VALUE_SET.add(ONE);
		VALUE_SET.add(TWO);
		VALUE_SET.add(THREE);
	}
	
	private CellProcessor processor;
	private CellProcessor processor2;
	private CellProcessor processorChain;
	private CellProcessor processorChain2;
	
	/**
	 * Sets up the processors for the test using all constructor combinations.
	 */
	@Before
	public void setUp() {
		processor = new IsIncludedIn(VALUE_SET);
		processor2 = new IsIncludedIn(VALUE_SET.toArray());
		processorChain = new IsIncludedIn(VALUE_SET, new IdentityTransform());
		processorChain2 = new IsIncludedIn(VALUE_SET.toArray(), new IdentityTransform());
	}
	
	/**
	 * Tests unchained/chained execution with values from the Set.
	 */
	@Test
	public void testValidInput() {
		assertEquals(ONE, processor.execute(ONE, ANONYMOUS_CSVCONTEXT));
		assertEquals(ONE, processor2.execute(ONE, ANONYMOUS_CSVCONTEXT));
		assertEquals(ONE, processorChain.execute(ONE, ANONYMOUS_CSVCONTEXT));
		assertEquals(ONE, processorChain2.execute(ONE, ANONYMOUS_CSVCONTEXT));
		
		assertEquals(TWO, processor.execute(TWO, ANONYMOUS_CSVCONTEXT));
		assertEquals(TWO, processor2.execute(TWO, ANONYMOUS_CSVCONTEXT));
		assertEquals(TWO, processorChain.execute(TWO, ANONYMOUS_CSVCONTEXT));
		assertEquals(TWO, processorChain2.execute(TWO, ANONYMOUS_CSVCONTEXT));
		
		assertEquals(THREE, processor.execute(THREE, ANONYMOUS_CSVCONTEXT));
		assertEquals(THREE, processor2.execute(THREE, ANONYMOUS_CSVCONTEXT));
		assertEquals(THREE, processorChain.execute(THREE, ANONYMOUS_CSVCONTEXT));
		assertEquals(THREE, processorChain2.execute(THREE, ANONYMOUS_CSVCONTEXT));
		
	}
	
	/**
	 * Tests execution with a value that's not in the Set (should throw an Exception).
	 */
	@Test(expected = SuperCSVException.class)
	public void testWithInvalidInput() {
		processor.execute(4, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with a null input (should throw an Exception).
	 */
	@Test(expected = NullInputException.class)
	public void testWithNull() {
		processor.execute(null, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests construction with a null array (should throw an Exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructionWithNullArray() {
		new IsIncludedIn((Object[]) null);
	}
	
	/**
	 * Tests construction with an empty array (should throw an Exception).
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructionWithEmptyArray() {
		new IsIncludedIn(new Object[] {});
	}
	
	/**
	 * Tests construction with a null Set (should throw an Exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructionWithNullSet() {
		new IsIncludedIn((Set<Object>) null);
	}
	
	/**
	 * Tests construction with an empty Set (should throw an Exception).
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructionWithEmptySet() {
		new IsIncludedIn(new HashSet<Object>());
	}
	
}
