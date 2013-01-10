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

import org.junit.Test;
import org.supercsv.mock.IdentityTransform;

/**
 * Tests the CellProcessorAdaptor abstract processor.
 * 
 * @author James Bassett
 */
public class CellProcessorAdaptorTest {
	
	@Test
	public void testToString() {
		assertEquals("org.supercsv.mock.IdentityTransform", new IdentityTransform().toString());
	}
	
	/**
	 * Tests construction of an unchained processor.
	 */
	@Test
	public void testValidUnchained() {
		IdentityTransform processor = new IdentityTransform();
		assertEquals("org.supercsv.cellprocessor.CellProcessorAdaptor$NullObjectPattern", processor.next.getClass()
			.getName());
		
	}
	
	/**
	 * Tests construction of an processor chain.
	 */
	@Test
	public void testValidChained() {
		ConvertNullTo processor = new ConvertNullTo("null");
		IdentityTransform processorChain = new IdentityTransform(processor);
		assertEquals(processor, processorChain.next);
	}
	
	/**
	 * Tests construction of a processor chain with a null processor (should throw an Exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testChainedWithNull() {
		new IdentityTransform(null);
	}
	
}
