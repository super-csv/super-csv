package org.supercsv.io;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.Trim;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.prefs.CsvPreference;

/**
 * @author Kasper B. Graversen
 */
public class CsvListReaderTest {
CsvListReader inFile = null;

@Before
public void setUp() throws Exception {
	final String str = "a!a,b.b,\"cc,dd\",\r" + "e:e,a@A$\n" + "f;f,g g,h/h\n" + "i'i,\"\"\"hej\"\"\" \n" + ""; // various
	// sign, "
	// mac and
	// pc
	// newline
	inFile = new CsvListReader(new StringReader(str), new CsvPreference('"', ',', "\n"));
}

@Test(expected = SuperCSVException.class)
public void testCellprocessor_fail() throws IOException {
	List<String> l;
	final StringBuilder sb = new StringBuilder();
	l = inFile.read(new CellProcessor[] { null });
	Assert.assertEquals("read 4 columns", 4, l.size());
	Assert.assertEquals("a", l.get(0));
	Assert.assertEquals("b.", l.get(1));
	Assert.assertEquals("cc,dd", l.get(2));
	Assert.assertEquals("no errors", "", sb.toString());
}

/**
 * Test method for {@link org.supercsv.io.AbstractCsvReader#getLineNumber()}.
 */
@Test
public void testNewLines() throws IOException {
	List<String> l;
	l = inFile.read();
	Assert.assertNotNull(l);
	Assert.assertEquals(1, inFile.getLineNumber());
	l = inFile.read();
	Assert.assertNotNull(l);
	Assert.assertEquals(2, inFile.getLineNumber());
	l = inFile.read();
	Assert.assertNotNull(l);
	Assert.assertEquals(3, inFile.getLineNumber());
	l = inFile.read();
	Assert.assertNotNull(l);
	Assert.assertEquals(4, inFile.getLineNumber());
	l = inFile.read();
	Assert.assertNull(l);
	Assert.assertEquals(4, inFile.getLineNumber());
}

@Test
public void testProcessorRead() throws IOException {
	List<String> l;
	l = inFile.read(new CellProcessor[] { new Trim(1), new Trim(2), null, null });
	Assert.assertEquals("read 4 columns", 4, l.size());
	Assert.assertEquals("a", l.get(0));
	Assert.assertEquals("b.", l.get(1));
	Assert.assertEquals("cc,dd", l.get(2));
	
	// test input array is cleared after each read
	l = inFile.read(new CellProcessor[] { new Trim(1), null });
	// Assert.assertEquals("read 2 columns", 2, l.size());
	Assert.assertEquals("e", l.get(0));
	Assert.assertEquals("a@A$", l.get(1));
}

@Test
public void testVariousCharacters() throws IOException {
	List<String> l;
	l = inFile.read();
	Assert.assertEquals("read 4 columns", 4, l.size());
	Assert.assertEquals("! test", "a!a", l.get(0));
	Assert.assertEquals(". test", "b.b", l.get(1));
	Assert.assertEquals("\" test", "cc,dd", l.get(2));
	Assert.assertEquals("empty test", "", l.get(3));
	
	l = inFile.read();
	Assert.assertEquals("read 2 columns", 2, l.size());
	Assert.assertEquals(": test", "e:e", l.get(0));
	Assert.assertEquals(": test", "a@A$", l.get(1));
	
	l = inFile.read();
	Assert.assertEquals("read 3 columns", 3, l.size());
	Assert.assertEquals("; test", "f;f", l.get(0));
	Assert.assertEquals("  test", "g g", l.get(1));
	Assert.assertEquals("  test", "h/h", l.get(2));
}
}
