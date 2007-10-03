package org.test.supercsv.io;

import java.io.StringReader;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.prefs.CsvPreference;
import org.test.supercsv.mock.PersonBean;

public class CsvBeanReaderTest {
	static final String fileWithHeader = "firstname, password, street, zip, town\n" + "Klaus,     Anderson,   Mauler Street 43,   4328,           New York\n";
	CsvBeanReader inFile = null;
	String[] nameMapper = { "firstname", "password", "street", "zip", "town" };
	String[] partialNameMapper = { "firstname", "password", "street", null, null };

	final CellProcessor[] processors = new CellProcessor[] { null, null, null, new Optional(new ParseInt()), null };

	@Before
	public void setUp() throws Exception {

		final String fileWithoutHeader = "Klaus,     Anderson,   Mauler Street 43,   4328,           New York\n" + "Moby,      Duck,       Sesam str,              ,         \n"; // missing
		// parts
		// of
		// the
		// address
		inFile = new CsvBeanReader(new StringReader(fileWithoutHeader), new CsvPreference('"', ',', "\n"));
	}

	@Test
	public void testReadOnlyNameMapping() throws Exception {
		PersonBean res;
		// final StringBuilder errorLog = new StringBuilder();
		res = inFile.read(PersonBean.class, partialNameMapper); // read line and
		// check the
		// values
		Assert.assertEquals("read elem ", "Klaus", res.getFirstname());
		Assert.assertEquals("read elem ", "Anderson", res.getPassword());
		Assert.assertEquals("read elem ", "Mauler Street 43", res.getStreet());
		Assert.assertEquals("read elem from map", 0, res.getZip());
		Assert.assertEquals("read elem from map", null, res.getTown());
		// read line 2
		Assert.assertNotNull(inFile.read(PersonBean.class, partialNameMapper));
		// no line 3
		Assert.assertNull(inFile.read(PersonBean.class, partialNameMapper));
	}

	@Test(expected = SuperCSVException.class)
	public void testReadUnprocessed_file_with_header_fail_due_to_header_not_having_an_int() throws Exception {
		inFile = new CsvBeanReader(new StringReader(fileWithHeader), CsvPreference.EXCEL_PREFERENCE);
		inFile.read(PersonBean.class, nameMapper); // try, wrongly, to skip line as its invalid (its the header) to get
		// to the lines
	}

	@Test
	public void testSimplestRead() throws Exception {
		PersonBean res;
		// final StringBuilder errorLog = new StringBuilder();
		res = inFile.read(PersonBean.class, nameMapper, processors); // read line and check the values
		Assert.assertEquals("read elem ", "Klaus", res.getFirstname());
		Assert.assertEquals("read elem ", "Anderson", res.getPassword());
		Assert.assertEquals("read elem ", "Mauler Street 43", res.getStreet());
		Assert.assertEquals("read elem ", 4328, res.getZip());
		Assert.assertEquals("read elem ", "New York", res.getTown());
		// Assert.assertEquals("no error log", 0, errorLog.length());

		res = inFile.read(PersonBean.class, partialNameMapper, processors); // read line and check the values
		Assert.assertEquals("read elem from map", "Moby", res.getFirstname());
		Assert.assertEquals("read elem from map", "Duck", res.getPassword());
		Assert.assertEquals("read elem from map", "Sesam str", res.getStreet());
		Assert.assertEquals("read elem from map", 0, res.getZip());
		Assert.assertEquals("read elem from map", null, res.getTown());

		// no line 3
		Assert.assertNull(inFile.read(PersonBean.class, partialNameMapper, processors));
	}
}
