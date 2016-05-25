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
import static org.junit.Assert.assertNull;
import static org.supercsv.SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.mock.IdentityTransform;

/**
 * Tests the Optional processor.
 * 
 * @author Kasper B. Graversen
 * @author James Bassett
 */
public class OptionalTest {
	
	private CellProcessor processor;
	private CellProcessor processorChain;
	
	/**
	 * Sets up the processors for the test using all constructor combinations.
	 */
	@Before
	public void setUp() {
		processor = new Optional();
		processorChain = new Optional(new IdentityTransform());
	}
	
	/**
	 * Tests unchained/chained execution with empty string as input.
	 */
	@Test
	public void testEmptyString() {
		assertEquals("", processor.execute("", ANONYMOUS_CSVCONTEXT));
		assertEquals("", processorChain.execute("", ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests unchained/chained execution with a space as input.
	 */
	@Test
	public void testSpace() {
		assertEquals(" ", processor.execute(" ", ANONYMOUS_CSVCONTEXT));
		assertEquals(" ", processorChain.execute(" ", ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests unchained/chained execution with normal input (not "").
	 */
	@Test
	public void testNormalInput() {
		String normal = "normal";
		assertEquals(normal, processor.execute(normal, ANONYMOUS_CSVCONTEXT));
		assertEquals(normal, processorChain.execute(normal, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests execution with a null input (should return null).
	 */
	@Test
	public void testWithNull() {
		assertNull(processor.execute(null, ANONYMOUS_CSVCONTEXT));
		assertNull(processorChain.execute(null, ANONYMOUS_CSVCONTEXT));
	}
}
