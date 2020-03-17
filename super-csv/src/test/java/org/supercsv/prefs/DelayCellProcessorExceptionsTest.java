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
package org.supercsv.prefs;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests the DelayCellProcessorExceptionsTest
 *
 * @author Chen Guoping
 */
public class DelayCellProcessorExceptionsTest {

	/**
	 * Tests the Default Constructor
	 */
	@Test public void testConstructor1() {
		DelayCellProcessorExceptions delayCellProcessorExceptions = new DelayCellProcessorExceptions();
		assertFalse(delayCellProcessorExceptions.isDelayExceptions());
		assertFalse(delayCellProcessorExceptions.isSkipExceptionsRow());
		assertNull(delayCellProcessorExceptions.getCallBack());
	}

	/**
	 * Tests the Constructor with skipCellProcessorExceptionsRow
	 */
	@Test public void testConstructor2() {
		DelayCellProcessorExceptions delayCellProcessorExceptions;
		delayCellProcessorExceptions = new DelayCellProcessorExceptions(true);
		assertTrue(delayCellProcessorExceptions.isDelayExceptions());
		assertTrue(delayCellProcessorExceptions.isSkipExceptionsRow());
		assertNull(null, delayCellProcessorExceptions.getCallBack().process(123));
		assertNull(null, delayCellProcessorExceptions.getCallBack().process("Csv file"));

		delayCellProcessorExceptions = new DelayCellProcessorExceptions(false);
		assertTrue(delayCellProcessorExceptions.isDelayExceptions());
		assertFalse(delayCellProcessorExceptions.isSkipExceptionsRow());
		assertNull(null, delayCellProcessorExceptions.getCallBack().process("\"\""));
		assertNull(null, delayCellProcessorExceptions.getCallBack().process(null));
	}

	/**
	 * Tests the Constructor with skipCellProcessorExceptionsRow and CallBackOnException
	 */
	@Test public void testConstructor3() {
		DelayCellProcessorExceptions delayCellProcessorExceptions;
		delayCellProcessorExceptions = new DelayCellProcessorExceptions(true, new CallBackOnException() {
			public Object process(Object rawColumns) {
				return "###";
			}
		});
		assertTrue(delayCellProcessorExceptions.isDelayExceptions());
		assertTrue(delayCellProcessorExceptions.isSkipExceptionsRow());
		assertEquals("###", delayCellProcessorExceptions.getCallBack().process(123));
		assertEquals("###", delayCellProcessorExceptions.getCallBack().process("Csv file"));

		delayCellProcessorExceptions = new DelayCellProcessorExceptions(false, new CallBackOnException() {
			public Object process(Object rawColumns) {
				return "EMPTY COLUMN";
			}
		});
		assertTrue(delayCellProcessorExceptions.isDelayExceptions());
		assertFalse(delayCellProcessorExceptions.isSkipExceptionsRow());
		assertEquals("EMPTY COLUMN", delayCellProcessorExceptions.getCallBack().process("\"\""));
		assertEquals("EMPTY COLUMN", delayCellProcessorExceptions.getCallBack().process(null));

	}

	/**
	 * Tests the Constructor with skipCellProcessorExceptionsRow and NULL CallBackOnException
	 */
	@Test(expected = NullPointerException.class) public void testConstructor4() {
		DelayCellProcessorExceptions delayCellProcessorExceptions;
		delayCellProcessorExceptions = new DelayCellProcessorExceptions(true, null);
	}
}
