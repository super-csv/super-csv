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

import java.io.IOException;
import java.util.Collection;
import java.util.function.Function;

/**
 * Interface for all CSV writers writing from strongly typed beans.
 * The major difference from {@link ICsvBeanWriter} is that current writer
 * is strongly typed and don't rely on bean naming conventions.
 * It demands a collection of {@link Function}, which are used to
 * extract particular fields from bean.
 *
 * @author Paul Polishchuk (ppol@ua.fm)
 * @since 2.5.0
 */
public interface ICsvTypedBeanWriter<T> extends ICsvWriter {

    /**
     * Write collection of beans to CSV.
     * @param beans Beans to be written.
     * @param extractors Functions specifying a way to extract bean fields.
     *          The extractors order should match columns order.
     * @throws IOException if an I/O error occurs
     */
    void write(Collection<T> beans, Collection<Function<T, ?>> extractors) throws IOException;
}
