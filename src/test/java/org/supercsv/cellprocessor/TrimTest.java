package org.supercsv.cellprocessor;

import static org.junit.Assert.assertEquals;
import static org.supercsv.TestConstants.ANONYMOUS_CSVCONTEXT;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.mock.IdentityTransform;

/**
 * Tests the Trim processor.
 * 
 * @author Kasper B. Graversen
 * @author James Bassett
 */
public class TrimTest {
	
	private static final int MAX_SIZE = 3;
	private static final String POSTFIX = "...";
	
	private CellProcessor processor;
	private CellProcessor processor2;
	private CellProcessor processorChain;
	private CellProcessor processorChain2;
	
	/**
	 * Sets up the processors for the test using all constructor combinations.
	 */
	@Before
	public void setUp() {
		processor = new Trim(MAX_SIZE);
		processor2 = new Trim(MAX_SIZE, POSTFIX);
		processorChain = new Trim(MAX_SIZE, new IdentityTransform());
		processorChain2 = new Trim(MAX_SIZE, POSTFIX, new IdentityTransform());
	}
	
	/**
	 * Tests unchained/chained execution with input the same as the max size (no trimming!).
	 */
	@Test
	public void testInputSameAsMax() {
		String input = "abc";
		assertEquals(input, processor.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(input, processor2.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(input, processorChain.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(input, processorChain2.execute(input, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests unchained/chained execution with input longer than the max size (trimming!).
	 */
	@Test
	public void testInputLongerThanMax() {
		String input = "abcd";
		String expected = "abc";
		String expectedPostFix = expected + "...";
		
		// no postfix
		assertEquals(expected, processor.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(expected, processorChain.execute(input, ANONYMOUS_CSVCONTEXT));
		
		// postfix
		assertEquals(expectedPostFix, processor2.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(expectedPostFix, processorChain2.execute(input, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests execution with an invalid max value (should throw an Exception).
	 */
	@Test(expected = SuperCSVException.class)
	public void testWithInvalidMax() {
		processor = new Trim(0);
	}
	
	/**
	 * Tests execution with an invalid max value and chaining (should throw an Exception).
	 */
	@Test(expected = SuperCSVException.class)
	public void testWithInvalidMaxChained() {
		processorChain = new Trim(0, new IdentityTransform());
	}
	
	/**
	 * Tests execution with a null input (should throw an Exception).
	 */
	@Test(expected = NullInputException.class)
	public void testWithNull() {
		processor.execute(null, ANONYMOUS_CSVCONTEXT);
	}
	
}
