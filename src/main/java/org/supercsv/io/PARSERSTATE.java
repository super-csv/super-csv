package org.supercsv.io;

/**
 * @author Kasper B. Graversen
 */
enum PARSERSTATE {
	NORMAL, // normal text
	QUOTESCOPE, // inside quote scope (e.g.: " here "
}
