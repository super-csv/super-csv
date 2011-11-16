package org.supercsv.io;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.ConvertNullTo;
import org.supercsv.cellprocessor.Trim;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.prefs.CsvPreference;

/**
 * @author Kasper B. Graversen
 */
public class CsvListWriterTest {
CsvPreference prefs;
CsvListWriter cw;
StringWriter writer;

@Before
public void setUp() throws Exception {
	prefs = new CsvPreference('"', ',', "\n");
	writer = new StringWriter();
	cw = new CsvListWriter(writer, prefs);
}

@Test(expected = SuperCSVException.class)
public void writeFailure() throws Exception {
	cw.write(new String[] {});
}

@Test
public void writeNColInklDelimiterAndNewline() throws IOException {
	cw.write(new String[] { "hel", "lo", "wor", "ld" });
	cw.write(new String[] { "I'm\na", "Berliner\nso high\nso, what", "3", "4" });
	cw.close(); // flush before compare
	Assert.assertEquals("write N Col Inkl Delimiter And Newline",
		"hel,lo,wor,ld\n\"I'm\na\",\"Berliner\nso high\nso, what\",3,4\n", writer.toString());
}

@Test
public void writeOneCol() throws IOException {
	Assert.assertEquals("start of file", 1, cw.getLineNumber());
	cw.write(new String[] { "hello" });
	Assert.assertEquals("one line in file", 2, cw.getLineNumber());
	cw.write(new String[] { "world" }); // write string array
	cw.write(new Integer[] { new Integer(1) }); // write obj array
	cw.close(); // flush before compare
	Assert.assertEquals("one column writes ", "hello\nworld\n1\n", writer.toString());
}

@Test
public void writeProcessedData() throws IOException {
	final CellProcessor[] processors = new CellProcessor[] { new Trim(1), new Trim(1) };
	final List<String> content = new ArrayList<String>();
	content.add("hello");
	content.add("world");
	cw.write(content, processors);
	content.clear();
	content.add("Im");
	content.add("aBerliner");
	cw.write(content, processors);
	final List<Integer> icontent = new ArrayList<Integer>();
	icontent.add(new Integer(1));
	icontent.add(new Integer(22));
	cw.write(icontent, processors);
	cw.close(); // flush before compare
	Assert.assertEquals("trimmed two column writes ", "h,w\nI,a\n1,2\n", writer.toString());
}

@Test
public void writeTwoCol() throws IOException {
	cw.write(new String[] { "hello", "world" });
	cw.write(new String[] { "Im", "aBerliner" });
	cw.write(new Integer[] { new Integer(1), new Integer(22) }); // write obj array
	cw.close(); // flush before compare
	Assert.assertEquals("two column writes ", "hello,world\nIm,aBerliner\n1,22\n", writer.toString());
}

@Test
public void writeTwoColList() throws IOException {
	final List<String> l = new ArrayList<String>();
	l.add("hello");
	l.add("world");
	cw.write(l);
	l.clear();
	
	l.add("Im");
	l.add("aBerliner");
	cw.write(l);
	l.clear();
	cw.close(); // flush before compare
	Assert.assertEquals("two column writes ", "hello,world\nIm,aBerliner\n", writer.toString());
}

@Test
public void writeNullEntryAndConvertIt() throws IOException {
	final List<String> l = new ArrayList<String>();
	l.add("hello");
	l.add(null);
	cw.write(l, new CellProcessor[] { null, new ConvertNullTo("null value") });
	cw.close();
	Assert.assertEquals("hello,null value\n", writer.toString());
}

@Test(expected = SuperCSVException.class)
public void writeWithProcessor_fail() throws IOException {
	final List<String> l = new ArrayList<String>();
	l.add("hello");
	l.add("world");
	cw.write(l, new CellProcessor[0]);
}
}
