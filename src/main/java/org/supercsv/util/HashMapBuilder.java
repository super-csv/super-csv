package org.supercsv.util;

import java.util.HashMap;

/**
 * Shortcut way to build hashmaps. It's great for making concise tests.
 * <p>
 * Instead of
 * 
 * <pre>
 * HashMap&lt;String, Integer&gt; map = new HashMap&lt;String, Integer&gt;();
 * map.put(&quot;one&quot;, 1);
 * map.put(&quot;two&quot;, 2);
 * ...
 * </pre>
 * 
 * you can now chain the inserts and use the more familiar add().
 * 
 * <pre>
 * HashMap&lt;String, Integer&gt; map = new HashMapBuilder&lt;String, Integer&gt;().add(&quot;one&quot;, 1).add(&quot;two&quot;, 2).build();
 * </pre>
 * 
 * @author Kasper B. Graversen, (c) 2007-2008
 */
public class HashMapBuilder<K, V> {
	HashMap<K, V> map;
	
	public HashMapBuilder() {
		map = new HashMap<K, V>();
	}
	
	/**
	 * add a key-value pair
	 */
	public HashMapBuilder<K, V> add(final K key, final V value) {
		map.put(key, value);
		return this;
	}
	
	/**
	 * build the hashmap.
	 */
	public HashMap<K, V> build() {
		return map;
	}
}
