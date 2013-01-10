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
package org.supercsv.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.supercsv.SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT;

import org.junit.Test;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.mock.IdentityTransform;
import org.supercsv.util.CsvContext;

/**
 * Tests the SuperCsvConstraintViolationException class.
 * 
 * @author James Bassett
 */
public class SuperCsvConstraintViolationExceptionTest {
	
	private static final String MSG = "You violated the rules!";
	private static final Throwable THROWABLE = new RuntimeException("I'm the cause of the problem");
	private static final CellProcessor PROCESSOR = new IdentityTransform();
	
	/**
	 * Tests the first constructor.
	 */
	@Test
	public void testConstuctor1() {
		SuperCsvConstraintViolationException e = new SuperCsvConstraintViolationException(MSG, ANONYMOUS_CSVCONTEXT, PROCESSOR);
		assertEquals(MSG, e.getMessage());
		assertEquals(ANONYMOUS_CSVCONTEXT, e.getCsvContext());
		assertEquals(PROCESSOR, e.getProcessor());
		e.printStackTrace();
		
		// test with null values
		e = new SuperCsvConstraintViolationException(null, (CsvContext) null, (CellProcessor) null);
		assertNull(e.getMessage());
		assertNull(e.getCsvContext());
		assertNull(e.getProcessor());
		e.printStackTrace();
	}
	
	/**
	 * Tests the second constructor.
	 */
	@Test
	public void testConstuctor2() {
		SuperCsvConstraintViolationException e = new SuperCsvConstraintViolationException(MSG, ANONYMOUS_CSVCONTEXT, PROCESSOR, THROWABLE);
		assertEquals(MSG, e.getMessage());
		assertEquals(ANONYMOUS_CSVCONTEXT, e.getCsvContext());
		assertEquals(PROCESSOR, e.getProcessor());
		assertEquals(THROWABLE, e.getCause());
		e.printStackTrace();
		
		// test with null values
		e = new SuperCsvConstraintViolationException(null, null, null, (Throwable) null);
		assertNull(e.getMessage());
		assertNull(e.getCsvContext());
		assertNull(e.getProcessor());
		assertNull(e.getCause());
		e.printStackTrace();
	}
	
}
