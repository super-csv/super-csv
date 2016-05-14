package org.supercsv.util;

import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

public abstract class Form {
	private Form(){
		
	}
	
	public static String at(String template, Object... params){
		FormattingTuple formatted = MessageFormatter.arrayFormat(template, params);
			
		return formatted.toString();
	}
}
