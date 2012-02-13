package org.supercsv.cellprocessor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.supercsv.TestConstants.ANONYMOUS_CSVCONTEXT;

import java.util.Arrays;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.ClassCastInputCSVException;
import org.supercsv.exception.NullInputException;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.mock.IdentityTransform;
import org.supercsv.util.DateHelper;

/**
 * Tests the ParseDate processor.
 * 
 * @author Kasper B. Graversen
 * @author James Bassett
 */
public class ParseDateTest {
	
	private static final Date DATE = DateHelper.date(2011, 12, 25);
	private static final String DATE_FORMAT = "dd/MM/yyyy";
	private static final String FORMATTED_DATE = "25/12/2011";
	private static final String DATE_FORMAT2 = "EEE, MMM d, ''yy";
	private static final String FORMATTED_DATE2 = "Sun, Dec 25, '11";
	
	private CellProcessor processor;
	private CellProcessor processor2;
	private CellProcessor processor3;
	private CellProcessor processorChain;
	private CellProcessor processorChain2;
	private CellProcessor processorChain3;
	
	/**
	 * Sets up the processors for the test using all constructor combinations.
	 */
	@Before
	public void setUp() {
		processor = new ParseDate(DATE_FORMAT);
		processor2 = new ParseDate(DATE_FORMAT, true);
		processor3 = new ParseDate(DATE_FORMAT, false);
		processorChain = new ParseDate(DATE_FORMAT, new IdentityTransform());
		processorChain2 = new ParseDate(DATE_FORMAT, true, new IdentityTransform());
		processorChain3 = new ParseDate(DATE_FORMAT, false, new IdentityTransform());
	}
	
	/**
	 * Tests unchained/chained execution with various date formats.
	 */
	@Test
	public void testValidDate(){
		// first date format
		assertEquals(DATE, processor.execute(FORMATTED_DATE, ANONYMOUS_CSVCONTEXT));
		assertEquals(DATE, processor2.execute(FORMATTED_DATE, ANONYMOUS_CSVCONTEXT));
		assertEquals(DATE, processor3.execute(FORMATTED_DATE, ANONYMOUS_CSVCONTEXT));
		assertEquals(DATE, processorChain.execute(FORMATTED_DATE, ANONYMOUS_CSVCONTEXT));
		assertEquals(DATE, processorChain2.execute(FORMATTED_DATE, ANONYMOUS_CSVCONTEXT));
		assertEquals(DATE, processorChain3.execute(FORMATTED_DATE, ANONYMOUS_CSVCONTEXT));

	}
	
	/**
	 * Tests unchained/chained execution with a different date format.
	 */
	@Test
	public void testValidDateDifferentFormat(){
		
		CellProcessor differentFormat = new ParseDate(DATE_FORMAT2);
		CellProcessor differentFormatChain = new ParseDate(DATE_FORMAT2, new IdentityTransform());
		
		// try a different date format
		assertEquals(DATE, differentFormat.execute(FORMATTED_DATE2, ANONYMOUS_CSVCONTEXT));
		assertEquals(DATE, differentFormatChain.execute(FORMATTED_DATE2, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests execution with an invalid date (doesn't match format), should throw an exception.
	 */
	@Test(expected = SuperCSVException.class)
	public void testBadlyFormattedDate() {
		processor.execute("2011-12-25", ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with an invalid date (matches format, but data invalid) and non-lenient processors (should throw an exception).
	 */
	@Test
	public void testInvalidDateWithNonLenient() {
		String dodgyDate = "30/02/2012";
		for (CellProcessor cp : Arrays.asList(processor, processor3, processorChain, processorChain3)){
			try {
				cp.execute(dodgyDate, ANONYMOUS_CSVCONTEXT);
				fail("should have thrown a SuperCSVException");
				} catch (SuperCSVException e){
				}
		}
	}
	
	/**
	 * Tests execution with an invalid date (matches format, but data invalid) and lenient processors (should work!).
	 */
	@Test
	public void testInvalidDateWithLenient() {
		String dodgyDate = "30/02/2012";
		Date expectedDate = DateHelper.date(2012, 3, 1);
		assertEquals(expectedDate, processor2.execute(dodgyDate, ANONYMOUS_CSVCONTEXT));
		assertEquals(expectedDate, processorChain2.execute(dodgyDate, ANONYMOUS_CSVCONTEXT));
	}
	
	/**
	 * Tests execution with a non String input (should throw an exception).
	 */
	@Test(expected = ClassCastInputCSVException.class)
	public void testWithNonCharInput() {
		processor.execute(1, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests execution with a null input (should throw an Exception).
	 */
	@Test(expected = NullInputException.class)
	public void testWithNull() {
		processor.execute(null, ANONYMOUS_CSVCONTEXT);
	}
	
	/**
	 * Tests construction with a null date format (should throw an Exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructionWithNullDateFormat() {
		new ParseDate(null);
	}
	
}
