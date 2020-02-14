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

import java.util.ArrayList;
import java.util.List;

import org.supercsv.exception.SuperCsvException;
import org.supercsv.util.EmptyColumnParsing;
import org.supercsv.prefs.CsvPreference;

/**
 * The default CsvDecoder implementation which parse line from CSV files to columns(List). If you want to define your
 * own implementation of {@link #decode(String, boolean)}, then consider writing your own Csv Decoder.
 *
 * @author Kasper B. Graversen
 * @author James Bassett
 * @author Pietro Aragona
 * @author Chen Guoping
 * @since 2.5.0 (adapted from Tokenizer)
 */
public class DefaultCsvDecoder implements CsvDecoder {

    private static final char NEWLINE = '\n';

    private static final char SPACE = ' ';

    // current column being decoded
    private final StringBuilder currentColumn = new StringBuilder();

    private char quoteChar;

    private int delimiterChar;

    private boolean surroundingSpacesNeedQuotes;

    private EmptyColumnParsing emptyColumnParsing;

    private char quoteEscapeChar;

    // process each character in the line, catering for surrounding quotes (QUOTE_MODE)
    private DecoderState state;

    /**
     * Enumeration of decoder states. QUOTE_MODE is activated between quotes.
     */
    private enum DecoderState {
        NORMAL, QUOTE_MODE;
    }

    /**
     * Constructs a new <tt>DefaultCsvDecoder</tt>.
     */
    public DefaultCsvDecoder() {
    }

    /**
     * {@inheritDoc}
     */
    public void initDecoder(final CsvPreference preferences) {
        if( preferences == null ) {
            throw new NullPointerException("preferences should not be null");
        }
        this.quoteChar = preferences.getQuoteChar();
        this.delimiterChar = preferences.getDelimiterChar();
        this.surroundingSpacesNeedQuotes = preferences.isSurroundingSpacesNeedQuotes();
        this.emptyColumnParsing = preferences.getEmptyColumnParsing();
        this.quoteEscapeChar = preferences.getQuoteEscapeChar();
        this.state = DecoderState.NORMAL;
    }

    /**
     * {@inheritDoc}
     */
    public List<String> decode(String line, boolean multi) {
        List<String> columnsOnThisLine = new ArrayList<String>();
        if( !multi ) {
            // clear the currentColumn and state to parse start with this line
            currentColumn.setLength(0);
            state = DecoderState.NORMAL;
            if( line == "" ) {
                return columnsOnThisLine;
            }
        } else {
            if( line == "" ) {
                currentColumn.append(NEWLINE);
                return columnsOnThisLine;
            }
        }

        int potentialSpaces = 0; // keep track of spaces (so leading/trailing space can be removed if required)
        int charIndex = 0;
        while( true ) {
            boolean endOfLineReached = charIndex == line.length();

            if( endOfLineReached )
            {
                if( DecoderState.NORMAL.equals(state) ) {
                    /*
                     * Newline. Add any required spaces (if surrounding spaces don't need quotes) and return (we've read
                     * a line!).
                     */
                    if( !surroundingSpacesNeedQuotes ) {
                        appendSpaces(currentColumn, potentialSpaces);
                    }
                    addColumn(columnsOnThisLine, line, charIndex);
                    currentColumn.setLength(0);
                    return columnsOnThisLine;
                }
                else
                {
                    /*
                     * Newline. Doesn't count as newline while in QUOTESCOPE. Add the newline char. Wait for the next
                     * call(s) to decode.
                     */
                    currentColumn.append(NEWLINE);
                    return columnsOnThisLine;
                }
            }

            final char c = line.charAt(charIndex);

            if( DecoderState.NORMAL.equals(state) ) {

                /*
                 * NORMAL mode (not within quotes).
                 */

                if( c == delimiterChar) {
                    /*
                     * Delimiter. Save the column (trim trailing space if required) then continue to next character.
                     */
                    if( !surroundingSpacesNeedQuotes ) {
                        appendSpaces(currentColumn, potentialSpaces);
                    }
                    addColumn(columnsOnThisLine, line, charIndex);
                    potentialSpaces = 0;
                    currentColumn.setLength(0);

                } else if( c == SPACE ) {
                    /*
                     * Space. Remember it, then continue to next character.
                     */
                    potentialSpaces++;

                }
                else if( c == quoteChar ) {
                    /*
                     * A single quote ("). Update to QUOTESCOPE (but don't save quote), then continue to next character.
                     */
                    state = DecoderState.QUOTE_MODE;

                    // cater for spaces before a quoted section (be lenient!)
                    if( !surroundingSpacesNeedQuotes || currentColumn.length() > 0 ) {
                        appendSpaces(currentColumn, potentialSpaces);
                    }
                    potentialSpaces = 0;

                } else {
                    /*
                     * Just a normal character. Add any required spaces (but trim any leading spaces if surrounding
                     * spaces need quotes), add the character, then continue to next character.
                     */
                    if( !surroundingSpacesNeedQuotes || currentColumn.length() > 0 ) {
                        appendSpaces(currentColumn, potentialSpaces);
                    }

                    potentialSpaces = 0;
                    currentColumn.append(c);
                }

            } else {

                /*
                 * QUOTE_MODE (within quotes).
                 */

                if( c == quoteEscapeChar ) {
                    int nextCharIndex = charIndex + 1;
                    boolean availableCharacters = nextCharIndex < line.length();
                    boolean nextCharIsQuote = availableCharacters && line.charAt(nextCharIndex) == quoteChar;
                    boolean nextCharIsEscapeQuoteChar = availableCharacters && line.charAt(nextCharIndex) == quoteEscapeChar;

                    if( nextCharIsQuote ) {
                        /*
                         * An escaped quote (e.g. "" or \"). Skip over the escape char, and add
                         * the following quote char as part of the column;
                         */
                        charIndex++;
                        currentColumn.append(quoteChar);
                    } else if( nextCharIsEscapeQuoteChar ) {
                        /*
                         * A double escape (normally \\). Save the escape char, then continue to
                         * next character.
                         */
                        currentColumn.append(c);
                        charIndex++;
                    } else if( quoteEscapeChar == quoteChar ) {
                        /*
                         * If the escape char is also the quote char and we didn't escape a
                         * subsequent character, then this is a lone quote and the end of the
                         * field.
                         */
                        state = DecoderState.NORMAL;
                    } else {
                        /*
                         * Escape char wasn't before either another escape char or a quote char,
                         * so process it normally.
                         */
                        currentColumn.append(c);
                    }
                } else if( c == quoteChar ) {

                    /*
                     * A single quote ("). Update to NORMAL (but don't save quote), then continue to next character.
                     */
                    state = DecoderState.NORMAL;

                    int nextCharIndex = charIndex + 1;
                    boolean availableCharacters = nextCharIndex < line.length();
                    boolean nextCharIsQuote = availableCharacters && line.charAt(nextCharIndex) == quoteChar;

                    if( quoteEscapeChar != quoteChar && nextCharIsQuote ) {
                        throw new SuperCsvException("Encountered repeat quote char (" +
                                quoteChar + ") when quoteEscapeChar was (" + quoteEscapeChar + ")" +
                                ".  Cannot process data where quotes are escaped both with " +
                                quoteChar + " and with " + quoteEscapeChar);
                    }
                } else {
                    /*
                     * Just a normal character, delimiter (they don't count in QUOTESCOPE) or space. Add the character,
                     * then continue to next character.
                     */
                    currentColumn.append(c);
                }
            }

            charIndex++; // read next char of the line
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isPending() {
        return currentColumn.toString().length() != 0;
    }

    /**
     * Adds the currentColumn to columns list managing the case with currentColumn.length() == 0
     * It was introduced to manage the emptyColumnParsing.
     *
     * @param columns
     * @param line
     * @param charIndex
     */
    private void addColumn(final List<String> columns, String line, int charIndex) {

        if(currentColumn.length() > 0){
            columns.add(currentColumn.toString());
        }
        else{
            int previousCharIndex = charIndex - 1;
            boolean availableCharacters = previousCharIndex >= 0 ;
            boolean previousCharIsQuote = availableCharacters && line.charAt(previousCharIndex) == quoteChar;
            String noValue = ( (previousCharIsQuote) && emptyColumnParsing.equals(EmptyColumnParsing.ParseEmptyColumnsAsEmptyString)) ? "" : null;
            columns.add(noValue);
        }
    }

    /**
     * Appends the required number of spaces to the StringBuilder.
     *
     * @param sb
     *            the StringBuilder
     * @param spaces
     *            the required number of spaces to append
     */
    private static void appendSpaces(final StringBuilder sb, final int spaces) {
        for( int i = 0; i < spaces; i++ ) {
            sb.append(SPACE);
        }
    }
}
