/*
 * SuperCSV is Copyright 2007, Kasper B. Graversen Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package org.supercsv.util;

import java.io.IOException;
import java.io.Writer;

/**
 * A writer that just ignores its input. Better than StringWriter on large files
 * 
 * @author Kasper B. Graversen
 */
public class NullWriter extends Writer {
@Override
public void close() throws IOException {
}

@Override
public void flush() throws IOException {
}

@Override
public void write(final char[] arg0, final int arg1, final int arg2) throws IOException {
}

}
