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

import java.util.List;

import org.supercsv.prefs.CsvPreference;

/**
 * The interface for Csv decoders, which are responsible for decoding the CSV file.
 *
 * @author Chen Guoping
 * @since 2.5.0 (adapted from Tokenizer)
 */
public interface CsvDecoder {

    /**
     * Initialize the decoder with CsvPreference to prepare for decoding the CSV file
     *
     * @param preference
     *           the CsvPreference
     */
    void initDecoder(CsvPreference preference);

    /**
     * Decoding the line which read from CSV file to columns(List). Because the row may span multiple lines, the boolean
     * variable multi identifies whether the line is the first line in the row.
     *
     * @param line
     *           the line which read from CSV file
     * @param multi
     *           boolean if <code>true</code>, not the first line in the row,
     *           if <code>false</code>, first line in the row.
     * @return columns list that the line decode.
     */
    List<String> decode(String line, boolean multi);

    /**
     * Judge whether the current row(may span multiple lines) decoded complete
     *
     * @return true, if something was left over from last decoding
     */
    boolean isPending();
}
