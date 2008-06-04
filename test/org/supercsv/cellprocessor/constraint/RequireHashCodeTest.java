package org.supercsv.cellprocessor.constraint;

import org.junit.Test;
import org.supercsv.TestConstants;
import org.supercsv.exception.SuperCSVException;

public class RequireHashCodeTest {
final String STR1 = "foo", STR2 = "bar";

@Test
public void should_succeed_on_valid() {
	final RequireHashCode p = new RequireHashCode(STR1.hashCode());
	p.execute(STR1, TestConstants.ANONYMOUS_CSVCONTEXT);
}

@Test
public void should_succeed_on_valid_input_in_multiple_set() {
	final RequireHashCode p = new RequireHashCode(STR1.hashCode(), STR2.hashCode());
	p.execute(STR1, TestConstants.ANONYMOUS_CSVCONTEXT);
	p.execute(STR2, TestConstants.ANONYMOUS_CSVCONTEXT);
}

@Test
public void should_succeed_on_valid_input_in_multiple_set_arr() {
	final RequireHashCode p = new RequireHashCode(new int[] { STR1.hashCode(), STR2.hashCode() });
	p.execute(STR1, TestConstants.ANONYMOUS_CSVCONTEXT);
	p.execute(STR2, TestConstants.ANONYMOUS_CSVCONTEXT);
}

@Test(expected = SuperCSVException.class)
public void should_fail_on_wrong_input() {
	final RequireHashCode p = new RequireHashCode(STR1.hashCode());
	p.execute(STR2, TestConstants.ANONYMOUS_CSVCONTEXT);
}

@Test(expected = SuperCSVException.class)
public void should_fail_on_invalid_input() {
	RequireHashCode p = new RequireHashCode(STR1.hashCode());
	p.execute(null, TestConstants.ANONYMOUS_CSVCONTEXT);
}
}
