package org.supercsv.io;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.Trim;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.prefs.CsvPreference;

public class CsvMapReaderTest {
static final CellProcessor[] PROCESSORS = new CellProcessor[] { null, null, null, new Optional(new ParseInt()),
	new Optional() };
static final CellProcessor[] PROCESSORS_PARTIAL = new CellProcessor[] { new Trim(3), null, null, null, new Optional() };
static final CellProcessor[] PROCESSORS_EMPTY = new CellProcessor[] { null, null, null, null, null };
CsvMapReader inFile = null;
final String[] nameMapper = { "firstname", "lastname", "street", "zip", "town" };
final String[] nameMapperPartial = { "firstname", null, null, null, "town" };
final String[] invalidNameMapperPartial = { "firstname", null, null, null, "firstname" };

/**
 * @throws java.lang.Exception
 */
@Before
public void setUp() throws Exception {
	final String str = "Klaus,     Anderson,   Mauler Street 43,   4328,           New York\n"
		+ "Moby,      Duck,       Sesam str,              ,         \n"; // missing
	// parts
	// of
	// the
	// address
	inFile = new CsvMapReader(new StringReader(str), new CsvPreference('"', ',', "\n"));
}

@Test
public void testAdvSkipColumnsRead() throws IOException {
	final Map<String, ? super Object> res = inFile.read(nameMapperPartial, PROCESSORS_PARTIAL); // read line and
	// check the values
	Assert.assertEquals("read elem from map", "Kla", res.get("firstname"));
	Assert.assertNull("skipped column", res.get("lastname"));
	Assert.assertEquals("read elem from map", "New York", res.get("town"));
	Assert.assertEquals("no keys", 2, res.size());
}

@Test
public void testAdvSkipColumnsRead_fail() throws IOException {
	final Map<String, ? super Object> res = inFile.read(nameMapperPartial, PROCESSORS_EMPTY); // read line and
	// check the values
	Assert.assertEquals("read elem from map", "Klaus", res.get("firstname"));
	Assert.assertNull("skipped column", res.get("lastname"));
	Assert.assertEquals("read elem from map", "New York", res.get("town"));
	Assert.assertEquals("no keys", 2, res.size());
}

/**
 * This example is being used on the web page and consequently must work! :-)
 * 
 * @throws IOException
 */
@Test
public void testEmptyLastLineWebExmaple() throws IOException {
	final StringReader file = new StringReader("name, birthdate,  phone,     town\r\n"
		+ "Bil,  30/05-1975, 5551684,   Nottingham\r\n" + "Kira, 01/04-2001, 5556621,   Sheffield\r\n");
	
	final ICsvMapReader mapReader = new CsvMapReader(file, CsvPreference.EXCEL_PREFERENCE);
	Map<String, ? super Object> map;
	Map<String, ? super Object> secondLastRead = null;
	
	// read the header to fetch column names (which are used as keys for the map)
	final String[] header = mapReader.getCSVHeader(true);
	
	while( (map = mapReader.read(header,
		new CellProcessor[] { null, new ParseDate("dd/MM-yyyy"), new ParseLong(), null })) != null ) {
		secondLastRead = map;
	}
	Assert.assertEquals("last read phone", 5556621L, secondLastRead.get("phone"));
}

@Test
public void testHeaderRead() throws IOException {
	final String[] header = inFile.getCSVHeader(true);
	Assert.assertEquals("First element in 'header' is the first column of the file", "Klaus", header[0]);
	Assert.assertEquals("Anderson", header[1]);
	Assert.assertEquals("Mauler Street 43", header[2]);
	Assert.assertEquals("4328", header[3]);
	Assert.assertEquals("New York", header[4]);
}

@Test(expected = SuperCSVException.class)
public void testReadInvalidMap() throws IOException {
	@SuppressWarnings("unused")
	final Map<String, ? super Object> res = inFile.read(invalidNameMapperPartial, PROCESSORS_EMPTY);
}

@Test
public void testSimplestRead() throws IOException {
	Map<String, String> res = inFile.read(nameMapper); // read line and check the values
	Assert.assertEquals("read elem from map", "Klaus", res.get("firstname"));
	Assert.assertEquals("read elem from map", "Anderson", res.get("lastname"));
	Assert.assertEquals("read elem from map", "Mauler Street 43", res.get("street"));
	Assert.assertEquals("read elem from map", "4328", res.get("zip"));
	Assert.assertEquals("read elem from map", "New York", res.get("town"));
	Assert.assertEquals("no keys", 5, res.size());
	
	res = inFile.read(nameMapper); // read line and check the values
	Assert.assertEquals("read elem from map", "Moby", res.get("firstname"));
	Assert.assertEquals("read elem from map", "Duck", res.get("lastname"));
	Assert.assertEquals("read elem from map", "Sesam str", res.get("street"));
	Assert.assertEquals("read elem from map", "", res.get("zip"));
	Assert.assertEquals("read elem from map", "", res.get("town"));
	Assert.assertEquals("no keys", 5, res.size());
}

@Test
public void testSimplestSkipColumnsRead() throws IOException {
	final Map<String, String> res = inFile.read(nameMapperPartial); // read line and check the values
	Assert.assertEquals("read elem from map", "Klaus", res.get("firstname"));
	Assert.assertEquals("skipped column", null, res.get("lastname"));
	Assert.assertEquals("read elem from map", "New York", res.get("town"));
	Assert.assertEquals("no keys", 2, res.size());
}

@Test
public void testWithProcessorsRead() throws IOException {
	Map<String, ? super Object> res = inFile.read(nameMapper, PROCESSORS); // read line and check the values
	Assert.assertEquals("read elem from map", "Klaus", res.get("firstname"));
	Assert.assertEquals("read elem from map", "Anderson", res.get("lastname"));
	Assert.assertEquals("read elem from map", "Mauler Street 43", res.get("street"));
	Assert.assertEquals("read elem from map", 4328, res.get("zip"));
	Assert.assertEquals("read elem from map", "New York", res.get("town"));
	Assert.assertEquals("no keys", 5, res.size());
	
	res = inFile.read(nameMapper, PROCESSORS); // read line and check the values
	Assert.assertEquals("read elem from map", "Moby", res.get("firstname"));
	Assert.assertEquals("read elem from map", "Duck", res.get("lastname"));
	Assert.assertEquals("read elem from map", "Sesam str", res.get("street"));
	Assert.assertEquals("read elem from map", null, res.get("zip"));
	Assert.assertEquals("read elem from map", null, res.get("town"));
	Assert.assertEquals("no keys", 5, res.size());
}

@Test(expected = SuperCSVException.class)
public void testWithProcessorsRead_fail() throws IOException {
	@SuppressWarnings("unused")
	final Map<String, ? super Object> res = inFile.read(nameMapper, new CellProcessor[] { null });
}
}
