package org.supercsv.cellprocessor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.supercsv.TestConstants.ANONYMOUS_CSVCONTEXT;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.NullInputException;
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
	public void testEmptyString(){
		assertNull(processor.execute("", ANONYMOUS_CSVCONTEXT));
		assertNull(processorChain.execute("", ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests unchained/chained execution with a space as input.
	 */
	@Test
	public void testSpace(){
		assertEquals(" ", processor.execute(" ", ANONYMOUS_CSVCONTEXT));
		assertEquals(" ", processorChain.execute(" ", ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests unchained/chained execution with normal input (not "").
	 */
	@Test
	public void testNormalInput(){
		String normal = "normal";
		assertEquals(normal, processor.execute(normal, ANONYMOUS_CSVCONTEXT));
		assertEquals(normal, processorChain.execute(normal, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests execution with a null input (should throw an Exception).
	 */
	@Test(expected = NullInputException.class)
	public void testWithNull() {
		processor.execute(null, ANONYMOUS_CSVCONTEXT);
	}
}
