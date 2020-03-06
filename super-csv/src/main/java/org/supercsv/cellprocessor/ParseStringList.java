package org.supercsv.cellprocessor;

import java.util.ArrayList;
import java.util.List;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

/**
 * Parses a csv column containing a list of strings back into a List<String>.  
  * 
 * For Example:
 * Given list: "aaa,bbb,""ccc,"""
 * Converted to: {"aaa", "bbb", "ccc,"}
 * 
 * @See FmtStringList
 * 
 * @author Jim Judd
 *
 */
public class ParseStringList extends CellProcessorAdaptor {

			public ParseStringList() {
				super();
			}

		    public ParseStringList(CellProcessor next) {
		        // this constructor allows other processors to be chained after FmtStringList
		        super(next);
		}

			@SuppressWarnings("unchecked")
			public List<String> execute(Object value, CsvContext context) {
				
				validateInputNotNull(value, context);  // throws an Exception if the input is null

				List<String> strList = new ArrayList<String>();
				
				String strValue = (String) value;
				int pos = 0;
				
				while ((pos < strValue.length()) && (pos != -1)) {
					boolean isEscaped = strValue.startsWith("\"", pos);
					if (isEscaped) {
						int curPos = ++pos;
						while ((pos <= strValue.length()) && (pos != -1)) {
							pos = strValue.indexOf("\"", pos);
							if (pos == -1) {
								throw new SuperCsvCellProcessorException(
				                        "List value didn't contain a closing qoute after position " + pos, context, this);
							}
							if (strValue.startsWith("\"", pos+1)) {
								pos += 2;
							} else {
								String substring = strValue.substring(curPos, pos);
								if (substring.indexOf("\"\"") != -1) {
									// Found an escaped string so replace with single quote
									substring = substring.replace("\"\"", "\"");
								}
								strList.add(substring);
								pos = strValue.indexOf(",", pos);
								if (pos != -1) {
									pos++;
									break;
								}
							}
						}
						
					} else {
						int curPos = pos;
						pos = strValue.indexOf(",", pos);
						if (pos == -1) {
							strList.add(strValue.substring(curPos));
						} else {
							String str = strValue.substring(curPos, pos);
							strList.add(str);
							pos++;
						}

					}
				}
								
		        return strList;
			}
}
