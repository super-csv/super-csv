package org.test.supercsv.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.io.Tokenizer;
import org.supercsv.prefs.CsvPreference;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TokenizerTest {
	Tokenizer tokenizer;
	List<String> result;

	@Test
	public void headerFile_emptyValue() throws Exception {
		String input = "header1\n ";
		tokenizer = new Tokenizer(new StringReader(input), CsvPreference.EXCEL_PREFERENCE);
		tokenizer.readStringList(result); // skip header
		tokenizer.readStringList(result);
		assertThat(result.size(), is(1));
		assertThat(result.get(0), is(""));
	}

	@Test
	public void inputOneRow_2_quote() throws Exception {
		String input = "\"\"";
		tokenizer = new Tokenizer(new StringReader(input), CsvPreference.EXCEL_PREFERENCE);
		tokenizer.readStringList(result);
		assertThat(result.size(), is(1));
		assertThat("only two must yield empty entry", result.get(0), is(""));
	}

	@Test
	public void inputOneRow_4_quote() throws Exception {
		String input = "\"\"\"\"";
		tokenizer = new Tokenizer(new StringReader(input), CsvPreference.EXCEL_PREFERENCE);
		tokenizer.readStringList(result);
		assertThat(result.size(), is(1));
		assertThat("4 quotes is 2 for start+end and 2 for an escaped quote", result.get(0), is("\""));
	}

	@Test
	public void inputOneRow_empty() throws Exception {
		String input = "";
		tokenizer = new Tokenizer(new StringReader(input), CsvPreference.EXCEL_PREFERENCE);
		tokenizer.readStringList(result);
		assertThat(result.size(), is(0));
	}

	@Test
	public void inputOneRow_value() throws Exception {
		String input = "k";
		tokenizer = new Tokenizer(new StringReader(input), CsvPreference.EXCEL_PREFERENCE);
		tokenizer.readStringList(result);
		assertThat(result.size(), is(1));
		assertThat(result.get(0), is("k"));
	}

	@Before
	public void setUp() throws Exception {
		result = new ArrayList<String>();
	}

	@Test
	public void test() throws Exception {

	}

	@Test
	public void testTrailingSpacesRemoval() throws Exception {
		final String fileWithHeader = "firstname , lastname , 	street , 			zip , 		town\n" + "Klaus,     Anderson ,   Mauler Street 43,   4328,       New York\n";
		tokenizer = new Tokenizer(new StringReader(fileWithHeader), CsvPreference.EXCEL_PREFERENCE);

		assertTrue(tokenizer.readStringList(result));
		assertEquals("pre-post-fix whitespace trimming works", "firstname", result.get(0));
		assertEquals("pre-post-fix whitespace trimming works", "lastname", result.get(1));
		assertEquals("pre-post-fix whitespace trimming works", "street", result.get(2));
		assertEquals("pre-post-fix whitespace trimming works", "zip", result.get(3));
		assertEquals("pre-post-fix whitespace trimming works", "town", result.get(4));
	}

	@Test
	public void valueInHeaderFile() throws Exception {
		String input = "header1\nvalue";
		tokenizer = new Tokenizer(new StringReader(input), CsvPreference.EXCEL_PREFERENCE);
		tokenizer.readStringList(result); // skip header
		tokenizer.readStringList(result);
		assertThat(result.size(), is(1));
		assertThat(result.get(0), is("value"));
	}
}