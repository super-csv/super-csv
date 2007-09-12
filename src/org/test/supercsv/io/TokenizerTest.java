package org.test.supercsv.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.io.Tokenizer;
import org.supercsv.prefs.CsvPreference;

public class TokenizerTest {
	Tokenizer tokenizer;

	@Before
	public void setUp() throws Exception {
		final String fileWithHeader = "firstname , lastname , 	street , 			zip , 		town\n"
				+ "Klaus,     Anderson ,   Mauler Street 43,   4328,       New York\n";
		tokenizer = new Tokenizer(new StringReader(fileWithHeader), CsvPreference.EXCEL_PREFERENCE);
	}

	@Test
	public void testTrailingSpacesRemoval() throws Exception {
		final List<String> result = new ArrayList<String>();
		assertTrue(tokenizer.readStringList(result));
		assertEquals("pre-post-fix whitespace trimming works", "firstname", result.get(0));
		assertEquals("pre-post-fix whitespace trimming works", "lastname", result.get(1));
		assertEquals("pre-post-fix whitespace trimming works", "street", result.get(2));
		assertEquals("pre-post-fix whitespace trimming works", "zip", result.get(3));
		assertEquals("pre-post-fix whitespace trimming works", "town", result.get(4));
	}
}