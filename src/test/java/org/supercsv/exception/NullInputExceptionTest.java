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
 * Test the NullInputException class.
 * 
 * @author James Bassett
 */
public class NullInputExceptionTest {
	
	private static final String MSG = "Ahhhh a null!";
	private static final Throwable THROWABLE = new RuntimeException("Muhahahahah I'm null");
	private static final CellProcessor PROCESSOR = new IdentityTransform();
	
	/**
	 * Tests the first constructor.
	 */
	@Test
	public void testConstructor1() {
		NullInputException e = new NullInputException(MSG);
		assertEquals(MSG, e.getMessage());
		e.printStackTrace();
		
		// test with null msg
		e = new NullInputException(null);
		assertNull(e.getMessage());
		e.printStackTrace();
	}
	
	/**
	 * Tests the second constructor.
	 */
	@Test
	public void testConstructor2() {
		NullInputException e = new NullInputException(MSG, PROCESSOR, THROWABLE);
		assertEquals(MSG, e.getMessage());
		assertEquals(PROCESSOR, e.getOffendingProcessor());
		assertEquals(THROWABLE, e.getCause());
		e.printStackTrace();
		
		// test with null msg, processor and throwable
		e = new NullInputException(null, (CellProcessor) null, (Throwable) null);
		assertNull(e.getMessage());
		assertNull(e.getOffendingProcessor());
		assertNull(e.getCause());
		e.printStackTrace();
	}
	
	/**
	 * Tests the third constructor.
	 */
	@Test
	public void testConstructor3() {
		NullInputException e = new NullInputException(MSG, ANONYMOUS_CSVCONTEXT, THROWABLE);
		assertEquals(MSG, e.getMessage());
		assertEquals(ANONYMOUS_CSVCONTEXT, e.getCsvContext());
		assertEquals(THROWABLE, e.getCause());
		e.printStackTrace();
		
		// test with null msg, context and throwable
		e = new NullInputException(null, (CsvContext) null, (Throwable) null);
		assertNull(e.getMessage());
		assertNull(e.getCsvContext());
		assertNull(e.getCause());
		e.printStackTrace();
	}
	
	/**
	 * Tests the fourth constructor.
	 */
	@Test
	public void testConstructor4() {
		NullInputException e = new NullInputException(MSG, ANONYMOUS_CSVCONTEXT, PROCESSOR);
		assertEquals(MSG, e.getMessage());
		assertEquals(ANONYMOUS_CSVCONTEXT, e.getCsvContext());
		assertEquals(PROCESSOR, e.getOffendingProcessor());
		e.printStackTrace();
		
		// test with null msg, context and processor
		e = new NullInputException(null, (CsvContext) null, (CellProcessor) null);
		assertNull(e.getMessage());
		assertNull(e.getCsvContext());
		assertNull(e.getOffendingProcessor());
		e.printStackTrace();
	}
	
	/**
	 * Tests the fifth constructor.
	 */
	@Test
	public void testConstructor5() {
		NullInputException e = new NullInputException(MSG, PROCESSOR);
		assertEquals(MSG, e.getMessage());
		assertEquals(PROCESSOR, e.getOffendingProcessor());
		e.printStackTrace();
		
		// test with null msg and processor
		e = new NullInputException(null, null);
		assertNull(e.getMessage());
		assertNull(e.getOffendingProcessor());
		e.printStackTrace();
	}
	
}
