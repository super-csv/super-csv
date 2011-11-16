package org.supercsv.io;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

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
public class CsvMapWriterTest {
CsvPreference prefs;
CsvMapWriter cw;
StringWriter outfile;
Map<String, Object> m;

@Before
public void setUp() throws Exception {
	prefs = new CsvPreference('"', ',', "\n");
	outfile = new StringWriter();
	cw = new CsvMapWriter(outfile, prefs);
	
	m = new HashMap<String, Object>();
	m.put("a", "hello");
	m.put("b", "world");
	m.put("c", 1);
	
}

@Test(expected = SuperCSVException.class)
public void writeFailure() throws Exception {
	cw.write(new HashMap<String, Object>(), new String[0]);
}

@Test
public void writeOneCol() throws IOException {
	Assert.assertEquals("start of file", 1, cw.getLineNumber());
	cw.write(m, new String[] { "a" });
	Assert.assertEquals("one line in file", 2, cw.getLineNumber());
	
	cw.write(m, new String[] { "c" }); // write string array
	cw.close(); // flush before compare
	Assert.assertEquals("one column writes ", "hello\n1\n", outfile.toString());
}

@Test
public void writeTwoCol() throws IOException {
	cw.write(m, new String[] { "a", "b" });
	cw.write(m, new String[] { "b", "c" });
	cw.close(); // flush before compare
	Assert.assertEquals("two column writes ", "hello,world\nworld,1\n", outfile.toString());
}

@Test
public void writeTwoColAndHeader() throws IOException {
	cw.writeHeader("headerA", "headerB");
	cw.write(m, new String[] { "a", "b" });
	cw.write(m, new String[] { "b", "c" });
	cw.close(); // flush before compare
	Assert.assertEquals("two column writes and header", "headerA,headerB\nhello,world\nworld,1\n", outfile.toString());
}

@Test
public void writeWithProcessors() throws IOException {
	cw.write(m, new String[] { "a", "b", "c" }, new CellProcessor[] { new Trim(2), null, null });
	cw.close();
	Assert.assertEquals("written content", "he,world,1\n", outfile.toString());
}

@Test(expected = SuperCSVException.class)
public void writeWithProcessors_fail() throws IOException {
	cw.write(m, new String[] { "a", "b", "c" }, new CellProcessor[] { null });
	cw.close();
}
}
