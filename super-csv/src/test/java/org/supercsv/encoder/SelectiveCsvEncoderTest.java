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
package org.supercsv.encoder;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.CsvContext;

/**
 * Tests the SelectiveCsvEncoder class.
 * 
 * @author James Bassett
 */
public class SelectiveCsvEncoderTest {
	
	private static final String UNESCAPED = "\"Escape!\", he yelled.";
	
	private static final String ESCAPED = "\"\"\"Escape!\"\", he yelled.\"";
	
	/**
	 * Tests the encode method using the int array constructor.
	 */
	@Test
	public void testEncodeWithIntArray() {
		final SelectiveCsvEncoder encoder = new SelectiveCsvEncoder(2, 3);
		testEncode(encoder);
	}
	
	/**
	 * Tests the encode method using the boolean array constructor.
	 */
	@Test
	public void testEncodeWithBoolArray() {
		final SelectiveCsvEncoder encoder = new SelectiveCsvEncoder(new boolean[]{false, true, true, false});
		testEncode(encoder);
	}
	
	/**
	 * Tests encoding using the supplied encoder.
	 * 
	 * @param encoder
	 *            the encoder
	 */
	private void testEncode(final CsvEncoder encoder) {
		final CsvContext context = new CsvContext(1, 1, 1);
		assertEquals(UNESCAPED, encoder.encode(UNESCAPED, context, CsvPreference.STANDARD_PREFERENCE));
		context.setColumnNumber(2);
		assertEquals(ESCAPED, encoder.encode(UNESCAPED, context, CsvPreference.STANDARD_PREFERENCE));
		context.setColumnNumber(3);
		assertEquals(ESCAPED, encoder.encode(UNESCAPED, context, CsvPreference.STANDARD_PREFERENCE));
		context.setColumnNumber(4);
		assertEquals(UNESCAPED, encoder.encode(UNESCAPED, context, CsvPreference.STANDARD_PREFERENCE));
	}
	
	/**
	 * Tests the encode method when no column numbers are supplied.
	 */
	@Test
	public void testEncodeWithNoColumnNumbers() {
		final SelectiveCsvEncoder encoder = new SelectiveCsvEncoder();
		final CsvContext context = new CsvContext(1, 1, 1);
		assertEquals(UNESCAPED, encoder.encode(UNESCAPED, context, CsvPreference.STANDARD_PREFERENCE));
	}
	
	/**
	 * Tests the int array constructor with a null array.
	 */
	@Test(expected = NullPointerException.class)
	public void testIntArrayConstructorWithNull() {
		new SelectiveCsvEncoder((int[]) null);
	}
	
	/**
	 * Tests the boolean array constructor with a null array.
	 */
	@Test(expected = NullPointerException.class)
	public void testBoolArrayConstructorWithNull() {
		new SelectiveCsvEncoder((boolean[]) null);
	}
	
}
