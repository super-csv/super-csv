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
import static org.junit.Assert.fail;
import static org.supercsv.SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT;

import org.junit.Test;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.mock.IdentityTransform;
import org.supercsv.util.CsvContext;

/**
 * Tests the SuperCsvCellProcessorException class.
 * 
 * @author James Bassett
 */
public class SuperCsvCellProcessorExceptionTest {
	
	private static final String MSG = "Cell processing failed!";
	private static final Throwable THROWABLE = new RuntimeException("I'm the cause of the problem");
	private static final CellProcessor PROCESSOR = new IdentityTransform();
	
	/**
	 * Tests the first constructor.
	 */
	@Test
	public void testConstuctor1() {
		SuperCsvCellProcessorException e = new SuperCsvCellProcessorException(MSG, ANONYMOUS_CSVCONTEXT, PROCESSOR);
		assertEquals(MSG, e.getMessage());
		assertEquals(ANONYMOUS_CSVCONTEXT, e.getCsvContext());
		assertEquals(PROCESSOR, e.getProcessor());
		
		// test with null values
		e = new SuperCsvCellProcessorException(null, (CsvContext) null, (CellProcessor) null);
		assertNull(e.getMessage());
		assertNull(e.getCsvContext());
		assertNull(e.getProcessor());
	}
	
	/**
	 * Tests the second constructor.
	 */
	@Test
	public void testConstuctor2() {
		SuperCsvCellProcessorException e = new SuperCsvCellProcessorException(MSG, ANONYMOUS_CSVCONTEXT, PROCESSOR, THROWABLE);
		assertEquals(MSG, e.getMessage());
		assertEquals(ANONYMOUS_CSVCONTEXT, e.getCsvContext());
		assertEquals(PROCESSOR, e.getProcessor());
		assertEquals(THROWABLE, e.getCause());
		
		// test with null values
		e = new SuperCsvCellProcessorException(null, null, null, (Throwable) null);
		assertNull(e.getMessage());
		assertNull(e.getCsvContext());
		assertNull(e.getProcessor());
		assertNull(e.getCause());
	}
	
	/**
	 * Tests the second constructor.
	 */
	@Test
	public void testConstuctor3() {
		
		// actual value not null
		SuperCsvCellProcessorException e = new SuperCsvCellProcessorException(String.class, 123, ANONYMOUS_CSVCONTEXT, PROCESSOR);
		assertEquals("the input value should be of type java.lang.String but is java.lang.Integer", e.getMessage());
		assertEquals(ANONYMOUS_CSVCONTEXT, e.getCsvContext());
		assertEquals(PROCESSOR, e.getProcessor());
		
		// null actual value
		e = new SuperCsvCellProcessorException(String.class, null, ANONYMOUS_CSVCONTEXT, PROCESSOR);
		assertEquals("the input value should be of type java.lang.String but is null", e.getMessage());
		assertEquals(ANONYMOUS_CSVCONTEXT, e.getCsvContext());
		assertEquals(PROCESSOR, e.getProcessor());
		
		// test with null values
		try {
			new SuperCsvCellProcessorException(null, null, null, (CellProcessor) null);
			fail("should have thrown NullPointerException");
		} catch (NullPointerException npe){
			assertEquals("expectedType should not be null", npe.getMessage());
		}
	}
	
}
