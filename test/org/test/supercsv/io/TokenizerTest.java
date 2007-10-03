package org.test.supercsv.io;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.io.Tokenizer;
import org.supercsv.prefs.CsvPreference;

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
	public void inputOneRow_2_quote_inside_quote() throws Exception {
		String input = "\"\"\"hello\"\"\"";
		tokenizer = new Tokenizer(new StringReader(input), CsvPreference.EXCEL_PREFERENCE);
		tokenizer.readStringList(result);
		assertThat("only two must yield empty entry", result.get(0), is("\"hello\""));
	}

	@Test
	public void inputOneRow_2_quote_outside_quote() throws Exception {
		String input = "  yo \"\"hello\"\"  "; // must not have " at start of line.. as that will not denote "
		tokenizer = new Tokenizer(new StringReader(input), CsvPreference.EXCEL_PREFERENCE);
		tokenizer.readStringList(result);
		assertThat(result.get(0), is("yo \"hello\""));
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
	public void inputOneRow_should_not_strim_spaces_before_and_after() throws Exception {
		String input = "\"    hello    \" , \"   you  \" , \" there \"";
		tokenizer = new Tokenizer(new StringReader(input), CsvPreference.EXCEL_PREFERENCE);
		tokenizer.readStringList(result);
		assertThat(result.get(0), is("    hello    "));
		assertThat(result.get(1), is("   you  "));
		assertThat(result.get(2), is(" there "));
	}

	@Test
	public void inputOneRow_should_not_trim_spaces_between_words() throws Exception {
		String input = "    hello  you  ,   there  on   the  ,  corner ";
		tokenizer = new Tokenizer(new StringReader(input), CsvPreference.EXCEL_PREFERENCE);
		tokenizer.readStringList(result);
		assertThat(result.get(0), is("hello  you"));
		assertThat(result.get(1), is("there  on   the"));
		assertThat(result.get(2), is("corner"));
	}

	@Test
	public void inputOneRow_should_not_trim_tabs_before_and_after() throws Exception {
		String input = " \thello\t ,\tyou\t,\tthere\t";
		tokenizer = new Tokenizer(new StringReader(input), CsvPreference.EXCEL_PREFERENCE);
		tokenizer.readStringList(result);
		assertThat(result.get(0), is("\thello\t"));
		assertThat(result.get(1), is("\tyou\t"));
		assertThat(result.get(2), is("\tthere\t"));
	}

	@Test
	public void inputOneRow_should_trim_spaces_before_and_after() throws Exception {
		String input = "    hello    ,   you  ,  there  ";
		tokenizer = new Tokenizer(new StringReader(input), CsvPreference.EXCEL_PREFERENCE);
		tokenizer.readStringList(result);
		assertThat(result.get(0), is("hello"));
		assertThat(result.get(1), is("you"));
		assertThat(result.get(2), is("there"));
	}

	@Test
	public void inputOneRow_value() throws Exception {
		String input = "k";
		tokenizer = new Tokenizer(new StringReader(input), CsvPreference.EXCEL_PREFERENCE);
		tokenizer.readStringList(result);
		assertThat(result.size(), is(1));
		assertThat(result.get(0), is("k"));
	}

	@Test(expected = IOException.class)
	public void inputOneRow_value_missing_end_quote() throws Exception {
		String input = "\"missing";
		tokenizer = new Tokenizer(new StringReader(input), CsvPreference.EXCEL_PREFERENCE);
		tokenizer.readStringList(result);
		assertThat(result.size(), is(1));
		assertThat(result.get(0), is("m\nn"));
	}

	@Test
	public void inputOneRow_value_newline() throws Exception {
		String input = "\"m\nn\"";
		tokenizer = new Tokenizer(new StringReader(input), CsvPreference.EXCEL_PREFERENCE);
		tokenizer.readStringList(result);
		assertThat(result.size(), is(1));
		assertThat(result.get(0), is("m\nn"));
	}

	@Before
	public void setUp() throws Exception {
		result = new ArrayList<String>();
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