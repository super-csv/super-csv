package org.supercsv.cellprocessor;

import java.util.List;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CsvContext;

/**
 * Formats a List<String> into a single column in a CSV file as a string.  It adheres to the same rules 
 * as SuperCSV in regards to quoting and escaping, 
 * <a href="https://super-csv.github.io/super-csv/csv_specification.html">csv specification</a>. 
 * <p>
 * For Example:<br>
 * Given list: {"aaa", "bbb", "ccc,"}<br>
 * Written as: "aaa,bbb,""ccc,"""<br>
 * 
 * Since the list is comma delimited the entire string is quoted, entries one and two of the list don't have
 * any additional quotes because they don't contain ", comma or CRLF. The third value as a comma in it so it
 * is quoted but since it's already in a quoted string because it's a list, the quotes need to be escaped.
 * 
 * @author Jim Judd
 * 
 *
 */
public class FmtStringList extends CellProcessorAdaptor {

	public FmtStringList() {
		super();
	}

    public FmtStringList(CellProcessor next) {
        // this constructor allows other processors to be chained after FmtStringList
        super(next);
}

	@SuppressWarnings("unchecked")
	public Object execute(Object value, CsvContext context) {
		validateInputNotNull(value, context);  // throws an Exception if the input is null

		StringBuilder buffer = new StringBuilder();		
		List<String> strList = (List<String>) value;
		boolean escape;
		
		for (String str : strList) {
			escape = false;
			if (buffer.length() > 0) {
				buffer.append(",");
			}

			// Determine if the string contains quotes so they can be escaped
			boolean hasQuotes = str.indexOf('"') != -1;

			if (hasQuotes || (str.indexOf(',') != -1) || (str.indexOf("\r\n") != -1)) {
				escape = true;
			}
			if (escape) {
				buffer.append("\"");
				if (hasQuotes) {
					// Ok there are quotes so escape them
					String replace = str.replace("\"", "\"\"");
					buffer.append(replace);
				} else {
					buffer.append(str);
				}
				buffer.append("\"");
			} else {
				buffer.append(str);
			}
		}
		
        return next.execute(buffer.toString(), context);
	}

}