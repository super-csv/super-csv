package org.supercsv.cellprocessor.constraint;

import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 * This processor converts the input to a string, and enforces a requirement that the input's hash function returns any
 * of a given set of hashcodes (specified as one or one in a set of hash codes). Lookup time is O(1).
 * <p>
 * This constraint is a very efficient way of ensuring constant expressions are present in certain columns of the CSV
 * file, such as "BOSS", "EMPLOYEE", or when a column denote an action to be taken for the input line such as "D"
 * (delete), "I" (insert), ...
 * <P>
 * It corresponds to the {@link Required} class, but is renamed to the new name clearer conveying the semantics.
 * 
 * @since 1.50
 * @author Kasper B. Graversen
 */
public class RequireHashCode extends Required {

public RequireHashCode(final int... requiredHashcodes) {
	super(requiredHashcodes);
}

public RequireHashCode(final int requiredHashcode, final CellProcessor next) {
	this(new int[] { requiredHashcode }, next);
}

public RequireHashCode(final int[] requiredHashcodes, final CellProcessor next) {
	super(requiredHashcodes, next);
}

}
