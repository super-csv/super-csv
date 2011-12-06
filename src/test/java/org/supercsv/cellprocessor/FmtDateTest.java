package org.supercsv.cellprocessor;

import static org.junit.Assert.assertEquals;
import static org.supercsv.TestConstants.ANONYMOUS_CSVCONTEXT;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.ClassCastInputCSVException;
import org.supercsv.exception.NullInputException;
import org.supercsv.mock.IdentityTransform;
import org.supercsv.util.DateHelper;

/**
 * Tests the FmtDate processor.
 * 
 * @author Dominique De Vito
 * @author James Bassett
 */
public class FmtDateTest {
	
	private static final String DATE_FORMAT = "dd/MM/yyyy";
	private static final Date DATE = DateHelper.date(2011, 12, 25);
	private static final String FORMATTED_DATE = "25/12/2011";
	
	private CellProcessor processor;
	private CellProcessor processorChain;
	
	/**
	 * Sets up the processors for the test using all constructor combinations.
	 */
	@Before
	public void setUp() {
		processor = new FmtDate(DATE_FORMAT);
		processorChain = new FmtDate(DATE_FORMAT, new IdentityTransform());
	}
	
	/**
	 * Tests unchained/chained execution with a valid date.
	 */
	@Test
	public void testWithValidDate(){
		assertEquals(FORMATTED_DATE, processor.execute(DATE, ANONYMOUS_CSVCONTEXT));
		assertEquals(FORMATTED_DATE, processorChain.execute(DATE, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests execution with a null input (should throw an Exception).
	 */
	@Test(expected = NullInputException.class)
	public void testWithNull() {
		processor.execute(null, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with a non-Date input (should throw an Exception).
	 */
	@Test(expected = ClassCastInputCSVException.class)
	public void testWithNonDate() {
		processor.execute(123, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with a invalid date format (should throw an Exception).
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testWithInvalidDateFormat() {
		CellProcessor invalidDateFormatProcessor = new FmtDate("abcd");
		invalidDateFormatProcessor.execute(DATE, ANONYMOUS_CSVCONTEXT);
	}
}
