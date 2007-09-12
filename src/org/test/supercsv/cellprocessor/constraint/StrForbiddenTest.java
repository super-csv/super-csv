package org.test.supercsv.cellprocessor.constraint;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ParseBool;
import org.supercsv.cellprocessor.constraint.StrForbidden;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * @author Kasper B. Graversen
 */
public class StrForbiddenTest {
	CellProcessorAdaptor cp1 = null;
	CellProcessorAdaptor cp2 = null;

	@Before
	public void setup() {
		cp1 = new StrForbidden("bomb");
		cp2 = new StrForbidden(new String[] { "error", "thunder", "fatal" });
	}

	@Test
	public void testConstructors() throws Exception {
		// string[], next
		final String[] forbids = { "error", "thunder", "fatal" };
		cp2 = new StrForbidden(forbids, new ParseBool());
		Assert.assertEquals("chain test", true, cp2.execute("true", new CSVContext(0, 0)));

		// List<String>, next
		final List<String> forbidList = new ArrayList<String>();
		forbidList.add(forbids[0]);
		cp2 = new StrForbidden(forbidList, new ParseBool());
		Assert.assertEquals("chain test", true, cp2.execute("true", new CSVContext(0, 0)));

		// List<String>
		forbidList.add(forbids[0]);
		cp2 = new StrForbidden(forbidList);
		Assert.assertEquals("chain test", "true", cp2.execute("true", new CSVContext(0, 0)));

		// String, next
		cp2 = new StrForbidden(forbids[0], new ParseBool());
		Assert.assertEquals("chain test", true, cp2.execute("true", new CSVContext(0, 0)));

	}

	@Test(expected = SuperCSVException.class)
	public void testIllegal1() throws Exception {
		cp1.execute("bomb", new CSVContext(0, 0));
	}

	// illegal tests...

	@Test(expected = SuperCSVException.class)
	public void testIllegal2() throws Exception {
		cp2.execute("error", new CSVContext(0, 0));
	}

	/** test last in arr */
	@Test(expected = SuperCSVException.class)
	public void testIllegal3() throws Exception {
		cp2.execute("fatal", new CSVContext(0, 0));
	}

	@Test
	public void testLegal() throws Exception {
		Assert.assertEquals("Legal name should be passed on", "tyson", cp1.execute("tyson", new CSVContext(0, 0)));
		Assert.assertEquals("Legal name should be passed on", "tyson", cp2.execute("tyson", new CSVContext(0, 0)));
	}

}
