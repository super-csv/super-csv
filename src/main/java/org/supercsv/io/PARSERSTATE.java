package org.supercsv.io;

/**
 * An enumeration of parser states.
 * 
 * @author Kasper B. Graversen
 */
enum PARSERSTATE {
	
	/** normal text */
	NORMAL,

	/** inside quote scope (e.g. " here ") */
	QUOTESCOPE,
}
