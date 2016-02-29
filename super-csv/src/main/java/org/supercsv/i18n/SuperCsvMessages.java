package org.supercsv.i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Resource bundle backed message interpolator.
 * 
 * @author Jesus G. Vences
 */
public final class SuperCsvMessages {
	/**
	 * The name of the default message bundle.
	 */
	private static final String DEFAULT_SUPER_CSV_MESSAGES = "org.supercsv.SuperCsvMessages";
	
	/**
	 * The name of the user-provided message bundle.
	 */
	private static final String USER_SUPER_CSV_MESSAGES = "SuperCsvMessages";
	
	/**
	 * The default locale.
	 */
	private static Locale defaultLocale;
	
	/**
	 * Runs the message interpolation using {@code defaultLocale}
	 * 
	 * @param messageKey
	 *            the messageKey to interpolate
	 * @param params
	 *            object(s) to format
	 * @return the interpolated message.
	 */
	public static String getMessage(String messageKey, Object... params) {
		return getMessage(messageKey, defaultLocale == null ? Locale.getDefault() : defaultLocale, params);
	}
	
	/**
	 * Runs the message interpolation.
	 * 
	 * @param messageKey
	 *            the messageKey to interpolate
	 * @param locale
	 *            the {@code Locale} to use for the resource bundle.
	 * @param params
	 *            object(s) to format
	 * @return the interpolated message.
	 */
	public static String getMessage(String messageKey, Locale locale, Object... params) {
		String resolvedMessage = null;
		
		ResourceBundle userResourceBundle = PlatformResourceBundleLocator.getResourceBundle(USER_SUPER_CSV_MESSAGES,
			locale);
		if( userResourceBundle != null ) {
			try {
				resolvedMessage = userResourceBundle.getString(messageKey);
			}
			catch(MissingResourceException userDefinitionNotFound) {
				resolvedMessage = getDefaultMessage(messageKey, locale);
			}
		} else {
			resolvedMessage = getDefaultMessage(messageKey, locale);
		}
		
		return MessageFormat.format(resolvedMessage, params);
	}
	/**
	 * Mainly for tests purposes */
	public static void setDefaultLocale(Locale locale) {
		defaultLocale = locale;
		PlatformResourceBundleLocator.resetLoadedBundles();
	}
	
	private static String getDefaultMessage(String messageKey, Locale locale) {
		try {
			return PlatformResourceBundleLocator.getResourceBundle(DEFAULT_SUPER_CSV_MESSAGES, locale).getString(
				messageKey);
		}
		catch(MissingResourceException userDefinitionNotFound) {
			return messageKey;
		}
	}
}
