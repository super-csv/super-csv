package org.supercsv.cellprocessor.constraint;

import static org.junit.Assert.assertEquals;
import static org.supercsv.TestConstants.ANONYMOUS_CSVCONTEXT;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.mock.IdentityTransform;

/**
 * Tests the RequireHashCode constraint.
 * 
 * @author Kasper B. Graversen
 * @author James Bassett
 */
public class RequireHashCodeTest {
	
	private static final String INPUT1 = "One";
	private static final String INPUT2 = "Two";
	private static final int HASH1 = INPUT1.hashCode();
	private static final int HASH2 = INPUT2.hashCode();
	
	private CellProcessor processor;
	private CellProcessor processorChain;
	private CellProcessor processorChain2;
	
	/**
	 * Sets up the processors for the test using all constructor combinations.
	 */
	@Before
	public void setUp() {
		processor = new RequireHashCode(HASH1, HASH2);
		processorChain = new RequireHashCode(HASH1, new IdentityTransform());
		processorChain2 = new RequireHashCode(new int[] { HASH1, HASH2 }, new IdentityTransform());
	}
	
	/**
	 * Tests unchained/chained execution with valid values.
	 */
	@Test
	public void testValidInput() {
		assertEquals(INPUT1, processor.execute(INPUT1, ANONYMOUS_CSVCONTEXT));
		assertEquals(INPUT1, processorChain.execute(INPUT1, ANONYMOUS_CSVCONTEXT));
		assertEquals(INPUT1, processorChain2.execute(INPUT1, ANONYMOUS_CSVCONTEXT));
		
		assertEquals(INPUT2, processor.execute(INPUT2, ANONYMOUS_CSVCONTEXT));
		// 'processorChain' doesn't have a second value to test with
		assertEquals(INPUT2, processorChain2.execute(INPUT2, ANONYMOUS_CSVCONTEXT));
	}
	
	
	/**
	 * Tests execution with input that doesn't match any of the hashcodes (should throw an Exception).
	 */
	@Test(expected = SuperCSVException.class)
	public void testInvalidInput() {
		String input = "invalid";
		assertEquals(input, processor.execute(input, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests execution with a null input (should throw an Exception).
	 */
	@Test(expected = NullInputException.class)
	public void testWithNull() {
		processor.execute(null, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests construction with a null array (should throw an Exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructionWithNullArray() {
		new RequireHashCode((int[]) null);
	}
	
	/**
	 * Tests construction with an empty array (should throw an Exception).
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructionWithEmptyArray() {
		new RequireHashCode(new int[]{});
	}
}
