package org.supercsv.cellprocessor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.TestConstants;
import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 * @author Kasper B. Graversen
 */
public class TokenTest {
CellProcessor cp = null, ccp = null;

@Before
public void setUp() throws Exception {
	cp = new Token("", null);
}

@Test
public void testChainedConstructon() throws Exception {
	cp = new Token("foo", "bar", new ParseLong());
	Assert
		.assertEquals("new ret val for new magic token", "bar", cp.execute("foo", TestConstants.ANONYMOUS_CSVCONTEXT));
}

@Test
public void testChaining() throws Exception {
	ccp = new Token("[empty]", 0, new ParseLong()); // chain processors
	Assert.assertEquals("chained optional value", 17L, ccp.execute("17", TestConstants.ANONYMOUS_CSVCONTEXT));
	Assert.assertEquals("magic token works before parsing number", 0, ccp.execute("[empty]",
		TestConstants.ANONYMOUS_CSVCONTEXT));
}

@Test
public void testConstructon() throws Exception {
	cp = new Token("foo", "bar");
	Assert
		.assertEquals("new ret val for new magic token", "bar", cp.execute("foo", TestConstants.ANONYMOUS_CSVCONTEXT));
	Assert.assertEquals("org val for magic token", "bob", cp.execute("bob", TestConstants.ANONYMOUS_CSVCONTEXT));
}

}
