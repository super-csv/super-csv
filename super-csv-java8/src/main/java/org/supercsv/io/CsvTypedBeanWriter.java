package org.supercsv.io;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.supercsv.prefs.CsvPreference;

/**
 * Default implementation of {@link ICsvTypedBeanWriter}.
 *
 * @param <T> Type of beans to be written.
 */
public final class CsvTypedBeanWriter<T> implements ICsvTypedBeanWriter<T> {

    private final ICsvListWriter writer;

    /**
     * Constructs a new <tt>CsvTypedBeanWriter</tt> with the supplied Writer and CSV preferences.
     * Note that the <tt>writer</tt> will be wrapped in a <tt>BufferedWriter</tt> before accessed.
     *
     * @param writer the writer
     * @param preference the CSV preferences
     * @throws NullPointerException if writer or preference are null
     */
    public CsvTypedBeanWriter(final Writer writer, final CsvPreference preference) {
        this.writer = new CsvListWriter(writer, preference);
    }

    @Override
    public void write(final Collection<T> beans,
        final Collection<Function<T, ?>> extractors) throws IOException {
        for (final T row : beans) {
            this.writer.write(
                extractors.stream()
                    .map(extractor -> extractor.apply(row))
                    .collect(Collectors.toList())
            );
        }
    }

    @Override
    public int getLineNumber() {
        return this.writer.getLineNumber();
    }

    @Override
    public int getRowNumber() {
        return this.writer.getRowNumber();
    }

    @Override
    public void writeComment(final String comment) throws IOException {
        this.writer.writeComment(comment);
    }

    @Override
    public void writeHeader(final String... header) throws IOException {
        this.writer.writeHeader(header);
    }

    @Override
    public void close() throws IOException {
        this.writer.close();
    }

    @Override
    public void flush() throws IOException {
        this.writer.flush();
    }
}
