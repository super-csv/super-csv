package org.test.supercsv.cellprocessor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CSVContext;
import org.test.supercsv.mock.ComparerCellProcessor;

/**
 * @author Kasper B. Graversen
 */
public class ParseLongTest {
	CellProcessor cp, ccp;

	@Before
	public void setUp() throws Exception {
		cp = new ParseLong();
	}

	@Test
	public void testChaining() throws Exception {
		ccp = new ParseLong(new ComparerCellProcessor(17L)); // chain
		// processors
		Assert.assertEquals("chained convert possitive long", true, ccp.execute("17", new CSVContext(0, 0)));
	}

	@Test(expected = NumberFormatException.class)
	public void testEmptyInput() throws Exception {
		cp.execute("", new CSVContext(0, 0));
	}

	@Test(expected = NumberFormatException.class)
	public void testInValidInput() throws Exception {
		Assert.assertEquals("", cp.execute("hello", new CSVContext(0, 0)));
	}

	@Test
	public void validInputTest() throws Exception {
		Assert.assertEquals("convert possitive long", 17L, cp.execute("17", new CSVContext(0, 0)));
		Assert.assertEquals("convert negative long", -43L, cp.execute("-43", new CSVContext(0, 0)));
		Assert.assertEquals("convert negative long", -43L, cp.execute(new Long(-43), new CSVContext(0, 0)));
	}
}
