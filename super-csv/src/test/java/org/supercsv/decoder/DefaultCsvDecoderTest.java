/*
 * Copyright 2007 Kasper B. Graversen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.supercsv.decoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.supercsv.util.EmptyColumnParsing;
import org.supercsv.prefs.CsvPreference;
import static org.supercsv.prefs.CsvPreference.EXCEL_PREFERENCE;

public class DefaultCsvDecoderTest {

    private static final CsvPreference SPACES_NEED_QUOTES_PREFERENCE = new CsvPreference.Builder(EXCEL_PREFERENCE)
            .surroundingSpacesNeedQuotes(true).build();
    private static final CsvPreference PARSE_EMPTY_COLUMNS_AS_EMPTY_STRING_PREFERENCE = new CsvPreference.Builder(EXCEL_PREFERENCE)
            .setEmptyColumnParsing(EmptyColumnParsing.ParseEmptyColumnsAsEmptyString).build();

    private CsvDecoder decoder;
    private List<String> columns;

    @Before
    public void setUp() {
    	decoder = new DefaultCsvDecoder();
    }

    /**
     * Tests the initDecoder() method with a null CsvPreference (should throw an Exception).
     */
    @Test(expected = NullPointerException.class)
	public void initDecoderWithNullPreference() {
    	decoder.initDecoder(null);
    }

    /**
     * Tests the decode() method with no data.
     */
    @Test
    public void testDecodeWithNoData() throws Exception {
        final String input = "";
        decoder.initDecoder(EXCEL_PREFERENCE);
        columns = decoder.decode(input, false);
        assertTrue(columns.isEmpty());
    }

    /**
     * Tests the decode() method a quoted section has text surrounding it. This is not technically valid CSV, but
     * the decoder is lenient enough to allow it (it will just unescape the quoted section).
     */
    @Test
    public void testQuotedFieldWithSurroundingText() throws Exception {

        final String input = "surrounding \"quoted\" text";
        decoder.initDecoder(EXCEL_PREFERENCE);
        columns = decoder.decode(input, false);

        assertTrue(columns.size() == 1);
        assertEquals("surrounding quoted text", columns.get(0));

        // same result when surrounding spaces require quotes
	    decoder.initDecoder(SPACES_NEED_QUOTES_PREFERENCE);
        columns = decoder.decode(input, false);
        assertTrue(columns.size() == 1);
        assertEquals("surrounding quoted text", columns.get(0));
    }

    /**
     * Tests the decode() method when a quoted section with text after it. This is not technically valid CSV, but
     * the decoder is lenient enough to allow it (it will just unescape the quoted section).
     */
    @Test
    public void testQuotedFieldWithTextAfter() throws Exception {

        // illegal char after quoted section
	    final String input = "\"quoted on 2 lines\nand afterward some\" text";
        final String input1 = "\"quoted on 2 lines";
        final String input2 = "and afterward some\" text";
        decoder.initDecoder(EXCEL_PREFERENCE);
        columns = new ArrayList<String>();
        columns.addAll(decoder.decode(input1, false));
        columns.addAll(decoder.decode(input2, true));
        assertEquals(1, columns.size());
        assertEquals("quoted on 2 lines\nand afterward some text", columns.get(0));


        // should have exactly the same result when surrounding spaces need quotes
	    decoder.initDecoder(SPACES_NEED_QUOTES_PREFERENCE);
	    columns = new ArrayList<String>();
	    columns.addAll(decoder.decode(input1, false));
	    columns.addAll(decoder.decode(input2, true));
        assertEquals(1, columns.size());
        assertEquals("quoted on 2 lines\nand afterward some text", columns.get(0));
    }

    /**
     * Tests the decode() method with a single quoted newline.
     */
    @Test
    public void testQuotedNewline() throws Exception {

        final String input = "\"\n\"";
        final String input1 = "\"";
        final String input2 = "\"";

	    decoder.initDecoder(EXCEL_PREFERENCE);
	    columns = new ArrayList<String>();
	    columns.addAll(decoder.decode(input1, false));
	    columns.addAll(decoder.decode(input2, true));
        assertTrue(columns.size() == 1);
        assertEquals("\n", columns.get(0));

        // same input when surrounding spaces require quotes (results should be identical)
	    decoder.initDecoder(SPACES_NEED_QUOTES_PREFERENCE);
	    columns = new ArrayList<String>();
	    columns.addAll(decoder.decode(input1, false));
	    columns.addAll(decoder.decode(input2, true));
        assertTrue(columns.size() == 1);
        assertEquals("\n", columns.get(0));
    }

    /**
     * Tests the decode() method with a variety of quoted newlines.
     */
    @Test
    public void testQuotedNewlines() throws Exception {

        final String input = "\"one line\",\"two\nlines\",\"three\nlines\n!\"";
        final String input1 = "\"one line\",\"two";
        final String input2 = "lines\",\"three";
        final String input3 = "lines";
        final String input4 = "!\"";
	    decoder.initDecoder(EXCEL_PREFERENCE);
	    columns = new ArrayList<String>();
	    columns.addAll(decoder.decode(input1, false));
	    columns.addAll(decoder.decode(input2, true));
	    columns.addAll(decoder.decode(input3, true));
	    columns.addAll(decoder.decode(input4, true));
        assertTrue(columns.size() == 3);
        assertEquals("one line", columns.get(0));
        assertEquals("two\nlines", columns.get(1));
        assertEquals("three\nlines\n!", columns.get(2));

        // same input when surrounding spaces require quotes (results should be identical)
	    decoder.initDecoder(SPACES_NEED_QUOTES_PREFERENCE);
	    columns = new ArrayList<String>();
	    columns.addAll(decoder.decode(input1, false));
	    columns.addAll(decoder.decode(input2, true));
	    columns.addAll(decoder.decode(input3, true));
	    columns.addAll(decoder.decode(input4, true));
        assertTrue(columns.size() == 3);
        assertEquals("one line", columns.get(0));
        assertEquals("two\nlines", columns.get(1));
        assertEquals("three\nlines\n!", columns.get(2));
    }

    /**
     * Tests the decode() method when a quoted field has consecutive newlines.
     */
    @Test
    public void testQuotedTextWithConsecutiveNewLines() throws Exception {

        // second field has consecutive newlines
        final String input = "one, \"multiline\n\n\ntext\"";
        final String input1 = "one, \"multiline";
        final String input2 = "";
        final String input3 = "";
        final String input4 = "text\"";
	    decoder.initDecoder(EXCEL_PREFERENCE);
	    columns = new ArrayList<String>();
	    columns.addAll(decoder.decode(input1, false));
	    columns.addAll(decoder.decode(input2, true));
	    columns.addAll(decoder.decode(input3, true));
	    columns.addAll(decoder.decode(input4, true));
        assertEquals(2, columns.size());
        assertEquals("one", columns.get(0));
        assertEquals(" multiline\n\n\ntext", columns.get(1));

        // should have exactly the same result when surrounding spaces need quotes
	    decoder.initDecoder(SPACES_NEED_QUOTES_PREFERENCE);
	    columns = new ArrayList<String>();
	    columns.addAll(decoder.decode(input1, false));
	    columns.addAll(decoder.decode(input2, true));
	    columns.addAll(decoder.decode(input3, true));
	    columns.addAll(decoder.decode(input4, true));
        assertTrue(columns.size() == 2);
        assertEquals("one", columns.get(0));
        assertEquals("multiline\n\n\ntext", columns.get(1));
    }

    /**
     * Tests the decode() method with a leading space before the first quoted field. This is not technically valid
     * CSV, but the decoder is lenient enough to allow it. The leading spaces will be trimmed off when surrounding
     * spaces require quotes, otherwise they will be part of the field.
     */
    @Test
    public void testQuotedFirstFieldWithLeadingSpace() throws Exception {

        // leading spaces should be preserved
        final String input = "  \"quoted with leading spaces\",two";
        decoder.initDecoder(EXCEL_PREFERENCE);
        columns = decoder.decode(input, false);
        assertTrue(columns.size() == 2);
        assertEquals("  quoted with leading spaces", columns.get(0));
        assertEquals("two", columns.get(1));

        // same input when surrounding spaces require quotes (leading spaces trimmed)
	    decoder.initDecoder(SPACES_NEED_QUOTES_PREFERENCE);
	    columns = decoder.decode(input, false);
        assertTrue(columns.size() == 2);
        assertEquals("quoted with leading spaces", columns.get(0));
        assertEquals("two", columns.get(1));
    }

    /**
     * Tests the decode() method with a leading space before the last quoted field. This is not technically valid
     * CSV, but the decoder is lenient enough to allow it. The leading spaces will be trimmed off when surrounding
     * spaces require quotes, otherwise they will be part of the field.
     */
    @Test
    public void testQuotedLastFieldWithLeadingSpace() throws Exception {

        // last field has a leading space before quote (should be preserved)
        final String input = "one,two,  \"quoted with leading spaces\"";
        decoder.initDecoder(EXCEL_PREFERENCE);
        columns = decoder.decode(input, false);
        assertTrue(columns.size() == 3);
        assertEquals("one", columns.get(0));
        assertEquals("two", columns.get(1));
        assertEquals("  quoted with leading spaces", columns.get(2));

        // leading space should be trimmed off
	    decoder.initDecoder(SPACES_NEED_QUOTES_PREFERENCE);
	    columns = decoder.decode(input, false);
        assertTrue(columns.size() == 3);
        assertEquals("one", columns.get(0));
        assertEquals("two", columns.get(1));
        assertEquals("quoted with leading spaces", columns.get(2));
    }

    /**
     * Tests the decode() method with a trailing space after the first quoted field. This is not technically valid
     * CSV, but the decoder is lenient enough to allow it. The trailing spaces will be trimmed off when surrounding
     * spaces require quotes, otherwise they will be part of the field.
     */
    @Test
    public void testQuotedFirstFieldWithTrailingSpace() throws Exception {

        // first field has a leading space before quote (should be preserved)
        final String input = "\"quoted with trailing spaces\"  ,two";
        decoder.initDecoder(EXCEL_PREFERENCE);
        columns = decoder.decode(input, false);
        assertTrue(columns.size() == 2);
        assertEquals("quoted with trailing spaces  ", columns.get(0));
        assertEquals("two", columns.get(1));

        // trailing spaces should be trimmed
	    decoder.initDecoder(SPACES_NEED_QUOTES_PREFERENCE);
	    columns = decoder.decode(input, false);
        assertTrue(columns.size() == 2);
        assertEquals("quoted with trailing spaces", columns.get(0));
        assertEquals("two", columns.get(1));
    }

    /**
     * Tests the decode() method with a trailing space after the last quoted field. This is not technically valid
     * CSV, but the decoder is lenient enough to allow it. The trailing spaces will be trimmed off when surrounding
     * spaces require quotes, otherwise they will be part of the field.
     */
    @Test
    public void testQuotedLastFieldWithTrailingSpace() throws Exception {

        // last field has a leading space before quote (should be preserved)
        final String input = "one,two,\"quoted with trailing spaces\"  ";
        decoder.initDecoder(EXCEL_PREFERENCE);
        columns = decoder.decode(input, false);
        assertTrue(columns.size() == 3);
        assertEquals("one", columns.get(0));
        assertEquals("two", columns.get(1));
        assertEquals("quoted with trailing spaces  ", columns.get(2));

        // trailing spaces should be trimmed off
	    decoder.initDecoder(SPACES_NEED_QUOTES_PREFERENCE);
	    columns = decoder.decode(input, false);
        assertTrue(columns.size() == 3);
        assertEquals("one", columns.get(0));
        assertEquals("two", columns.get(1));
        assertEquals("quoted with trailing spaces", columns.get(2));
    }

    /**
     * Tests the decode() method with a variety of quoted spaces.
     */
    @Test
    public void testQuotedSpaces() throws Exception {

        final String input = "\" one \",\"  two  \",\"   three   \"";
	    decoder.initDecoder(EXCEL_PREFERENCE);
	    columns = decoder.decode(input, false);
        assertTrue(columns.size() == 3);
        assertEquals(" one ", columns.get(0));
        assertEquals("  two  ", columns.get(1));
        assertEquals("   three   ", columns.get(2));

        // same input when surrounding spaces require quotes (results should be identical)
	    decoder.initDecoder(SPACES_NEED_QUOTES_PREFERENCE);
	    columns = decoder.decode(input, false);
        assertTrue(columns.size() == 3);
        assertEquals(" one ", columns.get(0));
        assertEquals("  two  ", columns.get(1));
        assertEquals("   three   ", columns.get(2));
    }

    /**
     * Tests the decode() method with a variety of unquoted spaces.
     */
    @Test
    public void testSpaces() throws Exception {

        final String input = " one ,  two  ,   three   ";
	    decoder.initDecoder(EXCEL_PREFERENCE);
	    columns = decoder.decode(input, false);
        assertTrue(columns.size() == 3);
        assertEquals(" one ", columns.get(0));
        assertEquals("  two  ", columns.get(1));
        assertEquals("   three   ", columns.get(2));

        // same input when surrounding spaces require quotes
	    decoder.initDecoder(SPACES_NEED_QUOTES_PREFERENCE);
	    columns = decoder.decode(input, false);
        assertTrue(columns.size() == 3);
        assertEquals("one", columns.get(0));
        assertEquals("two", columns.get(1));
        assertEquals("three", columns.get(2));
    }

    /**
     * Tests the decode() method with a variety of spaces and tabs.
     */
    @Test
    public void testSpacesAndTabs() throws Exception {

        // tabs should never be trimmed
        final String input = "\t, \tone\t ,  \ttwo\t  ,   \tthree\t   ";
	    decoder.initDecoder(EXCEL_PREFERENCE);
	    columns = decoder.decode(input, false);
        assertTrue(columns.size() == 4);
        assertEquals("\t", columns.get(0));
        assertEquals(" \tone\t ", columns.get(1));
        assertEquals("  \ttwo\t  ", columns.get(2));
        assertEquals("   \tthree\t   ", columns.get(3));

        // same input when surrounding spaces require quotes
	    decoder.initDecoder(SPACES_NEED_QUOTES_PREFERENCE);
	    columns = decoder.decode(input, false);
        assertTrue(columns.size() == 4);
        assertEquals("\t", columns.get(0));
        assertEquals("\tone\t", columns.get(1));
        assertEquals("\ttwo\t", columns.get(2));
        assertEquals("\tthree\t", columns.get(3));
    }

    /**
     * Tests the decode() method with spaces between words.
     */
    @Test
    public void testSpacesBetweenWords() throws Exception {

        final String input = " one partridge ,  two turtle doves  ,   three french hens   ";
	    decoder.initDecoder(EXCEL_PREFERENCE);
	    columns = decoder.decode(input, false);
        assertTrue(columns.size() == 3);
        assertEquals(" one partridge ", columns.get(0));
        assertEquals("  two turtle doves  ", columns.get(1));
        assertEquals("   three french hens   ", columns.get(2));

        // same input when surrounding spaces require quotes
	    decoder.initDecoder(SPACES_NEED_QUOTES_PREFERENCE);
	    columns = decoder.decode(input, false);
        assertTrue(columns.size() == 3);
        assertEquals("one partridge", columns.get(0));
        assertEquals("two turtle doves", columns.get(1));
        assertEquals("three french hens", columns.get(2));
    }


    /**
     * Tests that the decode() method reads a null column as null when preferences is ParseEmptyColumnsAsNull.
     */
    @Test
    public void testReadNullWithParseEmptyColumnsAsNull() throws Exception {

        final String input = ",";
        decoder.initDecoder(EXCEL_PREFERENCE);
        assertTrue(EXCEL_PREFERENCE.getEmptyColumnParsing().equals(EmptyColumnParsing.ParseEmptyColumnsAsNull));

        columns = decoder.decode(input, false);
        assertTrue(columns.size() == 2);
        assertEquals(null, columns.get(0));
        assertEquals(null, columns.get(1));
    }


    /**
     * Tests that the decode() method reads an empty string column as null when preferences is ParseEmptyColumnsAsNull.
     */
    @Test
    public void testReadEmptyStringsWithParseEmptyColumnsAsNull() throws Exception {

        final String input = "\"\",\"\"";
        decoder.initDecoder(EXCEL_PREFERENCE);
        assertTrue(EXCEL_PREFERENCE.getEmptyColumnParsing().equals(EmptyColumnParsing.ParseEmptyColumnsAsNull));

        columns = decoder.decode(input, false);
        assertTrue(columns.size() == 2);
        assertEquals(null, columns.get(0));
        assertEquals(null, columns.get(1));
    }

    /**
     * Tests that the decode() method reads a null column as null when preferences is ParseEmptyColumnsAsEmptyString.
     */
    @Test
    public void testReadNullWithParseEmptyColumnsAsEmptyString() throws Exception {

        final String input = ",";
        decoder.initDecoder(PARSE_EMPTY_COLUMNS_AS_EMPTY_STRING_PREFERENCE);
        assertTrue(PARSE_EMPTY_COLUMNS_AS_EMPTY_STRING_PREFERENCE.getEmptyColumnParsing().equals(EmptyColumnParsing.ParseEmptyColumnsAsEmptyString));

        columns = decoder.decode(input, false);
        assertTrue(columns.size() == 2);
        assertEquals(null, columns.get(0));
        assertEquals(null, columns.get(1));
    }

    /**
     * Tests that the decode() method reads an empty string column as empty string when preferences is ParseEmptyColumnsAsEmptyString.
     */
    @Test
    public void testReadEmptyStringsWithParseEmptyColumnsAsEmptyString() throws Exception {

        final String input = "\"\",\"\"";
        decoder.initDecoder(PARSE_EMPTY_COLUMNS_AS_EMPTY_STRING_PREFERENCE);
        assertTrue(PARSE_EMPTY_COLUMNS_AS_EMPTY_STRING_PREFERENCE.getEmptyColumnParsing().equals(EmptyColumnParsing.ParseEmptyColumnsAsEmptyString));

        columns = decoder.decode(input, false);
        assertTrue(columns.size() == 2);
        assertEquals("", columns.get(0));
        assertEquals("", columns.get(1));
    }

}




