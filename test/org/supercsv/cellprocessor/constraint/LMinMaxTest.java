package org.supercsv.cellprocessor.constraint;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * @author Kasper B. Graversen
 */
public class LMinMaxTest {
	LMinMax			cp;
	CellProcessor	ccp;

	@Test(expected = SuperCSVException.class)
	public void invalidmaxInputTest() throws Exception {
		Assert.assertEquals("max boundary +1", LMinMax.MAXC + 1, cp.execute(LMinMax.MAXC + 1, new CSVContext(0, 0)));
	}

	@Test(expected = SuperCSVException.class)
	public void invalidminInputTest() throws Exception {
		Assert.assertEquals("min boundary -1", 0 - 1, cp.execute(0 - 1, new CSVContext(0, 0)));
	}

	@Test(expected = SuperCSVException.class)
	public void invalidminMaxTest() throws Exception {
		Assert.assertEquals("max < min", 0, new LMinMax(200, 10));
	}

	@Test(expected = SuperCSVException.class)
	public void invalidminMaxTest_c2() throws Exception {
		Assert.assertEquals("max < min", 0, new LMinMax(200, 10, null));
	}

	@Before
	public void setUp() throws Exception {
		cp = new LMinMax(0, LMinMax.MAXC);
	}

	@Test
	public void validChainingTest() throws Exception {
		// chaining
		ccp = new LMinMax(1, 100, new Optional());
		Assert.assertEquals("test chaining ", 17L, ccp.execute("17", new CSVContext(0, 0)));
		Assert.assertEquals("test chaining ", 17L, ccp.execute(new Long(17), new CSVContext(0, 0)));
	}

	@Test
	public void validInputTest() throws Exception {
		Assert.assertEquals("min boundary as text", 0L, cp.execute("0", new CSVContext(0, 0)));
		Assert.assertEquals("min boundary as number", 0L, cp.execute(0, new CSVContext(0, 0)));
		Assert.assertEquals("max boundary as number", (long) LMinMax.MAXC, cp.execute(LMinMax.MAXC,
				new CSVContext(0, 0)));
	}
}
