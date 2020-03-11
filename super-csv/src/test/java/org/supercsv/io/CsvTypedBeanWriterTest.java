/*
 * Copyright 2007 Kasper B. Graversen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.supercsv.io;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;
import org.junit.Assert;
import org.junit.Test;
import org.supercsv.prefs.CsvPreference;

/**
 * Tests the {@link CsvTypedBeanWriterTest}.
 */
public final class CsvTypedBeanWriterTest {

    private static final String COMMA = ",";
    private static final String QUOTE = "\"";

    @Test
    public void writesTypedBeanToCsv() throws Exception {
        try (
            final StringWriter writer = new StringWriter();
            final ICsvTypedBeanWriter<CsvTypedBeanWriterTest.FakeBean> beanWriter =
                new CsvTypedBeanWriter<>(writer, CsvPreference.STANDARD_PREFERENCE)
        ) {
            final Collection<Function<FakeBean, ?>> extractors =
                Arrays.asList(
                    FakeBean::bool,
                    FakeBean::stringField,
                    FakeBean::collection,
                    FakeBean::decimal
                );
            final FakeBean bean = new FakeBean("Oh, String!", false,
                BigDecimal.TEN, Arrays.asList("Foo", "Bar", "Bazz"));
            beanWriter.writeHeader(CsvTypedBeanWriterTest.headers());
            beanWriter.write(Collections.singleton(bean), extractors);
            beanWriter.flush();
            Assert.assertEquals(
                CsvTypedBeanWriterTest.csv(CsvTypedBeanWriterTest.headers(), bean),
                writer.toString()
            );
        }
    }


    private static String csv(final String[] headers, final FakeBean bean) {
        final StringBuilder builder = new StringBuilder();
        builder.append(String.join(COMMA, headers)).append("\r\n");
        builder.append(bean.bool()).append(COMMA)
            .append(QUOTE).append(bean.stringField()).append(QUOTE).append(COMMA)
            .append(QUOTE).append(bean.collection()).append(QUOTE).append(COMMA)
            .append(bean.decimal()).append("\r\n");
        return builder.toString();
    }

    private static String[] headers() {
        return new String[] {"The Boolean", "The String", "Collection", "BigDec" };
    }

    /**
     * Fake bean with different field types.
     * Intentionally NOT compliant with Javabean conventions (not necessary).
     */
    private static class FakeBean {

        private final String stringField;
        private final boolean booleanField;
        private final BigDecimal decimalField;
        private final Collection<String> collectionField;

        private FakeBean(final String stringField, final boolean booleanField,
            final BigDecimal decimalField,
            final Collection<String> collectionField) {
            this.stringField = stringField;
            this.booleanField = booleanField;
            this.decimalField = decimalField;
            this.collectionField = collectionField;
        }

        public String stringField() {
            return this.stringField;
        }

        public boolean bool() {
            return this.booleanField;
        }

        public BigDecimal decimal() {
            return this.decimalField;
        }

        public Collection<String> collection() {
            return this.collectionField;
        }
    }
}
