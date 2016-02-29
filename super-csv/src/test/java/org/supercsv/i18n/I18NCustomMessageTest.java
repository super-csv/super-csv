package org.supercsv.i18n;

import static org.junit.Assert.assertEquals;
import static org.supercsv.SuperCsvTestUtils.ANONYMOUS_CSVCONTEXT;
import static org.supercsv.SuperCsvTestUtils.time;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.supercsv.cellprocessor.FmtSqlTime;
import org.supercsv.exception.SuperCsvException;

/**
 * Tests I18N message customization.
 * 
 * @author Jesús G. Vences
 */
public class I18NCustomMessageTest {
	
	private static final Locale EN = Locale.ENGLISH;
	private static final Locale ES = new Locale("es");
	
	/**
	 * Tests execution of FmtSql with a invalid input (should throw an Exception with a custom message in English).
	 * @throws Exception 
	 */
	@Test
	public void testFmtSqlTimeWithInvalidDateFormatCustomEnglishMessage() throws Exception {		
		FmtSqlTime invalidDateFormatProcessor = new FmtSqlTime("abcd");
		SuperCsvMessages.setDefaultLocale(EN);
		try {
			invalidDateFormatProcessor.execute(time(1, 12, 25), ANONYMOUS_CSVCONTEXT);
		}
		catch(SuperCsvException e) {
			assertEquals("custom message abcd", e.getMessage());
		}
	}
	
	/**
	 * Tests execution of FmtSql with a invalid input (should throw an Exception with a custom message in Spanish).
	 * @throws Exception 
	 */
	@Test
	public void testFmtSqlTimeWithInvalidDateFormatCustomSpanishMessage() throws Exception {		
		FmtSqlTime invalidDateFormatProcessor = new FmtSqlTime("abcd");
		SuperCsvMessages.setDefaultLocale(ES);
		try {
			invalidDateFormatProcessor.execute(time(1, 12, 25), ANONYMOUS_CSVCONTEXT);
		}
		catch(SuperCsvException e) {
			assertEquals("mensaje personalizado abcd", e.getMessage());
		}
	}
	
	private static List<File> boundleFiles;
	
	/**
	 * Temporal MessageBundles por testing purposes */
	@BeforeClass
	public static void setUp() throws Exception{
		boundleFiles = new ArrayList<File>();
		boundleFiles.add(createCustomMessageBundle("SuperCsvMessages",EN)
		.addKey("org.supercsv.exception.cellprocessor.InvalidDatePattern.message","custom message {0}")
		.doCreate());
		
		boundleFiles.add(createCustomMessageBundle("SuperCsvMessages",ES)
			.addKey("org.supercsv.exception.cellprocessor.InvalidDatePattern.message","mensaje personalizado {0}")
			.doCreate());
	}
	
	/**
	 * Delete after testing to avoid interfering with other tests*/
	@AfterClass
	public static void deleteBoundles(){
		for(File bundleFile:boundleFiles){
			bundleFile.delete();
		}
	}
	
	/**
	 * Helper method to create temporary MessageBundle */
	private static MessageBundleCreator createCustomMessageBundle(String bundleName, Locale locale) {
		return new MessageBundleCreator(bundleName, locale);
	}

	/**
	 * Intended only for testing purposes.
	 * Creates a message bundle (.properties file) on the root of the class path containing */
	static class MessageBundleCreator {
		private Locale locale;
		private String bundleName;
		private Map<String, String> bundleKeys = new HashMap<String, String>();
		
		public MessageBundleCreator(String bundleName, Locale locale) {
			this.locale = locale;
			this.bundleName=bundleName;
		}

		public MessageBundleCreator addKey(String messageKey, String message) {
			bundleKeys.put(messageKey, message);
			return this;
		}

		public File doCreate() throws Exception {
			File classPathRoot = new File(new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath()).getParent(),"classes");
			File boundleFile = new File(classPathRoot, getFileName());
			boundleFile.delete();
			FileWriter writer = new FileWriter(boundleFile);
			for( Entry<String, String> entry : bundleKeys.entrySet() ) {
				writer.write(new StringBuilder(entry.getKey()).append("=").append(entry.getValue()).append("\n")
					.toString());
			}
			writer.flush();
			writer.close();
			return boundleFile;
		}

		private String getFileName() {
			return new StringBuilder(bundleName).append("_").append(locale.toString()).append(".properties").toString();
		}
	}
}
