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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.mock.IdentityTransform;

/**
 * Tests the RequireSubStr constraint.
 * 
 * @author Kasper B. Graversen
 * @author James Bassett
 */
public class RequireSubStrTest {
	
	private static final String REQUIRED1 = "in";
	private static final String REQUIRED2 = "to";
	
	private CellProcessor processor;
	private CellProcessor processorChain;
	private CellProcessor processorChain2;
	private CellProcessor processorChain3;
	
	/**
	 * Sets up the processors for the test using all constructor combinations.
	 */
	@Before
	public void setUp() {
		processor = new RequireSubStr(REQUIRED1, REQUIRED2);
		processorChain = new RequireSubStr(Arrays.asList(REQUIRED1, REQUIRED2), new IdentityTransform());
		processorChain2 = new RequireSubStr(REQUIRED1, new IdentityTransform()); // only allows 1 substring
		processorChain3 = new RequireSubStr(new String[] { REQUIRED1, REQUIRED2 }, new IdentityTransform());
	}
	
	/**
	 * Tests unchained/chained execution with a String that contains all of the required substrings.
	 */
	@Test
	public void testValidInput() {
		String input = "to infinity and beyond!";
		assertEquals(input, processor.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(input, processorChain.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(input, processorChain2.execute(input, ANONYMOUS_CSVCONTEXT)); // only has 1 substring
		assertEquals(input, processorChain3.execute(input, ANONYMOUS_CSVCONTEXT));
		
	}
	
	/**
	 * Tests input that doesn't contain any of the substrings (should throw an Exception).
	 */
	@Test(expected = SuperCSVException.class)
	public void testInvalidInput() {
		String input = "no matches here";
		processor.execute(input, ANONYMOUS_CSVCONTEXT);
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
		new RequireSubStr((String[]) null);
	}
	
	/**
	 * Tests construction with an empty array (should throw an Exception).
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructionWithEmptyArray() {
		new RequireSubStr(new String[] {});
	}
	
	/**
	 * Tests construction with a null List (should throw an Exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructionWithNullList() {
		new RequireSubStr((List<String>) null, new IdentityTransform());
	}
	
	/**
	 * Tests construction with an empty List (should throw an Exception).
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructionWithEmptyList() {
		new RequireSubStr(new ArrayList<String>(), new IdentityTransform());
	}
	
	/**
	 * Tests construction with a null array (should throw an Exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructionWithNullSubstring() {
		new RequireSubStr(new String[] { null });
	}
}
