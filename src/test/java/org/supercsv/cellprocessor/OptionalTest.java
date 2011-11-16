package org.supercsv.cellprocessor;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.TestConstants;
import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 * @author Kasper B. Graversen
 */
public class OptionalTest extends TestCase {
CellProcessor cp = null, ccp = null;

@Override
@Before
public void setUp() throws Exception {
	cp = new Optional();
}

@Test
public void testChaining() throws Exception {
	ccp = new Optional(new ParseLong()); // chain processors
	Assert.assertEquals("chained optional value", 17L, ccp.execute("17", TestConstants.ANONYMOUS_CSVCONTEXT));
	Assert.assertEquals("chained optional empty value-the long conversion should not fail (not take place)", null, ccp
		.execute("", TestConstants.ANONYMOUS_CSVCONTEXT));
	
	ccp = new Optional(new ParseLong());
	Assert.assertEquals("chained optional value", 17L, ccp.execute("17", TestConstants.ANONYMOUS_CSVCONTEXT));
	Assert.assertEquals("new retval chained optional empty value-the long conversion should not fail (not take place)",
		null, ccp.execute("", TestConstants.ANONYMOUS_CSVCONTEXT));
}

@Test
public void validInputTest() throws Exception {
	Assert.assertEquals("optional string", "foo", cp.execute("foo", TestConstants.ANONYMOUS_CSVCONTEXT));
	Assert.assertEquals("optional empty", null, cp.execute("", TestConstants.ANONYMOUS_CSVCONTEXT));
}

@Test
public void validInputTestNewReturnArg() throws Exception {
	ccp = new Optional();
	Assert
		.assertEquals("optional string new return str", "foo", ccp.execute("foo", TestConstants.ANONYMOUS_CSVCONTEXT));
	Assert.assertEquals("optional empty new return str", null, ccp.execute("", TestConstants.ANONYMOUS_CSVCONTEXT));
}
}
