package org.supercsv.cellprocessor;

/**
 * @author Kasper B. Graversen
 */
import org.junit.Test;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.mock.ComparerCellProcessor;
import org.supercsv.util.CSVContext;

public class TrimTest {
CellProcessorAdaptor cp = new Trim(3);

@Test
public void chainTest() throws Exception {
	final ComparerCellProcessor cpp = new ComparerCellProcessor("foo");
	org.junit.Assert.assertEquals("trim and chain", true, new Trim(3, cpp).execute("fooo", new CSVContext(0, 0)));
	org.junit.Assert.assertEquals("trim and chain", true, new Trim(3, "", cpp).execute("fooo", new CSVContext(0, 0)));
}

@Test(expected = SuperCSVException.class)
public void illegal1() throws Exception {
	cp = new Trim(0, "");
}

@Test
public void trimTest() throws Exception {
	org.junit.Assert.assertEquals("no trim = max size", "foo", cp.execute("foo", new CSVContext(0, 0)));
	org.junit.Assert.assertEquals("no trim < max size", "fo", cp.execute("fo", new CSVContext(0, 0)));
	org.junit.Assert.assertEquals("trim", "foo", cp.execute("fooo", new CSVContext(0, 0)));
	
	cp = new Trim(3, "..");
	org.junit.Assert.assertEquals("no trim = max size", "foo", cp.execute("foo", new CSVContext(0, 0)));
	org.junit.Assert.assertEquals("trim and append", "foo..", cp.execute("fooo", new CSVContext(0, 0)));
}

}
