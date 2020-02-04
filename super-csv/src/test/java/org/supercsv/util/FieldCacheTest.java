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
package org.supercsv.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.mock.ReflectionBean;

import java.lang.reflect.Field;

import static org.junit.Assert.assertNotNull;

public class FieldCacheTest
{
    private FieldCache cache;

    /**
     * Sets up before the test.
     */
    @Before
    public void setUp() {
        cache = new FieldCache();
    }

    /**
     * Tidies up after the test.
     */
    @After
    public void tearDown() {
        cache = null;
    }

    /**
     * Tests getGetField() method.
     */
    @Test
    public void testGetField() throws Exception {
        ReflectionBean bean = new ReflectionBean();
        String fieldName = "name";
        Field field = cache.getField(bean, fieldName);
        assertNotNull(field);
    }

    /**
     * Tests getGetField() with a null object (should throw an exception).
     */
    @Test(expected = NullPointerException.class)
    public void testGetFieldWithNullObject() {
        String fieldName = "name";
        Field field = cache.getField(null, fieldName);
    }

    /**
     * Tests getGetField() with a null field name (should throw an exception).
     */
    @Test(expected = NullPointerException.class)
    public void testGetFieldWithNullFieldName() {
        ReflectionBean bean = new ReflectionBean();
        Field field = cache.getField(bean, null);
    }
}