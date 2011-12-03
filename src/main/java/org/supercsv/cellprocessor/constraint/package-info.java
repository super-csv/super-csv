/**
 * Provides <tt>CellProcessor</tt> classes for enforcing constraints.
 * <p>
 * Note however, that in order for these processors to carry out their constraint logic, they may convert the input
 * data. For example, the <tt>Strlen</tt> constraint, given the number 17, converts it to the string <tt>"17"</tt> before doing its length
 * check.
 */
package org.supercsv.cellprocessor.constraint;