package org.supercsv.cellprocessor.constraint;

import static org.junit.Assert.assertEquals;
import static org.supercsv.TestConstants.ANONYMOUS_CSVCONTEXT;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.mock.IdentityTransform;

/**
 * Tests the StrForbidden constraint.
 * 
 * @deprecated copy-paste of ForbidSubStrTest: to be removed when ForbidSubStr is removed
 * @author Kasper B. Graversen
 * @author James Bassett
 */
public class StrForbiddenTest {
	
	private static final String FORBIDDEN = "C++";
	private static final String FORBIDDEN2 = "Microsoft";
	
	private CellProcessor processor;
	private CellProcessor processor2;
	private CellProcessor processorChain;
	private CellProcessor processorChain2;
	private CellProcessor processorChain3;
	
	/**
	 * Sets up the processors for the test using all constructor combinations.
	 */
	@Before
	public void setUp() {
		processor = new StrForbidden(Arrays.asList(FORBIDDEN, FORBIDDEN2));
		processor2 = new StrForbidden(FORBIDDEN, FORBIDDEN2);
		processorChain = new StrForbidden(Arrays.asList(FORBIDDEN, FORBIDDEN2), new IdentityTransform());
		processorChain2 = new StrForbidden(FORBIDDEN, new IdentityTransform());
		processorChain3 = new StrForbidden(new String[] { FORBIDDEN, FORBIDDEN2 }, new IdentityTransform());
	}
	
	/**
	 * Tests unchained/chained execution with a String that doesn't contain any forbidden Strings.
	 */
	@Test
	public void testValidInput() {
		String input = "I think Java is an awesome language";
		assertEquals(input, processor.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(input, processor2.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(input, processorChain.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(input, processorChain2.execute(input, ANONYMOUS_CSVCONTEXT));
		assertEquals(input, processorChain3.execute(input, ANONYMOUS_CSVCONTEXT));
		
	}
	
	/**
	 * Tests input that contains the first forbidden String (should throw an Exception).
	 */
	@Test(expected = SuperCSVException.class)
	public void testForbidden() {
		String input = "I think C++ is an awesome language"; // blasphemy!
		processor.execute(input, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests input that contains the second forbidden String (should throw an Exception).
	 */
	@Test(expected = SuperCSVException.class)
	public void testForbidden2() {
		String input = "I love Microsoft";
		processor.execute(input, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with a null input (should throw an Exception).
	 */
	@Test(expected = NullInputException.class)
	public void testWithNull() {
		processor.execute(null, ANONYMOUS_CSVCONTEXT);
	}
}
