package org.supercsv.io;

import java.io.IOException;
import java.io.StringReader;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.prefs.CsvPreference;

/**
 * @author Kasper B. Graversen
 */
public class AbstractCsvReaderTest {
StringReader r;
CsvListReader csvr;
CsvPreference prefs;

/** testing general functionality */
@Test
public void generalTest() throws IOException {
	csvr.read();
	Assert.assertEquals("get test", "aa", csvr.get(0));
	Assert.assertEquals("get test", "eeee", csvr.get(4));
	Assert.assertEquals("length test", 5, csvr.length());
	csvr.read();
	Assert.assertEquals("length test", 4, csvr.length());
	csvr.close();
}

@Test(expected = SuperCSVException.class)
public void headerErrorTest() throws IOException {
	csvr.getCSVHeader(true);
	csvr.getCSVHeader(true); // cannot fetch header twice
}

public void headerTest() throws IOException {
	csvr.getCSVHeader(false);
	csvr.getCSVHeader(false); // can fetch header twice
}

@Test(expected = RuntimeException.class)
public void setInputErrorTest() {
	csvr.setInput(null);
}

@Before
public void setUp() {
	r = new StringReader("aa,b,ccc,dd,eeee\nkk,l,mmm,nnnn\n");
	prefs = new CsvPreference('"', ',', "\n");
	csvr = new CsvListReader(r, prefs);
}

}
