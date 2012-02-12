package org.supercsv.cellprocessor.constraint;

import static org.junit.Assert.assertEquals;
import static org.supercsv.TestConstants.ANONYMOUS_CSVCONTEXT;
import static org.supercsv.cellprocessor.constraint.LMinMax.MAX;
import static org.supercsv.cellprocessor.constraint.LMinMax.MIN;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.mock.IdentityTransform;

/**
 * Tests the LMinMax constraint.
 * 
 * @author Kasper B. Graversen
 * @author James Bassett
 */
public class LMinMaxTest {
	
	private CellProcessor processor;
	private CellProcessor processorChain;
	
	/**
	 * Sets up the processors for the test using all constructor combinations.
	 */
	@Before
	public void setUp() {
		processor = new LMinMax(MIN, MAX);
		processorChain = new LMinMax(MIN, MAX, new IdentityTransform());
	}
	
	/**
	 * Tests unchained/chained execution with a long in the range.
	 */
	@Test
	public void testValidLong(){
		long input = 123L;
		assertEquals(input, processor.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(input, processorChain.execute(input, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests unchained/chained execution with a long String in the range (should be converted to a Long).
	 */
	@Test
	public void testValidLongString(){
		String input = "123";
		Long expected = 123L;
		assertEquals(expected, processor.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(expected, processorChain.execute(input, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests unchained/chained execution with the minimum allowed value.
	 */
	@Test
	public void testMinBoundary(){
		long input = MIN;
		assertEquals(input, processor.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(input, processorChain.execute(input, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests unchained/chained execution with the maximum allowed value.
	 */
	@Test
	public void testMaxBoundary(){
		long input = MAX;
		assertEquals(input, processor.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(input, processorChain.execute(input, ANONYMOUS_CSVCONTEXT));
	}
	

	/**
	 * Tests execution with a value less than the minimum (should throw an Exception).
	 */
	@Test(expected = SuperCSVException.class)
	public void testLessThanMin() {
		long lessThanMin = MIN - 1L;
		processor.execute(lessThanMin, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with a value greater than the maximum (should throw an Exception).
	 */
	@Test(expected = SuperCSVException.class)
	public void testGreaterThanMax() {
		long greaterThanMax = MAX + 1L;
		processor.execute(greaterThanMax, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with a String that can't be parsed to a Long (should throw an Exception).
	 */
	@Test(expected = SuperCSVException.class)
	public void testWithNonLongString() {
		processor.execute("not long!", ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with a max < min (should throw an Exception).
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testWithInvalidMaxMin() {
		new LMinMax(MAX, MIN);
	}
	
	/**
	 * Tests execution with a null input (should throw an Exception).
	 */
	@Test(expected = NullInputException.class)
	public void testWithNull() {
		processor.execute(null, ANONYMOUS_CSVCONTEXT);
	}
}
