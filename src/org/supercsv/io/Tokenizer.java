package org.supercsv.io;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.List;

import org.supercsv.prefs.CsvPreference;

/**
 * The tokenizer is an internal mechanism to the csv parser
 * 
 * @author Kasper B. Graversen
 */
public class Tokenizer implements ITokenizer {
	/** states of the parser */
	protected enum PARSERSTATE {
		NORMAL, // normal text
		QUOTESCOPE, // inside quote scope (e.g.: " here "
	}

	CsvPreference preferences;
	LineNumberReader lnr;

	StringBuilder sb = null;

	public Tokenizer(final Reader stream, final CsvPreference preference) {
		this.preferences = preference;
		lnr = new LineNumberReader(stream);
		sb = new StringBuilder(500);
	}

	/**
	 * {@inheritDoc}
	 */
	public void close() throws IOException {
		lnr.close();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getLineNumber() {
		return lnr.getLineNumber();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean readStringList(final List<String> result) throws IOException {
		result.clear();
		PARSERSTATE state = PARSERSTATE.NORMAL; // start out in normal mode

		// fast access to preferences
		final int quote = preferences.getQuoteChar();
		final int delim = preferences.getDelimiterChar();

		String line;
		// read non-empty lines only
		do {
			line = lnr.readLine();
			if(line == null) return false; // EOF
		} while(line.length() == 0); // skip zero len lines

		// start parsing
		line = line + "\n"; // add a newline to determine end of line (making
		// parsing easier)
		sb = new StringBuilder();

		// proccess the line (and maybe more lines of the file)
		int p = 0; // the pos of the cursor on the line

		int startToken = 0; // represents the position in each cell of the first character. Is used to detect if a
		// quote char character of the cell (in which case it must always change the parser state

		int linenoQuoteState = -1; // the line number of the file where the

		while(true) {
			// relies on p being incremented at least at the end of the while
			final char c = line.charAt(p);

			// react to char c depending on the state we are in
			switch(state) {

				case NORMAL:
					// if(log.isDebugEnabled()) log.debug("normal " + p);

					if(c == delim) {
						result.add(sb.toString().trim()); // save token
						sb.delete(0, sb.length()); // reset the stringbuilder
						break; // read more
					}
					else if(c == ' ') { // trim starting spaces (trailing spaces
						// are removed using the String.trim()
						if(sb.length() > 0) // add only space if it is not the
							// first on the line
							sb.append(c);
						break; // read more
					}
					else if(c == '\n') {
						// save token
						result.add(sb.toString().trim());
						return true; // we've read a line
					}
					else if(c == quote && sb.length() == 0) { // quote char as first character in cell => state change
						state = PARSERSTATE.QUOTESCOPE;
						linenoQuoteState = getLineNumber();
						break; // read more
					}
					else if(c == quote && line.charAt(p + 1) == quote) { // an escaped quote,
						sb.append(c); // add and skip the first quote (end of
						// switch will skip the next quote
						p++;
						break; // read more
					}
					else if(c == quote && line.charAt(p + 1) != quote) { // a single quote, change state and don't
						// append
						state = PARSERSTATE.QUOTESCOPE;
						// update variable in order to do debug statements
						linenoQuoteState = getLineNumber();
						break; // read more
					}
					else { // if just a normal character
						sb.append(c); // add the char
					}

					break;

				// for each situation above, repeat now in the quote scope
				case QUOTESCOPE:
					// if(log.isDebugEnabled()) log.debug("quote: " + p);

					if(c == delim) {
						// delimiter does not count as delimiter in quote scope
						sb.append(c);
						break; // read more
					}
					else if(c == '\n') { // newline does not count as newline in
						// quote scope
						sb.append('\n');
						// parse the next line of the file
						p = -1; // reset delta to point to start of new line (set to
						// -1 as it will be incremented to 0 at the end of
						// the switch)
						line = lnr.readLine();
						if(line == null) throw new IOException("File ended unexpectedly while reading a quoted cell starting on line: " + linenoQuoteState);
						line = line + '\n'; // add \n to make parsing easy
						break; // read more
					}
					else if(c == quote && line.charAt(p + 1) == quote) { // an
						// escaped
						// quote,
						sb.append(c); // add and skip the first quote (end of
						// switch will skip the next quote
						p++;
						break; // read more
					}
					else if(line.charAt(p) == quote && line.charAt(p + 1) != quote) {
						// a single quote, only change state
						state = PARSERSTATE.NORMAL;
						break; // read more
					}
					else { // if just a normal character
						sb.append(c); // add the char
					}
					break;

			} // switch

			p++; // read next char of the line
		}
	}
}