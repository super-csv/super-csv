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
	public void testToString(){
		assertEquals("org.supercsv.mock.IdentityTransform", new IdentityTransform().toString());
	}
	
	/**
	 * Tests construction of an unchained processor.
	 */
	@Test
	public void testValidUnchained() {
		IdentityTransform processor = new IdentityTransform();
		assertEquals(NullObjectPattern.INSTANCE, processor.next);
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
