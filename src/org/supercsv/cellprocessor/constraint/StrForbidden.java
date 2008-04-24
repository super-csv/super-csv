/**
 *
 */
package org.supercsv.cellprocessor.constraint;

import java.util.List;

import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 * Convert to string and ensure the input string is not present (even as a substring) in a set of specified strings.
 * Such constraint may be handy when reading/writting e.g. filenames and wanting to ensure no filename contains e.g.
 * ":", "/", ... Use forbidSubstring instead (its a better name)
 * 
 * @deprecated
 * @author Kasper B. Graversen
 */
@Deprecated
public class StrForbidden extends ForbidSubStr {
public StrForbidden(final List<String> forbiddenStrings) {
	this(forbiddenStrings.toArray(new String[0]));
}

public StrForbidden(final List<String> forbiddenStrings, final CellProcessor next) {
	this(forbiddenStrings.toArray(new String[0]), next);
}

public StrForbidden(final String... forbiddenStrings) {
	super(forbiddenStrings);
}

public StrForbidden(final String forbiddenString, final CellProcessor next) {
	this(new String[] { forbiddenString }, next);
}

public StrForbidden(final String[] forbiddenStrings, final CellProcessor next) {
	super(forbiddenStrings);
}
}
