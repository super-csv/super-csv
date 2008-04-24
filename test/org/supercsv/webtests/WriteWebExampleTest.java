package org.supercsv.webtests;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

public class WriteWebExampleTest {
StringWriter outFile;
ICsvMapWriter writer;

@Before
public void setUp() {
	outFile = new StringWriter();
	writer = new CsvMapWriter(outFile, CsvPreference.EXCEL_PREFERENCE);
}

@After
public void tearDown() throws IOException {
	outFile.close();
}

@Test
public void testWriting() throws IOException {
	final String[] header = new String[] { "name", "city", "zip" };
	final HashMap<String, ? super Object> data1 = new HashMap<String, Object>();
	data1.put(header[0], "Karl");
	data1.put(header[1], "Tent city");
	data1.put(header[2], 5565);
	final HashMap<String, ? super Object> data2 = new HashMap<String, Object>();
	data2.put(header[0], "Banjo");
	data2.put(header[1], "River side");
	data2.put(header[2], 5551);
	writer.writeHeader(header);
	writer.write(data1, header);
	writer.write(data2, header);
	writer.close();
	Assert.assertEquals("name,city,zip\nKarl,Tent city,5565\nBanjo,River side,5551\n", outFile.toString());
}
}
