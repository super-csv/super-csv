package org.supercsv.cellprocessor;

import java.util.Map;

import org.supercsv.cellprocessor.ift.BoolCellProcessor;
import org.supercsv.cellprocessor.ift.DateCellProcessor;
import org.supercsv.cellprocessor.ift.DoubleCellProcessor;
import org.supercsv.cellprocessor.ift.LongCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.NullInputException;
import org.supercsv.util.CSVContext;

/**
 * Maps from one object to another, by looking up a <tt>Map</tt> with the input as the key, and returning its
 * corresponding value.
 * 
 * @since 1.50
 * @author Dominique De Vito
 */
public class HashMapper extends CellProcessorAdaptor implements BoolCellProcessor, DateCellProcessor,
	DoubleCellProcessor, LongCellProcessor, StringCellProcessor {
	
	private final Map<Object, Object> mapping;
	private final Object defaultValue;
	
	/**
	 * Constructs a new <tt>HashMapper</tt> processor, which maps from one object to another, by looking up a
	 * <tt>Map</tt> with the input as the key, and returning its corresponding value. If no mapping is found, then
	 * <tt>null</tt> is returned.
	 * 
	 * @param mapping
	 *            the Map
	 */
	public HashMapper(final Map<Object, Object> mapping) {
		super();
		this.mapping = mapping;
		this.defaultValue = null;
	}
	
	/**
	 * Constructs a new <tt>HashMapper</tt> processor, which maps from one object to another, by looking up a
	 * <tt>Map</tt> with the input as the key, and returning its corresponding value. If no mapping is found, then the
	 * supplied default value is returned.
	 * 
	 * @param mapping
	 *            the Map
	 * @param defaultValue
	 *            the value to return if no mapping is found
	 */
	public HashMapper(final Map<Object, Object> mapping, final Object defaultValue) {
		super();
		this.mapping = mapping;
		this.defaultValue = defaultValue;
	}
	
	/**
	 * Constructs a new <tt>HashMapper</tt> processor, which maps from one object to another, by looking up a
	 * <tt>Map</tt> with the input as the key, and returning its corresponding value. If no mapping is found, then
	 * <tt>null</tt> is returned. Regardless of whether a mapping is found, the next processor in the chain will be
	 * called.
	 * 
	 * @param mapping
	 *            the Map
	 * @param next
	 *            the next processor in the chain
	 */
	public HashMapper(final Map<Object, Object> mapping, final BoolCellProcessor next) {
		this(mapping, null, next);
	}
	
	/**
	 * Constructs a new <tt>HashMapper</tt> processor, which maps from one object to another, by looking up a
	 * <tt>Map</tt> with the input as the key, and returning its corresponding value. If no mapping is found, then the
	 * supplied default value is returned. Regardless of whether a mapping is found, the next processor in the chain
	 * will be called.
	 * 
	 * @param mapping
	 *            the Map
	 * @param defaultValue
	 *            the value to return if no mapping is found
	 * @param next
	 *            the next processor in the chain
	 */
	public HashMapper(final Map<Object, Object> mapping, final Object defaultValue, final BoolCellProcessor next) {
		super(next);
		this.mapping = mapping;
		this.defaultValue = defaultValue;
		if( mapping == null ) {
			throw new NullInputException("Mapping cannot be null", this);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object execute(final Object value, final CSVContext context) {
		validateInputNotNull(value, context, this);
		Object result = mapping.get(value);
		if( result == null ) {
			result = defaultValue;
		}
		return next.execute(result, context);
	}
}
