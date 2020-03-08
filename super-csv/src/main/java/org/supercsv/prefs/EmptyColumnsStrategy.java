/*
 * Copyright 2020
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
package org.supercsv.prefs;

/**
 * The strategies that the Tokenizer can use about interpreting rows with all fields of a row empty (i.e. a row of
 * commas only)
 *
 * @author Kai Hackemesser
 */
public enum EmptyColumnsStrategy {

    /**
     * Lines with only empty columns are to be skipped.
     */
   FILTER {
        public boolean toBeSkipped(String line, char delimiter) {
            return line.matches("^("+ delimiter+")+$");
        }
    },
    /**
     * Lines with only empty columns are to be returned.
     */
    PASS ;

    /**
     *  analyzes the row.
     * @return true if the Tokenizer should skip this row during processing.
     */
    public boolean toBeSkipped(String line, char delimiter) {
        return false;
    }
}
