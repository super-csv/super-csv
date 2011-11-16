package org.supercsv.cellprocessor.constraint;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ParseBool;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * @author Kasper B. Graversen
 */
public class ForbidSubStrTest {
private static final CSVContext CSVCONTEXT = new CSVContext(0, 0);
CellProcessorAdaptor cp1 = null;
CellProcessorAdaptor cp2 = null;

@Before
public void setup() {
	cp1 = new ForbidSubStr("bomb");
	cp2 = new ForbidSubStr("error", "thunder", "fatal");
}

@Test
public void testConstructors() throws Exception {
	// string[], next
	final String[] forbids = { "error", "thunder", "fatal" };
	cp2 = new ForbidSubStr(forbids, new ParseBool());
	Assert.assertEquals("chain test", true, cp2.execute("true", CSVCONTEXT));
	
	// List<String>, next
	final List<String> forbidList = new ArrayList<String>();
	forbidList.add(forbids[0]);
	cp2 = new ForbidSubStr(forbidList, new ParseBool());
	Assert.assertEquals("chain test", true, cp2.execute("true", CSVCONTEXT));
	
	// List<String>
	forbidList.add(forbids[0]);
	cp2 = new ForbidSubStr(forbidList);
	Assert.assertEquals("chain test", "true", cp2.execute("true", CSVCONTEXT));
	
	// String, next
	cp2 = new ForbidSubStr(forbids[0], new ParseBool());
	Assert.assertEquals("chain test", true, cp2.execute("true", CSVCONTEXT));
	
}

@Test(expected = SuperCSVException.class)
public void testIllegal1() throws Exception {
	cp1.execute("bomb", CSVCONTEXT);
}

// illegal tests...

@Test(expected = SuperCSVException.class)
public void testIllegal2() throws Exception {
	cp2.execute("error", CSVCONTEXT);
}

/** test last in arr */
@Test(expected = SuperCSVException.class)
public void testIllegal3() throws Exception {
	cp2.execute("fatal", CSVCONTEXT);
}

@Test
public void testLegal() throws Exception {
	Assert.assertEquals("Legal name should be passed on", "tyson", cp1.execute("tyson", CSVCONTEXT));
	Assert.assertEquals("Legal name should be passed on", "tyson", cp2.execute("tyson", CSVCONTEXT));
}

}
