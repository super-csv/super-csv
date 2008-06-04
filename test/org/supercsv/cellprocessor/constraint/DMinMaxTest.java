package org.supercsv.cellprocessor.constraint;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.supercsv.TestConstants;
import org.supercsv.exception.SuperCSVException;
import org.supercsv.mock.ComparerCellProcessor;
import org.supercsv.util.CSVContext;

public class DMinMaxTest {
private static final int MIN = 0;
private static final int MAX = 200;
public static final double IN_RANGE = 7.7;
public static final double OUTSIDE_RANGE = 777.7;

@Test(expected = SuperCSVException.class)
public void should_fail_on_out_of_range_input() {
	assertThat((Double) new DMinMax(MIN, MAX).execute(OUTSIDE_RANGE, TestConstants.ANONYMOUS_CSVCONTEXT), null);
}

@Test
public void should_have_chaining_working() {
	assertThat((Boolean) new DMinMax(MIN, MAX, new ComparerCellProcessor(IN_RANGE)).execute(IN_RANGE, new CSVContext(0,
		0)), is(true));
}

@Test(expected = SuperCSVException.class)
public void shouldFailMaxLessThanMin() {
	new DMinMax(MAX, MIN);
}

@Test
public void shouldWork() {
	assertThat((Double) new DMinMax(MIN, MAX).execute(IN_RANGE, TestConstants.ANONYMOUS_CSVCONTEXT), is(IN_RANGE));
	assertThat((Double) new DMinMax(MIN, MAX).execute("" + IN_RANGE, TestConstants.ANONYMOUS_CSVCONTEXT), is(IN_RANGE));
}

@Test
public void shouldWorkWithFineArguments() {
	new DMinMax(MIN, MIN);
	new DMinMax(MIN, MAX);
	
}

@Test(expected = SuperCSVException.class)
public void should_not_allow_non_number() {
	new DMinMax(MIN, MAX).execute("non number", TestConstants.ANONYMOUS_CSVCONTEXT);
}
}
