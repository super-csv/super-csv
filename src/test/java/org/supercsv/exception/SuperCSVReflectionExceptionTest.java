package org.supercsv.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Tests the SuperCSVReflectionException class.
 * 
 * @author James Bassett
 */
public class SuperCSVReflectionExceptionTest {
	
	private static final String MSG = "Reflection failed";
	private static final Throwable THROWABLE = new RuntimeException("Mirror is broken");
	
	/**
	 * Tests the first constructor.
	 */
	@Test
	public void testConstructor1(){
		SuperCSVReflectionException e = new SuperCSVReflectionException(MSG);
		assertEquals(MSG, e.getMessage());
		e.printStackTrace();
		
		// test with null msg
		e = new SuperCSVReflectionException(null);
		assertNull(e.getMessage());
		e.printStackTrace();
	}
	
	/**
	 * Tests the second constructor.
	 */
	@Test
	public void testConstructor2(){
		SuperCSVReflectionException e = new SuperCSVReflectionException(MSG, THROWABLE);
		assertEquals(MSG, e.getMessage());
		assertEquals(THROWABLE, e.getCause());
		e.printStackTrace();
		
		// test with null msg
		e = new SuperCSVReflectionException(null, null);
		assertNull(e.getMessage());
		assertNull(e.getCause());
		e.printStackTrace();
	}
	
}
