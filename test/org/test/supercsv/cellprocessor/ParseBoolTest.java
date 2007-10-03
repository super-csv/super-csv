package org.test.supercsv.cellprocessor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.ParseBool;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;
import org.test.supercsv.mock.ComparerCellProcessor;

/**
 * @author Kasper B. Graversen
 */
public class ParseBoolTest {
	CellProcessor cp, ccp;

	@Before
	public void setUp() throws Exception {
		cp = new ParseBool();
	}

	@Test
	public void testChaining() throws Exception {
		ccp = new ParseBool(new ComparerCellProcessor(true)); // chain
		// processors
		Assert.assertEquals("parse true", true, ccp.execute("1", new CSVContext(0, 0)));
	}

	@Test(expected = SuperCSVException.class)
	public void testEmptyInput() throws Exception {
		cp.execute("", new CSVContext(0, 0));
	}

	@Test(expected = SuperCSVException.class)
	public void testInValidInput() throws Exception {
		cp.execute("foo", new CSVContext(0, 0));
	}

	@Test
	public void validInputTest() throws Exception {
		Assert.assertEquals("convert true", true, cp.execute("true", new CSVContext(0, 0)));
		Assert.assertEquals("convert false", false, cp.execute("false", new CSVContext(0, 0)));
		Assert.assertEquals("convert 1", true, cp.execute("1", new CSVContext(0, 0)));
		Assert.assertEquals("convert 0", false, cp.execute("0", new CSVContext(0, 0)));
		Assert.assertEquals("convert y", true, cp.execute("y", new CSVContext(0, 0)));
		Assert.assertEquals("convert n", false, cp.execute("n", new CSVContext(0, 0)));
		Assert.assertEquals("convert t", true, cp.execute("t", new CSVContext(0, 0)));
		Assert.assertEquals("convert f", false, cp.execute("f", new CSVContext(0, 0)));
	}
}
