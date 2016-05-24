package org.supercsv.i18n;

import java.io.IOException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A resource bundle locator, that loads resource bundles by invoking {@code ResourceBundle.loadBundle(String, Local, ClassLoader)}.
 * <p>
 * This locator is also able to load all property files of a given name (in case there are multiple with the same
 * name on the classpath) and aggregates them into a {@code ResourceBundle}.
 * <p>
 * Adapted from: {@code org.hibernate.validator.resourceloading.PlatformResourceBundleLocator}
 * 
 * @author Jesus G. Vences
 */
public final class PlatformResourceBundleLocator{
	private static final Map<String, ResourceBundle> loadedBundles = new ConcurrentHashMap<String, ResourceBundle>(); 
    /** constant indicating that no resource bundle exists */
    private static final ResourceBundle NONEXISTENT_BUNDLE = new ResourceBundle() {
            public Enumeration<String> getKeys() { return null; }
            protected Object handleGetObject(String key) { return null; }
            public String toString() { return "NONEXISTENT_BUNDLE"; }
        };
	/**
	 * Search current thread classloader for the resource bundle. If not found,
	 * search this classloader.
	 *
	 * @param bundleName the name of the bundle to load.
	 * @param locale The locale of the bundle to load.
	 * @return the resource bundle or {@code null} if none is found.
	 */
	public static ResourceBundle getResourceBundle(String bundleName, Locale locale) {
		if(bundleName==null){
			throw new IllegalArgumentException("bundleName cannot be null");
		}
		if(locale==null){
			throw new IllegalArgumentException("locale cannot be null");
		}
		
		String bundleKey = new StringBuilder(bundleName).append("-").append(locale.toString()).toString();
		ResourceBundle rb = loadedBundles.get(bundleKey);
		
		if( rb == null && rb != NONEXISTENT_BUNDLE ) {
			ClassLoader classLoader = run( GetClassLoader.fromContext() );
			if ( classLoader != null ) {
				rb = loadBundle(
					bundleName, classLoader, locale
				);
			}
	
			if ( rb == null ) {
				classLoader = run( GetClassLoader.fromClass( PlatformResourceBundleLocator.class ) );
				rb = loadBundle(
					bundleName, classLoader, locale
				);
			}
			if( rb == null ) {
				rb = NONEXISTENT_BUNDLE;
			}
			loadedBundles.put(bundleKey, rb);
		}
		return rb;
	}

	private static ResourceBundle loadBundle(String bundleName, ClassLoader classLoader, Locale locale) {
		ResourceBundle rb = null;
		try {
			rb = ResourceBundle.getBundle(
				bundleName,
				locale,
				classLoader
				);
		}
		catch ( MissingResourceException e ) {
			e.printStackTrace();
		}
		return rb;
	}
	
	static void resetLoadedBundles(){
		loadedBundles.clear();
	}
	/**
	 * Runs the given privileged action, using a privileged block if required.
	 * <p>
	 * <b>NOTE:</b> This must never be changed into a publicly available method to avoid execution of arbitrary
	 * privileged actions within Super CSV's protection domain.
	 */
	private static <T> T run(PrivilegedAction<T> action) {
		return System.getSecurityManager() != null ? AccessController.doPrivileged( action ) : action.run();
	}
	
	/**
	 * @author Emmanuel Bernard
	 */
	final static class GetClassLoader implements PrivilegedAction<ClassLoader> {
		private final Class<?> clazz;

		public static GetClassLoader fromContext() {
			return new GetClassLoader( null );
		}

		public static GetClassLoader fromClass(Class<?> clazz) {
			if(clazz == null){
				throw new IllegalArgumentException("clazz cannot be null");
			}
			return new GetClassLoader( clazz );
		}

		private GetClassLoader(Class<?> clazz) {
			this.clazz = clazz;
		}

		public ClassLoader run() {
			if ( clazz != null ) {
				return clazz.getClassLoader();
			}
			else {
				return Thread.currentThread().getContextClassLoader();
			}
		}
	}
	
	/**
	 * A {@code PrivilegedAction} wrapping around {@code ClassLoader.getResources(String)}.
	 *
	 * @author Hardy Ferentschik
	 */
	final static class GetResources implements PrivilegedAction<Enumeration<URL>> {

		private final String resourceName;
		private final ClassLoader classLoader;

		public static GetResources action(ClassLoader classLoader, String resourceName) {
			return new GetResources( classLoader, resourceName );
		}

		private GetResources(ClassLoader classLoader, String resourceName) {
			this.classLoader = classLoader;
			this.resourceName = resourceName;
		}

		public Enumeration<URL> run() {
			try {
				return classLoader.getResources( resourceName );
			}
			catch ( IOException e ) {
				// Collections.emptyEnumeration() would be 1.7
				return Collections.enumeration( Collections.<URL>emptyList() );
			}
		}
	}
}