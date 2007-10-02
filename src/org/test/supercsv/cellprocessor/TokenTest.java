package org.test.supercsv.cellprocessor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.Token;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CSVContext;

/**
 * @author Kasper B. Graversen
 */
public class TokenTest {
	CellProcessor cp, ccp;

	@Before
	public void setUp() throws Exception {
		cp = new Token("", null);
	}

	@Test
	public void testChainedConstructon() throws Exception {
		cp = new Token("foo", "bar", new ParseLong());
		Assert.assertEquals("new ret val for new magic token", "bar", cp.execute("foo", new CSVContext(0, 0)));
	}

	@Test
	public void testChaining() throws Exception {
		ccp = new Token("[empty]", 0, new ParseLong()); // chain processors
		Assert.assertEquals("chained optional value", 17L, ccp.execute("17", new CSVContext(0, 0)));
		Assert.assertEquals("magic token works before parsing number", 0, ccp.execute("[empty]", new CSVContext(0, 0)));
	}

	@Test
	public void testConstructon() throws Exception {
		cp = new Token("foo", "bar");
		Assert.assertEquals("new ret val for new magic token", "bar", cp.execute("foo", new CSVContext(0, 0)));
		Assert.assertEquals("org val for magic token", "bob", cp.execute("bob", new CSVContext(0, 0)));
	}

}
