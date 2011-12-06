package org.supercsv.cellprocessor;

import static org.junit.Assert.assertEquals;
import static org.supercsv.TestConstants.ANONYMOUS_CSVCONTEXT;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.ClassCastInputCSVException;
import org.supercsv.exception.NullInputException;
import org.supercsv.mock.IdentityTransform;

/**
 * Tests the FmtBool processor.
 * 
 * @author Dominique De Vito
 * @author James Bassett
 */
public class FmtBoolTest {
	
	private static final String TRUE_VALUE = "y";
	private static final String FALSE_VALUE = "n";
	
	private CellProcessor processor;
	private CellProcessor processorChain;
	
	/**
	 * Sets up the processors for the test using all constructor combinations.
	 */
	@Before
	public void setUp() {
		processor = new FmtBool(TRUE_VALUE, FALSE_VALUE);
		processorChain = new FmtBool(TRUE_VALUE, FALSE_VALUE, new IdentityTransform());
	}
	
	/**
	 * Tests unchained/chained execution with true.
	 */
	@Test
	public void testWithTrue() {
		assertEquals(TRUE_VALUE, processor.execute(true, ANONYMOUS_CSVCONTEXT));
		assertEquals(TRUE_VALUE, processorChain.execute(true, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests unchained/chained execution with false.
	 */
	@Test
	public void testWithFalse() {
		assertEquals(FALSE_VALUE, processor.execute(false, ANONYMOUS_CSVCONTEXT));
		assertEquals(FALSE_VALUE, processorChain.execute(false, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests execution with a null input (should throw an Exception).
	 */
	@Test(expected = NullInputException.class)
	public void testWithNull() {
		processor.execute(null, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with a non-Boolean input (should throw an Exception).
	 */
	@Test(expected = ClassCastInputCSVException.class)
	public void testWithNonBoolean() {
		processor.execute(123, ANONYMOUS_CSVCONTEXT);
	}
	
}
