package org.test.supercsv.cellprocessor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CSVContext;
import org.test.supercsv.mock.ComparerCellProcessor;

/**
 * @author Kasper B. Graversen
 */
public class ParseDoubleTest {
	private static final double VAL2 = -43.0;
	private static final String VAL2STR = "-43.0";
	private static final double VAL1 = 17.3;
	private static final String VAL1_STR_ = "17.3";
	CellProcessor cp, ccp;

	@Before
	public void setUp() throws Exception {
		cp = new ParseDouble();
	}

	@Test
	public void shouldHandleInputOfTypeDoubleWithoutExtraConversion() {
		Assert.assertEquals(VAL1, new ParseDouble(new Optional(new ParseDouble())).execute(VAL1_STR_, new CSVContext(0, 0)));
	}

	@Test
	public void testChaining() throws Exception {
		ccp = new ParseDouble(new ComparerCellProcessor(VAL1));
		Assert.assertEquals("convert possitive double", true, ccp.execute(VAL1_STR_, new CSVContext(0, 0)));
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
		Assert.assertEquals("convert possitive", VAL1, cp.execute(VAL1_STR_, new CSVContext(0, 0)));
		Assert.assertEquals("convert negative", VAL2, cp.execute(VAL2STR, new CSVContext(0, 0)));
	}

}
