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
import static org.junit.Assert.assertNull;
import static org.supercsv.SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvConstraintViolationException;
import org.supercsv.mock.IdentityTransform;

/**
 * Tests the Equals constraint.
 * 
 * @author James Bassett
 */
public class EqualsTest {
	
	private static final String EXPECTED_VALUE = "expected";
	
	private CellProcessor processor;
	private CellProcessor processor2;
	private CellProcessor processorChain;
	private CellProcessor processorChain2;
	
	/**
	 * Sets up the processors for the test using all constructor combinations.
	 */
	@Before
	public void setUp() {
		processor = new Equals();
		processor2 = new Equals(EXPECTED_VALUE);
		processorChain = new Equals(new IdentityTransform());
		processorChain2 = new Equals(EXPECTED_VALUE, new IdentityTransform());
	}
	
	/**
	 * Tests unchained/chained execution with valid (equal) input. The non-constant processors have their constant value
	 * loaded after the first execution (no check is done on the first execution), so the subsequent call should check
	 * against that value.
	 */
	@Test
	public void testValidInput() {
		assertEquals(EXPECTED_VALUE, processor.execute(EXPECTED_VALUE, ANONYMOUS_CSVCONTEXT));
		assertEquals(EXPECTED_VALUE, processor2.execute(EXPECTED_VALUE, ANONYMOUS_CSVCONTEXT));
		assertEquals(EXPECTED_VALUE, processorChain.execute(EXPECTED_VALUE, ANONYMOUS_CSVCONTEXT));
		assertEquals(EXPECTED_VALUE, processorChain2.execute(EXPECTED_VALUE, ANONYMOUS_CSVCONTEXT));
		
		// repeat for the benefit of the 'non-constant' processors which will now check against the first value found
		assertEquals(EXPECTED_VALUE, processor.execute(EXPECTED_VALUE, ANONYMOUS_CSVCONTEXT));
		assertEquals(EXPECTED_VALUE, processorChain.execute(EXPECTED_VALUE, ANONYMOUS_CSVCONTEXT));
		
		// and check the 'constant' processors again, just for good measure
		assertEquals(EXPECTED_VALUE, processor2.execute(EXPECTED_VALUE, ANONYMOUS_CSVCONTEXT));
		assertEquals(EXPECTED_VALUE, processorChain2.execute(EXPECTED_VALUE, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests execution with a null expected value and null input.
	 */
	@Test
	public void testNullConstantWithNull() {
		CellProcessor equalsWithNullConstant = new Equals((Object) null);
		assertNull(equalsWithNullConstant.execute(null, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests execution with a null expected value, but a non-null input (should throw an exception).
	 */
	@Test(expected = SuperCsvConstraintViolationException.class)
	public void testNullConstantWithNonNull() {
		CellProcessor equalsWithNullConstant = new Equals((Object) null);
		equalsWithNullConstant.execute(EXPECTED_VALUE, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests a value that's not equal to the first value encountered (should throw an exception).
	 */
	@Test(expected = SuperCsvConstraintViolationException.class)
	public void testNotEqual() {
		assertEquals(EXPECTED_VALUE, processor.execute(EXPECTED_VALUE, ANONYMOUS_CSVCONTEXT));
		processor.execute("something else", ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests a value that's not equal to the constant value (should throw an exception).
	 */
	@Test(expected = SuperCsvConstraintViolationException.class)
	public void testNotEqualWithConstant() {
		processor2.execute("something else", ANONYMOUS_CSVCONTEXT);
	}
	
}
