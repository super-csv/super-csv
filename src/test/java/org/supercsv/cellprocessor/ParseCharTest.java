package org.supercsv.cellprocessor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.util.CSVContext;

/**
 * @author Kasper B. Graversen
 */
public class ParseCharTest {
public static final String VALID_CHAR_AS_STRING = "C";
public static final char VALID_CHAR = 'C';
CSVContext context;

@Before
public void setUp() throws Exception {
	context = new CSVContext(1, 2);
}

@Test(expected = SuperCSVException.class)
public void should_handle_long_strings_as_invalid() {
	new ParseChar().execute("CC", context);
}

@Test(expected = SuperCSVException.class)
public void should_only_accept_strings_and_char() {
	new ParseChar().execute(12, context);
}

@Test
public void test_valid_input_char() {
	Assert.assertEquals(VALID_CHAR, new ParseChar().execute(VALID_CHAR, context));
}

@Test
public void test_valid_input_string() {
	Assert.assertEquals(VALID_CHAR, new ParseChar().execute(VALID_CHAR_AS_STRING, context));
}
}
