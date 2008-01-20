package org.supercsv.cellprocessor;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CSVContext;

/**
 * @author Kasper B. Graversen
 */
public class OptionalTest extends TestCase {
	CellProcessor	cp, ccp;

	@Override
	@Before
	public void setUp() throws Exception {
		cp = new Optional();
	}

	@Test
	public void testChaining() throws Exception {
		ccp = new Optional(new ParseLong()); // chain processors
		Assert.assertEquals("chained optional value", 17L, ccp.execute("17", new CSVContext(0, 0)));
		Assert.assertEquals("chained optional empty value-the long conversion should not fail (not take place)", null,
				ccp.execute("", new CSVContext(0, 0)));

		ccp = new Optional(new ParseLong());
		Assert.assertEquals("chained optional value", 17L, ccp.execute("17", new CSVContext(0, 0)));
		Assert.assertEquals(
				"new retval chained optional empty value-the long conversion should not fail (not take place)", null,
				ccp.execute("", new CSVContext(0, 0)));
	}

	@Test
	public void validInputTest() throws Exception {
		Assert.assertEquals("optional string", "foo", cp.execute("foo", new CSVContext(0, 0)));
		Assert.assertEquals("optional empty", null, cp.execute("", new CSVContext(0, 0)));
	}

	@Test
	public void validInputTestNewReturnArg() throws Exception {
		ccp = new Optional();
		Assert.assertEquals("optional string new return str", "foo", ccp.execute("foo", new CSVContext(0, 0)));
		Assert.assertEquals("optional empty new return str", null, ccp.execute("", new CSVContext(0, 0)));
	}
}
