package org.supercsv.cellprocessor.constraint;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.TestConstants;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCSVException;

/**
 * @author Kasper B. Graversen
 */
public class UniqueTest {
CellProcessor cp = null, ccp = null;

@Before
public void setUp() throws Exception {
	cp = new Unique();
}

@Test(expected = SuperCSVException.class)
public void testInNonUniqueInput() throws Exception {
	Assert.assertEquals("first insert ok", "zello", cp.execute("zello", TestConstants.ANONYMOUS_CSVCONTEXT));
	Assert.assertEquals("second insert must fail", "zello", cp.execute("zello", TestConstants.ANONYMOUS_CSVCONTEXT));
}

public void testInValidInput() throws Exception {
	Assert.assertEquals("first insert ok", "zello", cp.execute("zello", TestConstants.ANONYMOUS_CSVCONTEXT));
}

@Test
public void validInputAndChainingTest() throws Exception {
	ccp = new Unique(new ParseLong());
	// test numbers and chaining
	Assert.assertEquals("test numbers", 17L, ccp.execute("17", TestConstants.ANONYMOUS_CSVCONTEXT));
	Assert.assertEquals("test negative numbers", -17L, ccp.execute("-17", TestConstants.ANONYMOUS_CSVCONTEXT));
}

@Test
public void validInputTest() throws Exception {
	// simpletest
	Assert.assertEquals("test lowercase", "hello", cp.execute("hello", TestConstants.ANONYMOUS_CSVCONTEXT));
	Assert.assertEquals("test uppercase", "HELLO", cp.execute("HELLO", TestConstants.ANONYMOUS_CSVCONTEXT));
	Assert.assertEquals("test empty", "", cp.execute("", TestConstants.ANONYMOUS_CSVCONTEXT));
	
}
}
