package org.supercsv.util;

import java.util.HashMap;
import java.util.Set;

/**
 * A two-dimensional hashmap, is a HashMap that enables you to refer to values via two keys rather than one. The
 * underlying implementation is simply a HashMap containing HashMap, each of which maps to values.
 * <P>
 * This is quite useful e.g. when wanting to cache values such as "currency ration" based keys "day of year" and "year".
 * 
 * @see java.util.HashMap
 * @author Kasper B. Graversen
 */
public class TwoDHashMap<K1, K2, V> {
	
	private HashMap<K1, HashMap<K2, V>> map = new HashMap<K1, HashMap<K2, V>>();
	
	/**
	 * Constructs a new <tt>TwoDHashMap</tt>.
	 */
	public TwoDHashMap() {
	}
	
	/**
	 * Constructs a new <tt>TwoDHashMap</tt> using the supplied map.
	 * 
	 * @param map
	 *            the map
	 */
	public TwoDHashMap(final HashMap<K1, HashMap<K2, V>> map) {
		this.map = map;
	}
	
	/**
	 * Existence check of a value (or <tt>null</tt>) mapped to the keys.
	 * 
	 * @param firstKey
	 *            first key
	 * @param secondKey
	 *            second key
	 * @return true when an element (or <tt>null</tt>) has been stored with the keys
	 */
	public boolean containsKey(final K1 firstKey, final K2 secondKey) {
		// existence check on inner map
		final HashMap<K2, V> innerMap = map.get(firstKey);
		if( innerMap == null ) {
			return false;
		}
		return innerMap.containsKey(secondKey);
	}
	
	/**
	 * Fetch a value from the Hashmap .
	 * 
	 * @param firstKey
	 *            first key
	 * @param secondKey
	 *            second key
	 * @return the element or null.
	 */
	public V get(final K1 firstKey, final K2 secondKey) {
		// existence check on inner map
		final HashMap<K2, V> innerMap = map.get(firstKey);
		if( innerMap == null ) {
			return null;
		}
		return innerMap.get(secondKey);
	}
	
	/**
	 * Insert a value
	 * 
	 * @param firstKey
	 *            first key
	 * @param secondKey
	 *            second key
	 * @param value
	 *            the value to be inserted. <tt>null</tt> may be inserted as well.
	 * @return null or the value the insert is replacing.
	 */
	public Object set(final K1 firstKey, final K2 secondKey, final V value) {
		// existence check on inner map
		HashMap<K2, V> innerMap = map.get(firstKey);
		
		if( innerMap == null ) {
			// no inner map, create it
			innerMap = new HashMap<K2, V>();
			map.put(firstKey, innerMap);
		}
		
		return innerMap.put(secondKey, value);
	}
	
	/**
	 * Returns the number of key-value mappings in this map for the first key.
	 * 
	 * @return Returns the number of key-value mappings in this map for the first key.
	 */
	public int size() {
		return map.size();
	}
	
	/**
	 * Returns the number of key-value mappings in this map for the second key.
	 * 
	 * @return Returns the number of key-value mappings in this map for the second key.
	 */
	public int size(final K1 firstKey) {
		// existence check on inner map
		final HashMap<K2, V> innerMap = map.get(firstKey);
		if( innerMap == null ) {
			return 0;
		}
		return innerMap.size();
	}
	
	/**
	 * Returns a set of the keys of the outermost map.
	 */
	public Set<K1> keySet() {
		return map.keySet();
	}
	
}
