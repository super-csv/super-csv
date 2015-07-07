package org.supercsv.cellprocessor.time;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.Temporal;
import java.util.Objects;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

/**
 * Abstract base class for cell processors converting Strings to {@link java.time} types.
 * 
 * @since 2.4.0
 * @author James Bassett
 * @param <T>
 *            the {@link java.time} type that the processor returns
 */
public abstract class AbstractTemporalParsingProcessor<T extends Temporal> extends
		CellProcessorAdaptor implements StringCellProcessor {

	private final DateTimeFormatter formatter;

	/**
	 * Constructs a new <tt>AbstractTemporalParsingProcessor</tt> processor, which
	 * parses a String as a {@link java.time} type.
	 */
	public AbstractTemporalParsingProcessor() {
		this.formatter = null;
	}

	/**
	 * Constructs a new <tt>AbstractTemporalParsingProcessor</tt> processor, which
	 * parses a String as a {@link java.time} type, then calls the next processor in the
	 * chain.
	 * 
	 * @param next
	 *            the next processor in the chain
	 * @throws NullPointerException
	 *             if next is null
	 */
	public AbstractTemporalParsingProcessor(final CellProcessor next) {
		super(next);
		this.formatter = null;
	}

	/**
	 * Constructs a new <tt>AbstractTemporalParsingProcessor</tt> processor, which
	 * parses a String as a {@link java.time} type using the supplied formatter.
	 * 
	 * @param formatter
	 *            the formatter used for parsing
	 * @throws NullPointerException
	 *             if formatter is null
	 */
	public AbstractTemporalParsingProcessor(final DateTimeFormatter formatter) {
		checkPreconditions(formatter);
		this.formatter = formatter;
	}

	/**
	 * Constructs a new <tt>AbstractTemporalParsingProcessor</tt> processor, which
	 * parses a String as a {@link java.time} type using the supplied formatter, then calls
	 * the next processor in the chain.
	 * 
	 * @param formatter
	 *            the formatter used for parsing
	 * @param next
	 *            the next processor in the chain
	 * @throws NullPointerException
	 *             if formatter or next is null
	 */
	public AbstractTemporalParsingProcessor(final DateTimeFormatter formatter,
			final CellProcessor next) {
		super(next);
		checkPreconditions(formatter);
		this.formatter = formatter;
	}

	/**
	 * Checks the preconditions for creating a new AbstractTemporalParsingProcessor
	 * processor.
	 * 
	 * @param formatter
	 *            the formatter
	 * @throws NullPointerException
	 *             if formatter is null
	 */
	private static void checkPreconditions(final DateTimeFormatter formatter) {
		Objects.requireNonNull(formatter, "formatter should not be null");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws SuperCsvCellProcessorException
	 *             if value is null or is not a String
	 */
	public Object execute(final Object value, final CsvContext context) {
		validateInputNotNull(value, context);
		if (!(value instanceof String)) {
			throw new SuperCsvCellProcessorException(String.class, value,
					context, this);
		}

		final String string = (String) value;
		final T result;
		try {
			if (formatter != null) {
				result = parse(string, formatter);
			} else {
				result = parse(string);
			}
		} catch (DateTimeParseException e) {
			throw new SuperCsvCellProcessorException("Failed to parse value",
					context, this, e);
		}

		return next.execute(result, context);
	}

	/**
	 * Parses the String into the appropriate {@link java.time} type.
	 * 
	 * @param string
	 *            the string to parse
	 * @return the {@link java.time} type
	 * @throws IllegalArgumentException
	 *             if the string can't be parsed
	 */
	protected abstract T parse(final String string);

	/**
	 * Parses the String into the appropriate {@link java.time} type, using the supplied
	 * formatter.
	 * 
	 * @param string
	 *            the string to parse
	 * @param formatter
	 *            the formatter to use
	 * @return the {@link java.time} type
	 * @throws IllegalArgumentException
	 *             if the string can't be parsed
	 */
	protected abstract T parse(final String string,
			final DateTimeFormatter formatter);

}
