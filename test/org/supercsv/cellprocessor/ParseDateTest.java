package org.supercsv.cellprocessor;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.mock.ComparerCellProcessor;
import org.supercsv.util.CSVContext;

/**
 * @author Kasper B. Graversen
 */
public class ParseDateTest {
	CellProcessor	cp, ccp;

	@Before
	public void setUp() throws Exception {
		cp = new ParseDate("dd/MM/yy");
	}

	@Test
	public void testChaining() throws Exception {
		ccp = new ParseDate("dd/MM/yy", new ComparerCellProcessor(new Date(2005 - 1900, 1 - 1, 1))); // chain
		// processors
		Assert.assertEquals("get date", true, ccp.execute("1/1/2005", new CSVContext(0, 0)));

		ccp = new ParseDate("dd-MM-yy", new ComparerCellProcessor(new Date(2005 - 1900, 1 - 1, 1))); // chain
		// processors
		Assert.assertEquals("get date", true, ccp.execute("1-1-2005", new CSVContext(0, 0)));
	}

	@Test(expected = SuperCSVException.class)
	public void testEmptyInput() throws Exception {
		cp.execute("", new CSVContext(0, 0));
	}

	@Test(expected = AssertionError.class)
	public void testInValidInput() throws Exception {
		Assert.assertEquals("never reached", cp.execute("21/21/21", new CSVContext(0, 0)));
	}

	@Test(expected = SuperCSVException.class)
	public void testInValidInput2() throws Exception {
		Assert.assertEquals("never reached", cp.execute("a date", new CSVContext(0, 0)));
	}

	@Test
	public void validInputTest() throws Exception {
		Assert.assertEquals("read date", new Date(2007 - 1900, 4 - 1, 17), cp.execute("17/04/2007",
				new CSVContext(0, 0)));

		cp = new ParseDate("MM-dd-yy");
		Assert.assertEquals("read date", new Date(2006 - 1900, 4 - 1, 17), cp.execute("04-17-2006",
				new CSVContext(0, 0)));
	}
}
