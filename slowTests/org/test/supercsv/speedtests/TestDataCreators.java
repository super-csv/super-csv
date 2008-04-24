/*
 * SuperCSV is Copyright 2007, Kasper B. Graversen Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package org.test.supercsv.speedtests;

import java.util.Random;

/**
 * creator methods to create random data for test data file
 * 
 * @author Kasper B. Graversen generate random content
 */
public class TestDataCreators {
static Random r = new Random();
static {
	r.setSeed(System.currentTimeMillis());
}

public static String createAnonymousLine_num_str_str_num_date() {
	final String s = "" + number(100000) + ", " + string(7) + ", " + string(10) + ", " + number(200) + ", " + date()
		+ "\n";
	if( r.nextInt() % 30 == 0 ) { return "\"" + s + "\""; }
	return s;
}

public static String date() {
	return "" + (number(11) + 1) + "/" + (number(11) + 1) + "/" + (number(11) + 1);
}

// public static void main(final String[] args) {
// for(int i = 0; i < 10; i++) {
// System.out.println(createAnonymousLine());
// }
// }

public static int number(final int max) {
	return r.nextInt(max);
}

public static String string(final int maxLengh) {
	final int len = 1 + r.nextInt(maxLengh);
	final StringBuffer sb = new StringBuffer();
	for( int i = 0; i < len; i++ ) {
		if( r.nextInt() % 6 == 0 ) {
			sb.append(' ');
		} else {
			sb.append(Character.toChars(65 + r.nextInt(25)));
		}
	}
	if( r.nextInt() % 30 == 0 ) { return "\"" + sb.toString() + "\""; // some times make it a "" string
	}
	return sb.toString();
}
}
