package org.supercsv.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.supercsv.TestConstants.ANONYMOUS_CSVCONTEXT;
import org.junit.Test;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.mock.IdentityTransform;
import org.supercsv.util.CSVContext;

/**
 * Tests the SuperCSVException class.
 * 
 * @author James Bassett
 */
public class SuperCSVExceptionTest {
	
	private static final String MSG = "Something terrible happened!";
	private static final Throwable THROWABLE = new RuntimeException("I'm the cause of the problem");
	private static final CellProcessor PROCESSOR = new IdentityTransform();
	
	/**
	 * Tests the first constructor.
	 */
	@Test
	public void testConstuctor1(){
		SuperCSVException e = new SuperCSVException(MSG);
		assertEquals(MSG, e.getMessage());
		e.printStackTrace();
		
		// test with null msg
		e = new SuperCSVException(null);
		assertNull(e.getMessage());
		e.printStackTrace();
	}
	
	/**
	 * Tests the second constructor.
	 */
	@Test
	public void testConstuctor2(){
		SuperCSVException e = new SuperCSVException(MSG, ANONYMOUS_CSVCONTEXT);
		assertEquals(MSG, e.getMessage());
		assertEquals(ANONYMOUS_CSVCONTEXT, e.getCsvContext());
		e.printStackTrace();
		
		// test with null msg and context
		e = new SuperCSVException(null, (CSVContext) null);
		assertNull(e.getMessage());
		assertNull(e.getCsvContext());
		e.printStackTrace();
	}
	
	/**
	 * Tests the third constructor.
	 */
	@Test
	public void testConstuctor3(){
		SuperCSVException e = new SuperCSVException(MSG, ANONYMOUS_CSVCONTEXT, THROWABLE);
		assertEquals(MSG, e.getMessage());
		assertEquals(ANONYMOUS_CSVCONTEXT, e.getCsvContext());
		assertEquals(THROWABLE, e.getCause());
		e.printStackTrace();
		
		// test with null msg, context and throwable
		e = new SuperCSVException(null, (CSVContext) null, (Throwable) null);
		assertNull(e.getMessage());
		assertNull(e.getCsvContext());
		assertNull(e.getCause());
		e.printStackTrace();
	}
	
	/**
	 * Tests the fourth constructor.
	 */
	@Test
	public void testConstuctor4(){
		SuperCSVException e = new SuperCSVException(MSG, PROCESSOR);
		assertEquals(MSG, e.getMessage());
		assertEquals(PROCESSOR, e.getOffendingProcessor());
		e.printStackTrace();
		
		// test with null msg and processor
		e = new SuperCSVException(null, (CellProcessor) null);
		assertNull(e.getMessage());
		assertNull(e.getOffendingProcessor());
		e.printStackTrace();
	}
	
	/**
	 * Tests the fifth constructor.
	 */
	@Test
	public void testConstuctor5(){
		SuperCSVException e = new SuperCSVException(MSG, ANONYMOUS_CSVCONTEXT, THROWABLE);
		assertEquals(MSG, e.getMessage());
		assertEquals(ANONYMOUS_CSVCONTEXT, e.getCsvContext());
		assertEquals(THROWABLE, e.getCause());
		e.printStackTrace();
		
		// test with null msg, context and throwable
		e = new SuperCSVException(null, (CSVContext) null, (Throwable) null);
		assertNull(e.getMessage());
		assertNull(e.getCsvContext());
		assertNull(e.getCause());
		e.printStackTrace();
	}
	
	/**
	 * Tests the sixth constructor.
	 */
	@Test
	public void testConstuctor6(){
		SuperCSVException e = new SuperCSVException(MSG, ANONYMOUS_CSVCONTEXT, PROCESSOR, THROWABLE);
		assertEquals(MSG, e.getMessage());
		assertEquals(ANONYMOUS_CSVCONTEXT, e.getCsvContext());
		assertEquals(PROCESSOR, e.getOffendingProcessor());
		assertEquals(THROWABLE, e.getCause());
		e.printStackTrace();
		
		// test with null msg, context, processor and throwable
		e = new SuperCSVException(null, null, null, null);
		assertNull(e.getMessage());
		assertNull(e.getCsvContext());
		assertNull(e.getOffendingProcessor());
		assertNull(e.getCause());
		e.printStackTrace();
	}
	
	/**
	 * Tests the CSVContext setter.
	 */
	@Test
	public void testSetCsvContext(){
		SuperCSVException e = new SuperCSVException(MSG);
		e.setCsvContext(ANONYMOUS_CSVCONTEXT);
		assertEquals(MSG, e.getMessage());
		assertEquals(ANONYMOUS_CSVCONTEXT, e.getCsvContext());
		e.printStackTrace();
	}
	
}
