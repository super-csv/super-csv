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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.supercsv.SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.mock.IdentityTransform;

/**
 * Tests the ParseEnum processor.
 * 
 * @author James Bassett
 */
public class ParseEnumTest {
	
	public enum TestEnum {
		NORMAL, lowercase, MixedCase
	}
	
	private CellProcessor processor;
	private CellProcessor processorChain;
	private CellProcessor ignoreCaseProcessor;
	private CellProcessor ignoreCaseProcessorChain;
	
	/**
	 * Sets up the processors for the test using all constructor combinations.
	 */
	@Before
	public void setUp() {
		processor = new ParseEnum(TestEnum.class);
		processorChain = new ParseEnum(TestEnum.class, new IdentityTransform());
		ignoreCaseProcessor = new ParseEnum(TestEnum.class, true);
		ignoreCaseProcessorChain = new ParseEnum(TestEnum.class, true, new IdentityTransform());
	}
	
	/**
	 * Tests that a normal enum is parsed correctly.
	 */
	@Test
	public void testNormalEnum() {
		final String input = "NORMAL";
		assertEquals(TestEnum.NORMAL, processor.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(TestEnum.NORMAL, processorChain.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(TestEnum.NORMAL, ignoreCaseProcessor.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(TestEnum.NORMAL, ignoreCaseProcessorChain.execute(input, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests that a lowercase enum is parsed correctly.
	 */
	@Test
	public void testLowerCaseEnum() {
		final String input = "lowercase";
		assertEquals(TestEnum.lowercase, processor.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(TestEnum.lowercase, processorChain.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(TestEnum.lowercase, ignoreCaseProcessor.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(TestEnum.lowercase, ignoreCaseProcessorChain.execute(input, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests that a mixed case enum is parsed correctly.
	 */
	@Test
	public void testMixedCaseEnum() {
		final String input = "MixedCase";
		assertEquals(TestEnum.MixedCase, processor.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(TestEnum.MixedCase, processorChain.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(TestEnum.MixedCase, ignoreCaseProcessor.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(TestEnum.MixedCase, ignoreCaseProcessorChain.execute(input, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests that an input with a different case fails when ignoreCase is false.
	 */
	@Test
	public void testDifferentCaseFailsWhenIgnoreCaseFalse() {
		final String input = "normal";
		for( CellProcessor p : Arrays.asList(processor, processorChain) ) {
			try {
				p.execute(input, ANONYMOUS_CSVCONTEXT);
				fail("should have thrown SuperCsvCellProcessorException");
			}
			catch(SuperCsvCellProcessorException e) {
				assertEquals("'normal' could not be parsed as a enum of type " + TestEnum.class.getName(),
					e.getMessage());
			}
		}
	}
	
	/**
	 * Tests that an input with a different case parses successfully when ignoreCase is true.
	 */
	@Test
	public void testDifferentCaseParsesWhenIgnoreCaseTrue() {
		final String input = "normal";
		for( CellProcessor p : Arrays.asList(ignoreCaseProcessor, ignoreCaseProcessorChain) ) {
			assertEquals(TestEnum.NORMAL, p.execute(input, ANONYMOUS_CSVCONTEXT));
		}
	}
	
	/**
	 * Tests execution with a null input (should throw an Exception).
	 */
	@Test(expected = SuperCsvCellProcessorException.class)
	public void testWithNull() {
		processor.execute(null, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests the first constructor with a null enum class.
	 */
	@Test(expected = NullPointerException.class)
	public void testFirstConstructorsWithNullEnumClass(){
		new ParseEnum(null);
	}
	
	/**
	 * Tests the second constructor with a null enum class.
	 */
	@Test(expected = NullPointerException.class)
	public void testSecondConstructorsWithNullEnumClass(){
		new ParseEnum(null, false);
	}
	
	/**
	 * Tests the third constructor with a null enum class.
	 */
	@Test(expected = NullPointerException.class)
	public void testThirdConstructorsWithNullEnumClass(){
		new ParseEnum(null, new IdentityTransform());
	}
	
	/**
	 * Tests the fourth constructor with a null enum class.
	 */
	@Test(expected = NullPointerException.class)
	public void testFourthConstructorsWithNullEnumClass(){
		new ParseEnum(null, false, new IdentityTransform());
	}
}
