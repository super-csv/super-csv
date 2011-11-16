package org.supercsv.io;

import java.io.StringWriter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.Strlen;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.mock.PersonBean;
import org.supercsv.prefs.CsvPreference;

public class CsvBeanWriterTest {

CsvBeanWriter cw = null;
String[] nameMapper = { "firstname", "password", "street", "zip", "town" };
final CellProcessor[] processors = new CellProcessor[] { new Strlen(5), null, null, new Optional(new ParseInt()), null };
StringWriter outfile;
PersonBean p1;

@Before
public void setUp() throws Exception {
	p1 = new PersonBean();
	p1.setFirstname("Klaus");
	p1.setPassword("Anderson");
	p1.setStreet("Mauler Street 43");
	p1.setZip(4328);
	p1.setTown("New York");
	
	outfile = new StringWriter();
	cw = new CsvBeanWriter(outfile, new CsvPreference('"', ',', "\n"));
}

@Test(expected = SuperCSVException.class)
public void testFail() throws Exception {
	p1.setFirstname("longerthanfive");
	// final StringBuilder sb = new StringBuilder();
	cw.write(p1, nameMapper, processors);
	cw.close();
	// Assert.assertEquals("error in log", "java.lang.BestCSVException: Entry \"longerthanfive\" on line 1 is not of
	// any of the required lengths 5 ", sb.toString());
	Assert.assertEquals("Empty file", "", outfile.toString());
}

@Test
public void testWrite() throws Exception {
	cw.write(p1, nameMapper);
	cw.close();
	Assert.assertEquals("simple write", "Klaus,Anderson,Mauler Street 43,4328,New York\n", outfile.toString());
}

@Test
public void testWriteEncode() throws Exception {
	p1.setFirstname("Kla,us");
	cw.write(p1, nameMapper);
	cw.close();
	Assert.assertEquals("encode before writing", "\"Kla,us\",Anderson,Mauler Street 43,4328,New York\n", outfile
		.toString());
}

@Test
public void testWriteProcessor() throws Exception {
	cw.write(p1, nameMapper, processors);
	cw.close();
	Assert.assertEquals("processor write", "Klaus,Anderson,Mauler Street 43,4328,New York\n", outfile.toString());
}
}
