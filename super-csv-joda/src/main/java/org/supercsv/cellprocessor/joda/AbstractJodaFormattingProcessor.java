package org.supercsv.cellprocessor.joda;

import java.util.Locale;

import org.joda.time.format.DateTimeFormatter;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.i18n.SuperCsvMessages;
import org.supercsv.util.CsvContext;

/**
 * Abstract base class for cell processors converting Joda types to Strings.
 * 
 * @since 2.3.0
 * @author James Bassett
 * @param <T>
 *            the Joda type that the processor formats
 */
public abstract class AbstractJodaFormattingProcessor<T> extends
		CellProcessorAdaptor {

	private final Class<T> jodaClass;

	private final DateTimeFormatter formatter;

	private final String pattern;

	private final Locale locale;

	/**
	 * Constructs a new <tt>AbstractJodaFormattingProcessor</tt> processor,
	 * which formats the Joda type as a String.
	 * 
	 * @param jodaClass
	 *            the Joda class that the processor formats
	 * @throws NullPointerException
	 *             if jodaClass is null
	 */
	public AbstractJodaFormattingProcessor(final Class<T> jodaClass) {
		checkPreconditions(jodaClass);
		this.jodaClass = jodaClass;
		this.formatter = null;
		this.pattern = null;
		this.locale = null;

	}

	/**
	 * Constructs a new <tt>AbstractJodaFormattingProcessor</tt> processor,
	 * which formats the Joda type as a String, then calls the next processor in
	 * the chain.
	 * 
	 * @param jodaClass
	 *            the Joda class that the processor formats
	 * @param next
	 *            next processor in the chain
	 * @throws NullPointerException
	 *             if jodaClass or next is null
	 */
	public AbstractJodaFormattingProcessor(final Class<T> jodaClass,
			final CellProcessor next) {
		super(next);
		checkPreconditions(jodaClass);
		this.jodaClass = jodaClass;
		this.formatter = null;
		this.pattern = null;
		this.locale = null;

	}

	/**
	 * Constructs a new <tt>AbstractJodaFormattingProcessor</tt> processor,
	 * which formats the Joda type as a String using the supplied formatter.
	 * 
	 * @param jodaClass
	 *            the Joda class that the processor formats
	 * @param formatter
	 *            the formatter to use
	 * @throws NullPointerException
	 *             if jodaClass or formatter is null
	 */
	public AbstractJodaFormattingProcessor(final Class<T> jodaClass,
			final DateTimeFormatter formatter) {
		checkPreconditions(jodaClass, formatter);
		this.jodaClass = jodaClass;
		this.formatter = formatter;
		this.pattern = null;
		this.locale = null;
	}

	/**
	 * Constructs a new <tt>AbstractJodaFormattingProcessor</tt> processor,
	 * which formats the Joda type as a String using the supplied formatter,
	 * then calls the next processor in the chain.
	 * 
	 * @param jodaClass
	 *            the Joda class that the processor formats
	 * @param formatter
	 *            the formatter to use
	 * @param next
	 *            the next processor in the chain
	 * @throws NullPointerException
	 *             if jodaClass, formatter or next is null
	 */
	public AbstractJodaFormattingProcessor(final Class<T> jodaClass,
			final DateTimeFormatter formatter, final CellProcessor next) {
		super(next);
		checkPreconditions(jodaClass, formatter);
		this.jodaClass = jodaClass;
		this.formatter = formatter;
		this.pattern = null;
		this.locale = null;
	}

	/**
	 * Constructs a new <tt>AbstractJodaFormattingProcessor</tt> processor,
	 * which formats the Joda type as a String using the supplied pattern and
	 * the default locale.
	 * 
	 * @param jodaClass
	 *            the Joda class that the processor formats
	 * @param pattern
	 *            the pattern to use
	 * @throws NullPointerException
	 *             if jodaClass or pattern is null
	 */
	public AbstractJodaFormattingProcessor(final Class<T> jodaClass,
			final String pattern) {
		this(jodaClass, pattern, (Locale) null);
	}

	/**
	 * Constructs a new <tt>AbstractJodaFormattingProcessor</tt> processor,
	 * which formats the Joda type as a String using the supplied pattern and
	 * the default locale, then calls the next processor in the chain.
	 * 
	 * @param jodaClass
	 *            the Joda class that the processor formats
	 * @param pattern
	 *            the pattern to use
	 * @param next
	 *            the next processor in the chain
	 * @throws NullPointerException
	 *             if jodaClass, pattern or next is null
	 */
	public AbstractJodaFormattingProcessor(final Class<T> jodaClass,
			final String pattern, final CellProcessor next) {
		this(jodaClass, pattern, (Locale) null, next);
	}

	/**
	 * Constructs a new <tt>AbstractJodaFormattingProcessor</tt> processor,
	 * which formats the Joda type as a String using the supplied pattern and
	 * the locale.
	 * 
	 * @param jodaClass
	 *            the Joda class that the processor formats
	 * @param pattern
	 *            the pattern to use
	 * @param locale
	 *            the locale to use (default used if <tt>null</tt>)
	 * @throws NullPointerException
	 *             if jodaClass or pattern is null
	 */
	public AbstractJodaFormattingProcessor(final Class<T> jodaClass,
			final String pattern, final Locale locale) {
		checkPreconditions(jodaClass, pattern);
		this.jodaClass = jodaClass;
		this.pattern = pattern;
		this.locale = locale;
		this.formatter = null;
	}

	/**
	 * Constructs a new <tt>AbstractJodaFormattingProcessor</tt> processor,
	 * which formats the Joda type as a String using the supplied pattern and
	 * the locale, then calls the next processor in the chain.
	 * 
	 * @param jodaClass
	 *            the Joda class that the processor formats
	 * @param pattern
	 *            the pattern to use
	 * @param locale
	 *            the locale to use (default used if <tt>null</tt>)
	 * @param next
	 *            the next processor in the chain
	 * @throws NullPointerException
	 *             if jodaClass, pattern or next is null
	 */
	public AbstractJodaFormattingProcessor(final Class<T> jodaClass,
			final String pattern, final Locale locale, final CellProcessor next) {
		super(next);
		checkPreconditions(jodaClass, pattern);
		this.jodaClass = jodaClass;
		this.pattern = pattern;
		this.locale = locale;
		this.formatter = null;
	}

	/**
	 * Checks the preconditions for creating a new
	 * AbstractJodaFormattingProcessor processor.
	 * 
	 * @param jodaClass
	 *            the Joda class
	 * @throws NullPointerException
	 *             if jodaClass is null
	 */
	private static void checkPreconditions(final Class<?> jodaClass) {
		if (jodaClass == null) {
			throw new NullPointerException("jodaClass should not be null");
		}
	}

	/**
	 * Checks the preconditions for creating a new
	 * AbstractJodaFormattingProcessor processor.
	 * 
	 * @param jodaClass
	 *            the Joda class
	 * @param formatter
	 *            the formatter
	 * @throws NullPointerException
	 *             if jodaClass or formatter is null
	 */
	private static void checkPreconditions(final Class<?> jodaClass,
			final DateTimeFormatter formatter) {
		if (jodaClass == null) {
			throw new NullPointerException("jodaClass should not be null");
		} else if (formatter == null) {
			throw new NullPointerException("formatter should not be null");
		}
	}

	/**
	 * Checks the preconditions for creating a new
	 * AbstractJodaFormattingProcessor processor.
	 * 
	 * @param jodaClass
	 *            the Joda class
	 * @param pattern
	 *            the pattern
	 * @throws NullPointerException
	 *             if jodaClass or pattern is null
	 */
	private static void checkPreconditions(final Class<?> jodaClass,
			final String pattern) {
		if (jodaClass == null) {
			throw new NullPointerException("jodaClass should not be null");
		} else if (pattern == null) {
			throw new NullPointerException("pattern should not be null");
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws SuperCsvCellProcessorException
	 *             if value is null, not the correct type, or can't be formatted
	 */
	public Object execute(final Object value, final CsvContext context) {
		validateInputNotNull(value, context);
		if (!(value.getClass().equals(jodaClass))) {
			throw new SuperCsvCellProcessorException(jodaClass, value, context,
					this);
		}
		final T jodaType = jodaClass.cast(value);
		try {
			if (formatter != null) {
				return format(jodaType, formatter);
			} else if (pattern != null) {
				return format(jodaType, pattern, locale);
			} else {
				return format(jodaType);
			}
		} catch (IllegalArgumentException e) {
			throw new SuperCsvCellProcessorException(
					SuperCsvMessages.getMessage("org.supercsv.exception.cellprocessor.joda.InvalidFormat.message",
							jodaClass.getSimpleName()), context, this, e);
		}
	}

	/**
	 * Formats the Joda type as a String using a DateTimeFormatter.
	 * 
	 * @param jodaType
	 *            the Joda type to format
	 * @param formatter
	 *            the formatter to use
	 * @return the formatted String
	 * @throws IllegalArgumentException
	 *             if the Joda type couldn't be formatted
	 */
	protected abstract String format(final T jodaType,
			final DateTimeFormatter formatter);

	/**
	 * Formats the Joda type as a String using the supplied pattern and
	 * (optional) locale.
	 * 
	 * @param jodaType
	 *            the Joda type to format
	 * @param pattern
	 *            the pattern to use
	 * @param locale
	 *            the (optional) locale
	 * @return the formatted String
	 * @throws IllegalArgumentException
	 *             if the Joda type couldn't be formatted
	 */
	protected abstract String format(final T jodaType, final String pattern,
			final Locale locale);

	/**
	 * Formats the Joda type as a String using the default pattern and locale.
	 * 
	 * @param jodaType
	 *            the Joda type to format
	 * @return the formatted String
	 * @throws IllegalArgumentException
	 *             if the Joda type couldn't be formatted
	 */
	protected abstract String format(final T jodaType);

}
