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
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.exception.SuperCsvConstraintViolationException;
import org.supercsv.mock.IdentityTransform;

/**
 * Tests the ForbidSubStr constraint.
 * 
 * @author Kasper B. Graversen
 * @author James Bassett
 */
public class ForbidSubStrTest {
	
	private static final String FORBIDDEN = "C++";
	private static final String FORBIDDEN2 = "Microsoft";
	
	private CellProcessor processor;
	private CellProcessor processor2;
	private CellProcessor processorChain;
	private CellProcessor processorChain2;
	private CellProcessor processorChain3;
	
	/**
	 * Sets up the processors for the test using all constructor combinations.
	 */
	@Before
	public void setUp() {
		processor = new ForbidSubStr(Arrays.asList(FORBIDDEN, FORBIDDEN2));
		processor2 = new ForbidSubStr(FORBIDDEN, FORBIDDEN2);
		processorChain = new ForbidSubStr(Arrays.asList(FORBIDDEN, FORBIDDEN2), new IdentityTransform());
		processorChain2 = new ForbidSubStr(FORBIDDEN, new IdentityTransform());
		processorChain3 = new ForbidSubStr(new String[] { FORBIDDEN, FORBIDDEN2 }, new IdentityTransform());
	}
	
	/**
	 * Tests unchained/chained execution with a String that doesn't contain any forbidden Strings.
	 */
	@Test
	public void testValidInput() {
		String input = "I think Java is an awesome language";
		assertEquals(input, processor.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(input, processor2.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(input, processorChain.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(input, processorChain2.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(input, processorChain3.execute(input, ANONYMOUS_CSVCONTEXT));
		
	}
	
	/**
	 * Tests input that contains the first forbidden String (should throw an Exception).
	 */
	@Test(expected = SuperCsvConstraintViolationException.class)
	public void testForbidden() {
		String input = "I think C++ is an awesome language"; // blasphemy!
		processor.execute(input, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests input that contains the second forbidden String (should throw an Exception).
	 */
	@Test(expected = SuperCsvConstraintViolationException.class)
	public void testForbidden2() {
		String input = "I love Microsoft";
		processor.execute(input, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with a null input (should throw an Exception).
	 */
	@Test(expected = SuperCsvCellProcessorException.class)
	public void testWithNull() {
		processor.execute(null, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests construction with a null array (should throw an Exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructionWithNullArray() {
		new ForbidSubStr((String[]) null);
	}
	
	/**
	 * Tests construction with an empty array (should throw an Exception).
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructionWithEmptyArray() {
		new ForbidSubStr(new String[] {});
	}
	
	/**
	 * Tests construction with a null List (should throw an Exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructionWithNullList() {
		new ForbidSubStr((List<String>) null, new IdentityTransform());
	}
	
	/**
	 * Tests construction with an empty List (should throw an Exception).
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructionWithEmptyList() {
		new ForbidSubStr(new ArrayList<String>(), new IdentityTransform());
	}
	
	/**
	 * Tests construction with a null array (should throw an Exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructionWithNullSubstring() {
		new ForbidSubStr(new String[] { null });
	}
}
