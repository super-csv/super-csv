package org.supercsv.cellprocessor;

import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 * This processor returns null if it meets the empty String in a column and does not call subsequent processors in its
 * chain. It is a simple customization of the <tt>MagicToken</tt>. If you need to return different values than null,
 * use that class instead.
 * 
 * @author Kasper B. Graversen
 */
public class Optional extends Token {
/**
 * Upon meeting the empty string in a column, return null
 */
public Optional() {
	super("", null);
}

/**
 * Upon meeting the empty string in a column, return null
 */
public Optional(final CellProcessor next) {
	super("", null, next);
}
}
