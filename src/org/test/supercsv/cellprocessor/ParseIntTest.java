package org.test.supercsv.cellprocessor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CSVContext;
import org.test.supercsv.mock.ComparerCellProcessor;

/**
 * @author Kasper B. Graversen
 */
public class ParseIntTest {
	final static int VAL1 = 17;
	final static String VAL1_STR = "17";
	CellProcessor cp, ccp;

	@Before
	public void setUp() throws Exception {
		cp = new ParseInt();
	}

	@Test
	public void shouldHandleInputOfTypeDoubleWithoutExtraConversion() {
		Assert.assertEquals(VAL1, new ParseInt(new Optional()).execute(VAL1_STR, new CSVContext(0, 0)));
	}

	@Test
	public void testChaining() throws Exception {
		ccp = new ParseInt(new ComparerCellProcessor(VAL1)); // chain processors
		Assert.assertEquals("convert possitive int", true, ccp.execute(VAL1_STR, new CSVContext(0, 0)));
	}

	@Test(expected = NumberFormatException.class)
	public void testEmptyInput() throws Exception {
		cp.execute("", new CSVContext(0, 0));
	}

	@Test(expected = NumberFormatException.class)
	public void testInValidInput() throws Exception {
		Assert.assertEquals(cp.execute("hello", new CSVContext(0, 0)), "");
	}

	@Test
	public void validInputTest() throws Exception {
		Assert.assertEquals("convert possitive int", VAL1, cp.execute(VAL1_STR, new CSVContext(0, 0)));
		Assert.assertEquals("convert negative int", -43, cp.execute("-43", new CSVContext(0, 0)));
	}

}
